package examplemod.examples.objects;

import necesse.inventory.lootTable.LootTable;
import necesse.level.gameObject.TreeObject;
import necesse.level.maps.Level;

import java.awt.*;

public class ExampleTreeObject extends TreeObject {

    public ExampleTreeObject() {
        super(
                "exampletree", // Texture name
                "examplelog", // Log item stringID
                "examplesapling", // Sapling stringID
                new Color(87, 6, 86), // Minimap color
                45, // Width of the tree's crown texture (where dropped leaves will spawn from)
                60, // Min height that leaves will be dropped from
                110, // Max height that leaves will be dropped from
                "exampleleaves" // Leaves texture name
        );
    }

    // Optional: override drops if you want something different than the base TreeObject default
    // Base TreeObject drops 1-2 saplings + 4-5 logs
    @Override
    public LootTable getLootTable(Level level, int layerID, int tileX, int tileY) {
        return super.getLootTable(level, layerID, tileX, tileY);
    }

}
