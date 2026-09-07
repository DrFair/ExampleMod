package examplemod.examples;

import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.*;

/**
 * This loot table can be referenced from presets, object entities (like storage boxes),
 * mobs, or any system that accepts a LootTable instance.
 */
public class ExampleLootTable {

    /**
     * A reusable LootTable instance.
     * The LootTable constructor takes a list of "loot entries" which are rolled when loot is generated.
     * Each entry can be:
     *  - guaranteed items (LootItem)
     *  - probabilistic items (ChanceLootItem or ChanceLootItemList)
     *  - groups like "pick one of these" (OneOfLootItems)
     *  Or any custom implementation of LootItemInterface
     */
    public static final LootTable exampleLootTable = new LootTable(

            // Rotating entries:
            // This uses the (level + AtomicInteger lootRotation) arguments that chest rooms pass in.
            // If it does not get the correct arguments, it will just generate a random one in the list
            RotationLootItem.presetRotation(
                    new LootItem("exampletrinket"),
                    new LootItem("examplehelmet"),
                    new LootItem("examplechestplate"),
                    new LootItem("exampleboots")
            ),
            // Guaranteed drops:
            // LootItem(String itemStringID, int amount)
            // These are always added when the table is rolled.
            LootItem.between("examplebar", 2, 4), // Between 2 and 4 example bar
            new LootItem("examplepotion"), // Just one potion

            // 60% chance for a single example food item
            new ChanceLootItem(0.6f, "examplefood"),

            // Next, a 50% chance to generate a OneOfLootItems
            // OneOfLootItems will pick ONE option from the list
            new ChanceLootItemList(0.5f, new OneOfLootItems(
                    new LootItem("examplemeleesword"),
                    new LootItem("examplemagicstaff"),
                    new LootItem("examplesummonorb"),
                    new LootItem("examplerangedbow")
            ))
    );

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used statically: ExampleLootTable.exampleloottable
     */
    private ExampleLootTable() {
    }

}