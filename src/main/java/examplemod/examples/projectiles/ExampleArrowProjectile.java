package examplemod.examples.projectiles;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.NetworkClient;
import necesse.engine.util.GameRandom;
import necesse.engine.util.GameUtils;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.projectile.Projectile;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.EntityDrawable;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObjectHit;
import necesse.level.maps.light.GameLight;

public class ExampleArrowProjectile extends Projectile {


    @Override
    public void init() {
        super.init();
        this.height = 18.0F;
        this.heightBasedOnDistance = true;
        setWidth(8.0F);
        this.doesImpactDamage = false;
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list,
                             OrderableDrawables tileList, OrderableDrawables topList, OrderableDrawables overlayList,
                             Level level, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        if (removed()) return;

        GameLight light = level.getLightLevel(this);
        int drawX = camera.getDrawX(this.x) - this.texture.getWidth() / 2;
        int drawY = camera.getDrawY(this.y);

        final TextureDrawOptionsEnd options = this.texture.initDraw()
                .light(light)
                .rotate(getAngle(), this.texture.getWidth() / 2, 0)
                .pos(drawX, drawY - (int)getHeight());

        list.add(new EntityDrawable(this) {
            @Override
            public void draw(TickManager tickManager) {
                options.draw();
            }
        });

        // Shadow
        addShadowDrawables(tileList, drawX, drawY, light, getAngle(), 0);
    }
    @Override
    protected Stream<Mob> streamTargets(Mob owner, Shape hitBounds) {
        // The projectile calls this to ask: “Which mobs should I check collisions against this tick?”

        Level level = getLevel();
        // If we don’t have a level there’s nothing to test.
        if (level == null || hitBounds == null) return Stream.empty();

        // collect non player mobs
        NetworkClient attackerClient = (owner == null) ? null : GameUtils.getAttackerClient(owner);

        // enemies/settlers/animals/etc) that are inside the projectile_attach hit area.
        Stream<Mob> mobs = level.entityManager.mobs
                .streamInRegionsShape(hitBounds, 1) //query mobs in nearby regions

                // Is valid target logic
                .filter(m -> owner == null || m.canBeTargeted(owner, attackerClient));


        // Collect players even if pvp is off
        Stream<PlayerMob> players = level.entityManager.players
                .streamInRegionsShape(hitBounds, 1)

                // Ignore null, the shooter, and players that were removed from the world.
                .filter(p -> p != null && p != owner && !p.removed())

                /* this makes sure the player has a network client
                *  the client has spawned and is not dead
                *  the playerMob exists and has a valid Level reference
                */
                .filter(p -> {
                    NetworkClient c = p.getNetworkClient();
                    return (c != null
                            && !c.isDead()
                            && c.hasSpawned()
                            && c.playerMob != null
                            && c.playerMob.getLevel() != null);
                });

        // Combine the two streams into one “things we can collide with” stream.
        // PlayerMob is a subclass of Mob
        return Stream.concat(mobs, players.map(p -> (Mob) p));
    }

    @Override
    public boolean canHit(Mob mob) {
        Mob owner = getOwner();

        // Allow hitting allies
        if (owner != null && (mob == owner || mob.isSameTeam(owner))) {
            return true;
        }

        // Otherwise use normal rules (enemies, etc.)
        return super.canHit(mob);
    }

    @Override
    public void doHitLogic(Mob mob, LevelObjectHit object, float x, float y) {
        super.doHitLogic(mob, object, x, y);

        if (!isServer() || mob == null) return;

        int durationMs = 4000; // 4 seconds regen
        int healPerTick = 5;   // healed every 250ms in the buff = 20 HP/sec

        // Try get existing buff
        ActiveBuff existing = mob.buffManager.getBuff("examplearrowbuff");
        if (existing != null) {
            // refresh duration
            existing.setDurationLeft(durationMs);

            // optionally stack strength
            int current = existing.getGndData().getInt("healPerTick");
            existing.getGndData().setInt("healPerTick", Math.max(current, healPerTick));

            return;
        }

        // create new
        ActiveBuff regen = new ActiveBuff("examplearrowbuff", mob, durationMs, getOwner());
        regen.getGndData().setInt("healPerTick", healPerTick);

        // add the buff, send an update packet to clients and force update the buff
        mob.buffManager.addBuff(regen, true,true,true);
    }

    @Override
    public void dropItem() {
        // Optional: drop your arrow item sometimes, like vanilla StoneArrowProjectile does.
        if (GameRandom.globalRandom.getChance(0.5F)) {
            getLevel().entityManager.pickups.add(new InventoryItem("examplearrow").getPickupEntity(getLevel(), this.x, this.y)
            );
        }
    }
}
