package examplemod.Loaders;

import examplemod.examples.items.*;
import examplemod.examples.items.tools.ExampleProjectileWeapon;
import examplemod.examples.items.tools.ExampleSwordItem;
import necesse.engine.registries.ItemRegistry;

public class ExampleModItems {
    public static void load(){

        // Materials
        ItemRegistry.registerItem("exampleitem", new ExampleMaterialItem(), 10, true);
        ItemRegistry.registerItem("examplestone", new ExampleStoneItem(), 15, true);
        ItemRegistry.registerItem("exampleore", new ExampleOreItem(), 25, true);
        ItemRegistry.registerItem("examplebar", new ExampleBarItem(), 50, true);
        ItemRegistry.registerItem("examplehuntincursionmaterial", new ExampleHuntIncursionMaterialItem(), 50, true);

        // Weapons / tools
        ItemRegistry.registerItem("examplesword", new ExampleSwordItem(), 20, true);
        ItemRegistry.registerItem("examplestaff", new ExampleProjectileWeapon(), 30, true);

        // Consumables
        ItemRegistry.registerItem("examplepotion", new ExamplePotionItem(), 10, true);
        ItemRegistry.registerItem("examplefood", new ExampleFoodItem(), 15, true);
    }
}
