package examplemod;

import examplemod.examples.*;
import examplemod.examples.incursion.ExampleBiome;
import examplemod.examples.incursion.ExampleIncursionBiome;
import examplemod.examples.incursion.ExampleIncursionLevel;
import examplemod.examples.items.*;
import examplemod.examples.items.tools.ExampleProjectileWeapon;
import examplemod.examples.items.tools.ExampleSwordItem;
import examplemod.examples.mobs.ExampleMob;
import examplemod.examples.mobs.ExampleBossMob;
import examplemod.examples.objects.*;
import examplemod.examples.ExampleRecipes;
import examplemod.examples.packets.ExamplePacket;
import examplemod.examples.packets.ExamplePlaySoundPacket;
import necesse.engine.commands.CommandsManager;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.*;
import necesse.engine.sound.SoundSettings;
import necesse.engine.sound.gameSound.GameSound;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.biomes.Biome;

@ModEntry
public class ExampleMod {

    // We define our static registered objects here, so they can be referenced elsewhere
    public static ExampleBiome EXAMPLE_BIOME;
    public static GameSound EXAMPLESOUND;
    public static SoundSettings EXAMPLESOUNDSETTINGS;

    public void init() {
        System.out.println("Hello world from my example mod!");

        // Register a simple biome that will not appear in natural world gen.
        EXAMPLE_BIOME = BiomeRegistry.registerBiome("exampleincursion", new ExampleBiome(), false);

        // Register the incursion biome with tier requirement 1.
        IncursionBiomeRegistry.registerBiome("exampleincursion", new ExampleIncursionBiome(), 1);

        // Register the level class used for the incursion.
        LevelRegistry.registerLevel("exampleincursionlevel", ExampleIncursionLevel.class);

        // Register our tiles
        TileRegistry.registerTile("exampletile", new ExampleTile(), 1, true);

        // Register our objects
        ObjectRegistry.registerObject("exampleobject", new ExampleObject(), 2, true);

        // Register a rock object
        ExampleBaseRockObject exampleBaseRock = new ExampleBaseRockObject();
        ObjectRegistry.registerObject("examplebaserock", exampleBaseRock, -1.0F, true);

        // Register an ore rock object that overlays onto our incursion rock
        ObjectRegistry.registerObject("exampleorerock", new ExampleOreRockObject(exampleBaseRock), -1.0F, true);

        // Register a wall object, window object and door object
        ExampleWallWindowDoorObject.registerWallsDoorsWindows();

        // Register our items
        ItemRegistry.registerItem("exampleitem", new ExampleMaterialItem(), 10, true);
        ItemRegistry.registerItem("examplestone", new ExampleStoneItem(),15,true);
        ItemRegistry.registerItem("exampleore", new ExampleOreItem(), 25, true);
        ItemRegistry.registerItem("examplebar", new ExampleBarItem(),50,true);
        ItemRegistry.registerItem("examplehuntincursionmaterial", new ExampleHuntIncursionMaterialItem(), 50, true);
        ItemRegistry.registerItem("examplesword", new ExampleSwordItem(), 20, true);
        ItemRegistry.registerItem("examplestaff", new ExampleProjectileWeapon(), 30, true);
        ItemRegistry.registerItem("examplepotion", new ExamplePotionItem(), 10, true);
        ItemRegistry.registerItem("examplefood", new ExampleFoodItem(),15, true);

        // Register our mob
        MobRegistry.registerMob("examplemob", ExampleMob.class, true);

        // Register boss mob
        MobRegistry.registerMob("examplebossmob",ExampleBossMob.class,true,true);

        // Register our projectile
        ProjectileRegistry.registerProjectile("exampleprojectile", ExampleProjectile.class, "exampleprojectile", "exampleprojectile_shadow");

        // Register our buff
        BuffRegistry.registerBuff("examplebuff", new ExampleBuff());

        // Register our packets
        PacketRegistry.registerPacket(ExamplePacket.class);

        PacketRegistry.registerPacket(ExamplePlaySoundPacket.class);
    }

    public void initResources() {
        // Sometimes your textures will have a black or other outline unintended under rotation or scaling
        // This is caused by alpha blending between transparent pixels and the edge
        // To fix this, run the preAntialiasTextures gradle task
        // It will process your textures and save them again with a fixed alpha edge color

        ExampleMob.texture = GameTexture.fromFile("mobs/examplemob");
        ExampleBossMob.texture = GameTexture.fromFile("mobs/examplebossmob");

        //initialising the sound to be used by our boss mob
        EXAMPLESOUND = GameSound.fromFile("examplesound");

        // Optional settings (volume/pitch/falloff) – used when playing via SoundSettings
        EXAMPLESOUNDSETTINGS = new SoundSettings(EXAMPLESOUND)
                .volume(0.8f)
                .basePitch(1.0f)
                .pitchVariance(0.08f)
                .fallOffDistance(900);
    }

    public void postInit() {
        // load our recipes from the ExampleRecipes class
        ExampleRecipes.registerRecipes();

        // Add our example mob to default cave mobs.
        // Spawn tables use a ticket/weight system. In general, common mobs have about 100 tickets.
        Biome.defaultCaveMobs
                .add(100, "examplemob");

        // Register our server chat command
        CommandsManager.registerServerCommand(new ExampleChatCommand());
    }

}
