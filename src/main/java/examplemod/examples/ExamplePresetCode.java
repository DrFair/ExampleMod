package examplemod.examples;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.util.GameRandom;
import necesse.level.maps.presets.Preset;

/**
 * ExamplePresetCode
 * This class describes a small "structure" (a room) that the game can stamp into the world.
 * In Necesse, a "Preset" is basically a small grid of tiles + objects that can be placed onto a level.
 * This version builds the room using normal Java code (loops and variables),
 * instead of using a big PRESET={...} text script.
 */
public class ExamplePresetCode extends Preset {

    /**
     * Constructor
     * Constructors run when you create the object: new ExamplePresetCode(random)
     * The GameRandom is passed in so things like loot can be randomized,
     * but still be repeatable (important for world generation).
     */
    public ExamplePresetCode(GameRandom random) {

        // This calls the Preset parent class constructor.
        // It sets the size of the preset to 15 tiles wide and 11 tiles tall.
        super(15, 11);

        /*
         * Tiles and Objects in Necesse use numeric IDs internally.
         *
         * - TileRegistry.getTileID("name") looks up a TILE by its string ID
         * - ObjectRegistry.getObjectID("name") looks up an OBJECT by its string ID
         *
         * We store those numbers in variables so we can use them repeatedly.
         */
        int floor = TileRegistry.getTileID("stonefloor");        // ground tile
        int wall = ObjectRegistry.getObjectID("stonewall");      // wall object
        int air  = ObjectRegistry.getObjectID("air");            // "nothing here" object
        int storagebox = ObjectRegistry.getObjectID("storagebox"); // chest/container object

        /*
         * Fill the entire preset area with a base:
         *
         * - Every tile becomes stone floor
         * - Every object becomes air (empty)
         *
         * width and height are fields from the Preset parent class (because we called super(15, 11)).
         */
        for (int x = 0; x < width; x++) {        // loop across columns (left -> right)
            for (int y = 0; y < height; y++) {   // loop across rows (top -> bottom)
                setTile(x, y, floor);            // place the floor tile at (x, y)
                setObject(x, y, air);            // clear any object at (x, y)
            }
        }

        /*
         * Build the walls around the edge of the preset.
         *
         * First: top wall (y = 0) and bottom wall (y = height - 1)
         */
        for (int x = 0; x < width; x++) {
            setObject(x, 0, wall);               // top edge
            setObject(x, height - 1, wall);      // bottom edge
        }

        /*
         * Next: left wall (x = 0) and right wall (x = width - 1)
         */
        for (int y = 0; y < height; y++) {
            setObject(0, y, wall);               // left edge
            setObject(width - 1, y, wall);       // right edge
        }

        /*
         * Choose a position in the middle of the room for the storage box.
         *
         * width / 2 and height / 2 are integer division in Java.
         * Example: 15 / 2 becomes 7 (Java drops the .5)
         */
        int storageboxX = width / 2;
        int storageboxY = height / 2;

        /*
         * Place the storage box object at the center.
         *
         * setObject(x, y, objectID, rotation)
         *
         * Some objects use rotation to decide which way they face.
         * 0/1/2/3 usually mean different directions.
         * For a storage box it usually doesn't matter much, but we set it anyway.
         */
        setObject(storageboxX, storageboxY, storagebox, 1);

        /*
         * Fill the storage box with loot.
         *
         * ExampleLootTable.exampleloottable is your custom LootTable from the other class.
         *
         * addInventory(...) searches for an object with an inventory at that position
         * (like a storagebox) and then generates loot into it.
         */
        addInventory(ExampleLootTable.exampleloottable, random, storageboxX, storageboxY);

        /*
         * OPTIONAL SAFETY RULE (CanApply predicate):
         *
         * This says: "Only allow this preset to be placed if the area is suitable."
         *
         * addCanApplyRectEachPredicate(...) checks every tile in a rectangle.
         * If ANY tile fails the test, the preset cannot be applied there.
         *
         * Our test:
         *   !level.getTile(levelX, levelY).isFloor
         *
         * Meaning:
         *   - If the tile already IS a floor, then this returns false (bad)
         *   - If the tile is NOT a floor, then this returns true (good)
         *
         * In plain English:
         *   "Don't place this room on top of an area that already has flooring."
         */
        addCanApplyRectEachPredicate(0, 0, width, height, 0,
                (level, levelX, levelY, dir) -> !level.getTile(levelX, levelY).isFloor
        );
    }
}
