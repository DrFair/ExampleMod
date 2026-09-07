package examplemod.examples.mobs;

import examplemod.examples.ai.ExampleAI;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.ability.CoordinateMobAbility;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.hostile.HostileMob;
import necesse.entity.particle.FleshParticle;
import necesse.entity.particle.Particle;
import necesse.entity.particle.SmokePuffParticle;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class ExampleMob extends HostileMob {

    // Loaded in examplemod.ExampleMod.initResources()
    public static GameTexture texture;

    public static LootTable lootTable = new LootTable(
            // 50% chance to drop between 1-3 example items
            ChanceLootItem.between(0.5f, "exampleitem", 1, 3)
    );

    // Here we define a mob ability. Mob abilities are an easy way for the server to run some logic and
    // send it to the client over the network.
    // In this case, we use a CoordinateMobAbility which allows us to send a coordinate.
    public final CoordinateMobAbility teleportAbility;

    // MUST HAVE an empty constructor
    public ExampleMob() {
        super(200);
        setSpeed(50);
        setFriction(3);

        // Hitbox, collision box, and select box (for hovering)
        collision = new Rectangle(-10, -7, 20, 14);
        hitBox = new Rectangle(-14, -12, 28, 24);
        selectBox = new Rectangle(-14, -7 - 34, 28, 48);
        // Swim mask values
        swimMaskMove = 16;
        swimMaskOffset = -2;
        swimSinkOffset = -4;

        // We construct and register our teleport ability in the constructor. It will be used in our AI.
        teleportAbility = registerAbility(new CoordinateMobAbility() {
            @Override
            protected void run(int x, int y) {
                if (isClient()) {
                    // If this is run from a client, spawn particles where we were and where we're teleporting
                    getLevel().entityManager.addParticle(new SmokePuffParticle(getLevel(), ExampleMob.this.x, ExampleMob.this.y, new Color(30, 165, 161)), Particle.GType.CRITICAL);
                    getLevel().entityManager.addParticle(new SmokePuffParticle(getLevel(), x, y, new Color(30, 165, 161)), Particle.GType.CRITICAL);
                }
                // Teleport to the position
                setPos(x, y, true);
            }
        });
    }

    // Init happens after the mob was added to a level
    @Override
    public void init() {
        super.init();
        // Setup AI
        ai = new BehaviourTreeAI<>(this, new ExampleAI<ExampleMob>(12 * 32, new GameDamage(25), 25, 40_000) {
            @Override
            public boolean teleport(ExampleMob mob, int x, int y) {
                // Use the teleport ability
                mob.teleportAbility.runAndSend(x, y);
                // And make sure we stop moving when teleported
                getBlackboard().mover.stopMoving(mob);
                return true;
            }
        });
    }

    // The regular loot table, shared between all players
    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    // Called only on the client, when it should spawn death particles
    @Override
    public void spawnDeathParticles(float knockbackX, float knockbackY) {
        // Spawn 4 flesh particles
        for (int i = 0; i < 4; i++) {
            getLevel().entityManager.addParticle(new FleshParticle(
                    getLevel(), texture,
                    GameRandom.globalRandom.nextInt(5), // Randomize between the debris sprites
                    8, // Sprite y coordinate
                    32, // Sprite resolution
                    x, y, 20f, // Position
                    knockbackX, knockbackY // Basically start speed of the particles
            ), Particle.GType.IMPORTANT_COSMETIC);
        }
    }

    @Override
    protected void addDrawables(List<MobDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, Level level, int x, int y, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        super.addDrawables(list, tileList, topList, level, x, y, tickManager, camera, perspective);
        // Tile positions are basically level positions divided by 32. getTileX() does this for us etc.
        GameLight light = level.getLightLevel(getTileX(), getTileY());
        // We always draw mobs so that their "feet" at the center of their collision/hotbox
        int drawX = camera.getDrawX(x) - 32;
        int drawY = camera.getDrawY(y) - 51;

        // A helper method to get the sprite of the current animation/direction of this mob
        Point sprite = getAnimSprite(x, y, getDir());

        drawY += getBobbing(x, y);
        drawY += getLevel().getTile(getTileX(), getTileY()).getMobSinkingAmount(this);

        DrawOptions drawOptions = texture.initDraw()
                .sprite(sprite.x, sprite.y, 64)
                .light(light)
                .pos(drawX, drawY);

        list.add(new MobDrawable() {
            @Override
            public void draw(TickManager tickManager) {
                drawOptions.draw();
            }
        });

        addShadowDrawables(tileList, level, x, y, light, camera);
    }

    @Override
    public int getRockSpeed() {
        // Change the speed at which this mobs animation plays
        return 20;
    }

}
