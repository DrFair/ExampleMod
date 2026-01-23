package examplemod.examples;

import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.lootTable.lootItem.OneOfLootItems;

public class ExampleLootTable {
    public static final LootTable exampleloottable = new LootTable(
        new LootItem("exampleore", 8),
        new LootItem("examplebar", 20),
        new LootItem("examplepotion",1),
        new LootItem("examplefood",1),
        new OneOfLootItems(
            new ChanceLootItem(0.60f, "examplesword"),
            new ChanceLootItem(0.60f,"examplestaff")
        )
    );

    private ExampleLootTable() { }
}