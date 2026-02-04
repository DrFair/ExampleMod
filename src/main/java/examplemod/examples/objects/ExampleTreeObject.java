package examplemod.examples.objects;

import necesse.inventory.lootTable.LootTable;
import necesse.level.gameObject.TreeObject;
import necesse.level.maps.Level;

import java.awt.*;

public class ExampleTreeObject extends TreeObject {
    public ExampleTreeObject(){
        super("exampletree",              // textureName
                "examplelog",                        // logStringID
                "examplesapling",                    // saplingStringID
                new Color(116, 69, 43),     // mapColor
                45,                                  // leavesCenterWidth
                60,                                  // leavesMinHeight
                110,                                 // leavesMaxHeight
                "exampleleaves");                    // leavesTextureName
    }

    // Optional: override drops if you want something different than the base TreeObject default
    // (base TreeObject drops 1-2 saplings + 4-5 logs, splitItems(5))
    @Override
    public LootTable getLootTable(Level level, int layerID, int tileX, int tileY) {
        return super.getLootTable(level, layerID, tileX, tileY);
    }
}
