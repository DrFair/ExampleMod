package examplemod.examples.mobs;

import examplemod.examples.ai.ExampleBossAI;
import examplemod.loaders.ExampleModResources;
import necesse.engine.eventStatusBars.EventStatusBarManager;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.MusicRegistry;
import necesse.engine.sound.PositionSoundEffect;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.sound.SoundSettings;
import necesse.engine.sound.gameSound.GameSound;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.MobDrawable;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.ability.EmptyMobAbility;
import necesse.entity.mobs.ai.behaviourTree.BehaviourTreeAI;
import necesse.entity.mobs.hostile.bosses.FlyingBossMob;
import necesse.entity.particle.FleshParticle;
import necesse.entity.particle.Particle;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptions;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.inventory.lootTable.lootItem.RotationLootItem;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

// Extends FlyingBossMob, which makes the boss not collide with any objects, etc.
public class ExampleBossMob extends FlyingBossMob {

    // Loaded in examplemod.ExampleMod.initResources()
    public static GameTexture texture;

    // Items this boss drops on rotation for each player
    public static RotationLootItem uniqueDrops = RotationLootItem.privateLootRotation(
            new LootItem("examplemeleesword"),
            new LootItem("examplemagicstaff"),
            new LootItem("examplesummonorb"),
            new LootItem("examplerangedbow"));

    // The loot table that is private for each individual player
    public static LootTable privateLootTable = new LootTable(uniqueDrops);

    // Deals 40 collision damage. We use this in a getter later
    public static GameDamage collisionDamage = new GameDamage(40);

    // Similar to ExampleMob, we define an ability that can be run from the server
    public EmptyMobAbility chargeSoundAbility;

    // MUST HAVE an empty constructor
    public ExampleBossMob() {
        super(2000);
        setSpeed(50);
        setFriction(3);
        // Bosses don't save by default, but we could define that here if we want to
//        this.shouldSave = true;

        // Hitbox, collision box, and select box (for hovering)
        collision = new Rectangle(-10, -7, 20, 14);
        hitBox = new Rectangle(-14, -12, 28, 24);
        selectBox = new Rectangle(-14, -7 - 34, 28, 48);
        // Swim mask values
        swimMaskMove = 16;
        swimMaskOffset = -2;
        swimSinkOffset = -4;

        // Register our charge sound ability. It will be used in our boss AI
        chargeSoundAbility = registerAbility(new EmptyMobAbility() {
            @Override
            protected void run() {
                // Play a sound on the client when this ability is run from the server
                if (isClient()) {
                    // Choose one of the ascended wizard sounds
                    GameSound sound = GameRandom.globalRandom.getOneOf(
                            GameResources.ascendedWizardHurt1,
                            GameResources.ascendedWizardHurt2,
                            GameResources.ascendedWizardHurt3
                    );

                    // Define the effect to come from the mob itself
                    PositionSoundEffect effect = SoundEffect.effect(ExampleBossMob.this)
                            .volume(2f) // Let's make it loud
                            .falloffDistance(2000);

                    // Play the sound
                    SoundManager.playSound(sound, effect);
                }
            }
        });
    }

    // Init happens after the boss was added to a level
    @Override
    public void init() {
        super.init();
        // Setup AI
        ai = new BehaviourTreeAI<>(this, new ExampleBossAI<>());

        // We want to play a spawn sound here. Only do so on the client
        if (isClient()) {
            // When passing a sound to somewhere for playing, you can use the SoundSettings class to
            // specify stuff like pitch, falloff distance, volume, etc.
            SoundSettings soundSettings = new SoundSettings(ExampleModResources.EXAMPLE_SOUND)
                    .volume(0.8f)
                    .basePitch(1.0f)
                    .pitchVariance(0.08f)
                    .fallOffDistance(1500); // Large falloff distance since this is a boss spawned

            // Finally, play the sound with this mob as the emitter
            soundSettings.play(this);
        }
    }

    // Client tick happens only on the clients game ticks (20 times a second)
    @Override
    public void clientTick() {
        super.clientTick();

        // Only show boss bar when the client player is close enough
        if (isClientPlayerNearby()) {
            EventStatusBarManager.registerMobHealthStatusBar(this);
        }

        // Make sure the boss music is playing
        SoundManager.setMusic(MusicRegistry.AscendedReturn, SoundManager.MusicPriority.EVENT, 1.5F);
    }

    // Return the defined collision damage in this override method
    @Override
    public GameDamage getCollisionDamage(Mob target, boolean fromPacket, ServerClient packetSubmitter) {
        return collisionDamage;
    }

    // The private loot table, unique to each player
    @Override
    public LootTable getPrivateLootTable() {
        return privateLootTable;
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
        // Defines the speed at which this mobs animation plays (used in getAnimSprite(...))
        return 20;
    }

}
