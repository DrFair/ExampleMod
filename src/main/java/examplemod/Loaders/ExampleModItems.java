package examplemod.Loaders;

import examplemod.examples.items.armor.ExampleBootsArmorItem;
import examplemod.examples.items.armor.ExampleChestArmorItem;
import examplemod.examples.items.armor.ExampleHelmetArmorItem;
import examplemod.examples.items.consumable.ExampleFoodItem;
import examplemod.examples.items.consumable.ExamplePotionItem;
import examplemod.examples.items.materials.*;
import examplemod.examples.items.tools.ExampleMagicProjectileWeapon;
import examplemod.examples.items.tools.ExampleMeleeSwordWeapon;
import examplemod.examples.items.tools.ExampleSummonOrbWeapon;
import necesse.engine.registries.ItemRegistry;

public class ExampleModItems {
    public static void load(){

        // Materials
        ItemRegistry.registerItem("exampleitem", new ExampleMaterialItem(), 10, true);
        ItemRegistry.registerItem("examplestone", new ExampleStoneItem(), 15, true);
        ItemRegistry.registerItem("exampleore", new ExampleOreItem(), 25, true);
        ItemRegistry.registerItem("examplebar", new ExampleBarItem(), 50, true);
        ItemRegistry.registerItem("examplehuntincursionmaterial", new ExampleHuntIncursionMaterialItem(), 50, true);
        ItemRegistry.registerItem("examplelog", new ExampleLogItem().setItemCategory("materials","logs"),10,true);
        ItemRegistry.registerItem("examplegrassseed", new ExampleGrassSeedItem(),1,true);

        // Tools
        ItemRegistry.registerItem("examplemeleesword", new ExampleMeleeSwordWeapon(), 20, true);
        ItemRegistry.registerItem("examplemagicstaff", new ExampleMagicProjectileWeapon(), 30, true);
        ItemRegistry.registerItem("examplesummonorb", new ExampleSummonOrbWeapon(),40,true);

        // Armor
        ItemRegistry.registerItem("examplehelmet", new ExampleHelmetArmorItem(), 200f, true);
        ItemRegistry.registerItem("examplechestplate", new ExampleChestArmorItem(), 250f, true);
        ItemRegistry.registerItem("exampleboots", new ExampleBootsArmorItem(), 180f, true);

        // Consumables
        ItemRegistry.registerItem("examplepotion", new ExamplePotionItem(), 10, true);
        ItemRegistry.registerItem("examplefood", new ExampleFoodItem(), 15, true);
    }
}
