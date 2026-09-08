package examplemod.examples.objects;

import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.gameObject.GrassObject;
import necesse.level.maps.Level;

import java.awt.*;

public class ExampleGrassObject extends GrassObject {

    public ExampleGrassObject() {
        // "examplegrass" is the texture name
        // 2 = max density (how many adjacent grass objects can be next to each other before they stop growing)
        super("examplegrass", 2);
        mapColor = new Color(112, 0, 109); // Minimap color
    }

    @Override
    public LootTable getLootTable(Level level, int layerID, int tileX, int tileY) {
        if (level.objectLayer.isPlayerPlaced(tileX, tileY)) {
            // If the grass is player placed (like from the landscaping station), it should just drop the grass itself
            return super.getLootTable(level, layerID, tileX, tileY);
        } else {
            // Else 4% chance to drop an example grass seed
            return new LootTable(
                    new ChanceLootItem(0.04f, "examplegrassseed")
            );
        }
    }

}