package examplemod.examples;

import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.lootTable.lootItem.OneOfLootItems;

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
     *  - probabilistic items (ChanceLootItem)
     *  - groups like "pick one of these" (OneOfLootItems)
     */
    public static final LootTable exampleloottable = new LootTable(

            // Guaranteed drops:
            // LootItem(String itemStringID, int amount)
            // These are always added when the table is rolled.
            new LootItem("exampleore", 8),
            new LootItem("examplebar", 20),
            new LootItem("examplepotion", 1),
            new LootItem("examplefood", 1),
            new LootItem("examplesapling", 1),

            // Group entry: OneOfLootItems will attempt to pick ONE option from the list below.
            // In your case, the options are "chance-based" items.
            new OneOfLootItems(

                    // ChanceLootItem(float chance, String itemStringID)
                    // 0.60f = 60% chance for this item to be granted IF this option is selected.
                    // Because these are inside OneOfLootItems, the group will choose a single option,
                    // then that option rolls its chance.
                    new ChanceLootItem(0.60f, "examplesword"),
                    new ChanceLootItem(0.60f, "examplestaff")
            )
    );

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used statically: ExampleLootTable.exampleloottable
     */
    private ExampleLootTable() {
    }
}