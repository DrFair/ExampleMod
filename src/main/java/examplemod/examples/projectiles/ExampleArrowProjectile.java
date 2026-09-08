package examplemod.examples.projectiles;

import examplemod.loaders.ExampleModBuffs;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.util.GameRandom;
import necesse.entity.levelEvent.mobAbilityLevelEvent.AmethystGlyphEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobBeforeHitCalculatedEvent;
import necesse.entity.mobs.MobBeforeHitEvent;
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

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

public class ExampleArrowProjectile extends Projectile {

    // Must have an empty constructor for the registry to accept it
    public ExampleArrowProjectile() {
    }

    @Override
    public void init() {
        super.init();
        // Projectile starts at height 18 (roughly where the players bow is)
        height = 18;

        // Height reduces over time as the projectile travels
        heightBasedOnDistance = true;

        // Has 8 pixels of width for collision with terrain and targets
        setWidth(8);

        // This arrow does not do damage to targets. Instead, we apply a buff in doHitLogic
        doesImpactDamage = false;
    }

    @Override
    protected Stream<Mob> streamTargets(Mob owner, Shape hitBounds) {
        // If owner is null, use default targeting
        if (owner == null) {
            return super.streamTargets(owner, hitBounds);
        }

        // This arrow is supposed to hit friendly targets, similar to gem staves/glyphs
        // The Amethyst Glyph event has a static method we can use to stream targets
        return AmethystGlyphEvent.streamBuffableTargets(getLevel(), hitBounds, owner)
                .filter(mob -> mob != owner); // Don't hit the owner
    }

    @Override
    public boolean canHit(Mob mob) {
        // Since we can hit all our targets from streamTargets(..), we just return true here
        return true;
    }

    @Override
    public void doHitLogic(Mob mob, LevelObjectHit object, float x, float y) {
        super.doHitLogic(mob, object, x, y);

        // doHitLogic happens both on server and client.
        // If mob is null, it means it hit an object or traveled max distance
        // If object is null, it means it hit a mob or traveled max distance
        if (!isServer() || mob == null) return;

        // We want this arrow to heal the mob it hits
        // Heal amount will be the damage the arrow does
        // And the health will be given over a duration
        // Game design wise, this might be a bit OP with something like greatbows. But just as an example :D

        // We get the owner of the projectile for later use
        Mob owner = getOwner();

        // To calculate the actual damage, we use the hit events similar to how mobs does it
        // This ensures that we calculate crit chance and crit damage correctly
        MobBeforeHitEvent hitEvent = new MobBeforeHitEvent(mob, owner, getDamage(), 0f, 0f, 0f);
        MobBeforeHitCalculatedEvent calculatedEvent = new MobBeforeHitCalculatedEvent(hitEvent);

        // Now we can get the final damage this hit should do as a number
        int totalDamage = calculatedEvent.damage;

        // And we want to calculate how much healing that is per game tick
        float duration = 4; // Heal duration in seconds
        float healthPerSecond = totalDamage / duration;
        float healthPerGameTick = healthPerSecond / TickManager.ticksPerSec;

        // We then use our registered arrow buff to actually apply the healing
        // To give a buff to a player, we have to create an ActiveBuff that contains the duration and data we need
        ActiveBuff activeBuff = new ActiveBuff(ExampleModBuffs.EXAMPLE_ARROW_BUFF, mob, duration, owner);

        // Active buffs can have extra data in the form of GND data (Game Network Data)
        // In this case, we store the healthPerGameTick on it
        activeBuff.getGndData().setFloat("healthPerGameTick", healthPerGameTick);

        // Lastly we add the buff to the target
        // The buff class is what actually handles the healing, etc.
        mob.buffManager.addBuff(
                activeBuff,
                true, // Since this logic only happening on the server, we make sure to send it to clients
                true // We force override the previous buff if it existed
        );
    }

    @Override
    public void dropItem() {
        // Optional: Drop your arrow item sometimes, like vanilla StoneArrowProjectile does.
        if (GameRandom.globalRandom.getChance(0.5f)) {
            getLevel().entityManager.pickups.add(new InventoryItem("examplearrow").getPickupEntity(getLevel(), x, y));
        }
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list,
                             OrderableDrawables tileList, OrderableDrawables topList, OrderableDrawables overlayList,
                             Level level, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        if (removed()) return;

        GameLight light = level.getLightLevel(this);
        int drawX = camera.getDrawX(x) - texture.getWidth() / 2;
        int drawY = camera.getDrawY(y);

        TextureDrawOptionsEnd options = texture.initDraw()
                .light(light)
                .rotate(getAngle(), texture.getWidth() / 2, 0)
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

}
