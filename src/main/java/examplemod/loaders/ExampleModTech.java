package examplemod.loaders;

import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Tech;

public class ExampleModTech {

    public static Tech EXAMPLE_TECH;

    public static void load() {
        // All recipes have some tech that they are assigned to
        // Crafting stations then define which techs that can be crafted there
        // Even the forge is also looking at recipes for the forge tech

        // Here we register our own tech for our example crafting stations

        // stringID: how recipes refer to it internally
        // itemStringID: used for icon/tooltips (usually your crafting station item id)
        EXAMPLE_TECH = RecipeTechRegistry.registerTech("exampletech", "exampleworkstation");

        // Remember to also add the tech to your locale file. The name of the tech will be
        // shown in the crafting guide book, etc. (This is already done in this example)
    }

}
