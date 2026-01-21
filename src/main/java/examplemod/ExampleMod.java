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
import examplemod.examples.objects.ExampleObject;
import examplemod.examples.objects.ExampleBaseRockObject;
import examplemod.examples.objects.ExampleOreRockObject;
import examplemod.examples.ExampleRecipes;
import necesse.engine.commands.CommandsManager;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.*;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.biomes.Biome;

@ModEntry
public class ExampleMod {

    // We define our static registered objects here, so they can be referenced elsewhere
    public static ExampleBiome EXAMPLE_BIOME;

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

        // Register a rock for the example incursion to use as cave walls
        ExampleBaseRockObject exampleBaseRock = new ExampleBaseRockObject();
        ObjectRegistry.registerObject(ExampleBaseRockObject.ID, exampleBaseRock, -1.0F, true);

        // Register an ore rock that overlays onto our incursion rock
        ObjectRegistry.registerObject(ExampleOreRockObject.ID, new ExampleOreRockObject(exampleBaseRock), -1.0F, true);

        // Register our items
        ItemRegistry.registerItem("exampleitem", new ExampleMaterialItem(), 10, true);
        ItemRegistry.registerItem("exampleoreitem", new ExampleOreItem(), 25, true);
        ItemRegistry.registerItem("examplebaritem", new ExampleBarItem(),50,true);
        ItemRegistry.registerItem("examplehuntincursionitem", new ExampleHuntIncursionMaterialItem(), 50, true);
        ItemRegistry.registerItem("examplesword", new ExampleSwordItem(), 20, true);
        ItemRegistry.registerItem("examplestaff", new ExampleProjectileWeapon(), 30, true);
        ItemRegistry.registerItem("examplepotionitem", new ExamplePotionItem(), 10, true);
        ItemRegistry.registerItem("examplefooditem", new ExampleFoodItem(),15, true);

        // Register our mob
        MobRegistry.registerMob("examplemob", ExampleMob.class, true);

        // Register boss mob
        MobRegistry.registerMob("examplebossmob",ExampleBossMob.class,true,true);

        // Register our projectile
        ProjectileRegistry.registerProjectile("exampleprojectile", ExampleProjectile.class, "exampleprojectile", "exampleprojectile_shadow");

        // Register our buff
        BuffRegistry.registerBuff("examplebuff", new ExampleBuff());

        // Register our packet
        PacketRegistry.registerPacket(ExamplePacket.class);
    }

    public void initResources() {
        // Sometimes your textures will have a black or other outline unintended under rotation or scaling
        // This is caused by alpha blending between transparent pixels and the edge
        // To fix this, run the preAntialiasTextures gradle task
        // It will process your textures and save them again with a fixed alpha edge color

        ExampleMob.texture = GameTexture.fromFile("mobs/examplemob");

        ExampleBossMob.texture = GameTexture.fromFile("mobs/examplebossmob");
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
