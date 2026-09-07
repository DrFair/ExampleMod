package examplemod;

import examplemod.Loaders.*;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.sound.gameSound.GameSound;

@ModEntry
public class ExampleMod {

    // Global access point for mod settings
    public static ExampleModSettings SETTINGS;

    // We define our static registered objects here, so they can be referenced elsewhere
    public static GameSound EXAMPLE_SOUND;

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

        // Register Tech Trees
        ExampleModTech.load();

        // Register categories first: Used by Items/Objects to appear correctly in Creative/crafting trees
        ExampleModCategories.load();

        // Register packets early: Anything networked (mobs, settlers, job UIs, events) can safely reference packet IDs
        ExampleModPackets.load();

        // Core content building blocks first: Tiles/Objects/Items are referenced by biomes, incursions, mobs, projectiles, buffs, etc.
        ExampleModTiles.load();
        ExampleModObjects.load();
        ExampleModItems.load();

        // Combat + entity registries next: Projectiles and buffs often reference items/mobs, and mobs can reference buffs/projectiles.
        ExampleModProjectiles.load();
        ExampleModBuffs.load();
        ExampleModMobs.load();

        // Settlement systems after mobs/items exist: Settlers are mobs; jobs can reference settlers, items, and packets/UI.
        ExampleModSettlers.load();
        ExampleModJobs.load();

        // World generation last-ish: Biomes/incursions can safely reference all registered tiles/objects/mobs/items now.
        ExampleModBiomes.load();
        ExampleModIncursions.load();

        // Events after everything is registered: Lets event listeners safely reference IDs and content without ordering surprises.
        ExampleModEvents.load();

        // Journal last: JournalEntry.addMobEntries() resolves MobRegistry immediately at registration time.
        ExampleModJournal.load();
    }

    public void initResources() {
        ExampleModResources.load();
    }

    public void postInit() {
        // load our recipes from the ExampleRecipes class so we can keep this class easy to read
        ExampleModRecipes.registerRecipes();
    }

}
