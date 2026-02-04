package examplemod.examples.objects;

import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.gameObject.GrassObject;
import necesse.level.maps.Level;

public class ExampleGrassObject extends GrassObject {

    public ExampleGrassObject() {
        // "examplegrass" is the texture name, 2 = variants/height setting used by GrassObject
        super("examplegrass", 2);
    }

    public LootTable getLootTable(Level level, int tileX, int tileY) {
        // 4% chance, tweak to match vanilla feel
        return new LootTable(
                new ChanceLootItem(0.04F, "examplegrassseed")
        );
    }
}