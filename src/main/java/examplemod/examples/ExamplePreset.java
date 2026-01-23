package examplemod.examples;

import examplemod.ExampleMod;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.util.GameRandom;
import necesse.level.maps.presets.Preset;

public class ExamplePreset extends Preset {
    public ExamplePreset(GameRandom random) {
        // Pass a GameRandom so loot is randomized
        super(15, 11);

        int floor = TileRegistry.getTileID("stonefloor");
        int wall = ObjectRegistry.getObjectID("stonewall");
        int air  = ObjectRegistry.getObjectID("air");
        int storagebox = ObjectRegistry.getObjectID("storagebox"); // replace with what you want

        // Fill background
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setTile(x, y, floor);
                setObject(x, y, air);
            }
        }

        // Simple rectangle “room”
        for (int x = 0; x < width; x++) {
            setObject(x, 0, wall);
            setObject(x, height - 1, wall);
        }
        for (int y = 0; y < height; y++) {
            setObject(0, y, wall);
            setObject(width - 1, y, wall);
        }

        // Put a storagebox
        int storageboxX = width / 2;
        int storageboxY = height / 2;
        setObject(storageboxX,storageboxY, storagebox,1);

        //Fill the storage box with loot from our example loot table
        addInventory(ExampleLootTable.exampleloottable, random,storageboxX,storageboxY);

        // Optional: only allow placing if the whole area isn’t floor already (example rule)
        addCanApplyRectEachPredicate(0, 0, width, height, 0, (level, levelX, levelY, dir) ->
                !level.getTile(levelX, levelY).isFloor
        );
    }
}
