package examplemod;

import examplemod.loaders.*;
import necesse.engine.modLoader.annotations.ModEntry;

@ModEntry
public class ExampleMod {

    // Global access point for mod settings
    public static ExampleModSettings SETTINGS;

    // Load settings for the example mod from the external file defined in ExampleModSettings
    public ExampleModSettings initSettings() {
        SETTINGS = new ExampleModSettings();
        return SETTINGS;
    }

    public void init() {
        System.out.println("Hello world from my example mod!");
        SETTINGS.logLoadedSettings(); // log the loaded settings for debug

        // Note: If you're using Intellij IDEA, you can ctrl+click the different references
        // like "load()" to jump to their code and see how they work!

        // The order which you register your content is important. Before registering any
        // objects, items, etc. you want to register anything that might use that. In our case,
        // we register our item categories, recipe tech and packets first. If we had custom
        // global ingredients, we would also register them here
        ExampleModTech.load();
        ExampleModCategories.load();
        ExampleModPackets.load();

        // Next we register all our content:
        // Our tiles, objects and items in that order
        ExampleModTiles.load();
        ExampleModObjects.load();
        ExampleModItems.load();

        // Our biomes/level
        ExampleModBiomes.load();
        ExampleModIncursions.load();

        // Now any entities and content the entities use after
        ExampleModProjectiles.load();
        ExampleModBuffs.load();
        ExampleModMobs.load();

        // Content that our entities use
        ExampleModSettlers.load(); // Settlers
        ExampleModJobs.load(); // Jobs
        ExampleModEvents.load(); // Level events, etc.

        // Lastly, anything that uses our content, entities, etc. Like our adventure journal entries
        ExampleModJournal.load();

        // And anything remaining, like our chat commands
        ExampleModCommands.load();
    }

    public void initResources() {
        ExampleModResources.load();
    }

    public void postInit() {
        // Load our recipes from the ExampleRecipes class so we can keep this class easy to read
        ExampleModRecipes.registerRecipes();
    }

}
