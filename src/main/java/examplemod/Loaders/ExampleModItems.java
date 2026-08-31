package examplemod.Loaders;

import examplemod.examples.items.ammo.ExampleArrowItem;
import examplemod.examples.items.armor.ExampleBootsArmorItem;
import examplemod.examples.items.armor.ExampleChestArmorItem;
import examplemod.examples.items.armor.ExampleHelmetArmorItem;
import examplemod.examples.items.consumable.ExampleBossSummonItem;
import examplemod.examples.items.consumable.ExampleFoodItem;
import examplemod.examples.items.consumable.ExamplePotionItem;
import examplemod.examples.items.materials.*;
import examplemod.examples.items.tools.ExampleBowRangedWeapon;
import examplemod.examples.items.tools.ExampleOrbSummonWeapon;
import examplemod.examples.items.tools.ExampleStaffMagicWeapon;
import examplemod.examples.items.tools.ExampleSwordMeleeWeapon;
import examplemod.examples.items.trinkets.ExampleTrinketItem;
import necesse.engine.registries.ItemRegistry;

public class ExampleModItems {

    public static void load() {
        // Materials
        ItemRegistry.registerItem("exampleitem", new ExampleMaterialItem(), 10, true);
        ItemRegistry.registerItem("examplestone", new ExampleStoneItem(), 15, true);
        ItemRegistry.registerItem("exampleore", new ExampleOreItem(), 25, true);
        ItemRegistry.registerItem("examplebar", new ExampleBarItem(), 50, true);
        ItemRegistry.registerItem("examplelog", new ExampleLogItem(), 10, true);
        ItemRegistry.registerItem("examplegrassseed", new ExampleGrassSeedItem(), 1, true);

        // Tools
        ItemRegistry.registerItem("examplemeleesword", new ExampleSwordMeleeWeapon(), 20, true);
        ItemRegistry.registerItem("examplemagicstaff", new ExampleStaffMagicWeapon(), 30, true);
        ItemRegistry.registerItem("examplesummonorb", new ExampleOrbSummonWeapon(), 40, true);
        ItemRegistry.registerItem("examplerangedbow", new ExampleBowRangedWeapon(), 10, true);

        // Armor
        ItemRegistry.registerItem("examplehelmet", new ExampleHelmetArmorItem(), 200, true);
        ItemRegistry.registerItem("examplechestplate", new ExampleChestArmorItem(), 250, true);
        ItemRegistry.registerItem("exampleboots", new ExampleBootsArmorItem(), 180, true);

        // Consumables
        ItemRegistry.registerItem("examplepotion", new ExamplePotionItem(), 10, true);
        ItemRegistry.registerItem("examplefood", new ExampleFoodItem(), 15, true);
        ItemRegistry.registerItem("examplebosssummonitem", new ExampleBossSummonItem(), 1, true);

        // Ammo
        ItemRegistry.registerItem("examplearrow", new ExampleArrowItem(), 5, true);

        // Trinkets
        ItemRegistry.registerItem("exampletrinket", new ExampleTrinketItem(), 1, true);
    }

}
