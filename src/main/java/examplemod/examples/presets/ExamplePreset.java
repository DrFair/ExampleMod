package examplemod.examples.presets;

import examplemod.examples.ExampleLootTable;
import necesse.engine.util.GameRandom;
import necesse.level.maps.presets.Preset;

/**
 * ExamplePreset (Script-based)
 * This preset is the same idea as the code-built room, but it is created using a big text string
 * in Necesse's "PRESET script" format.
 */
public class ExamplePreset extends Preset {

    /**
     * You pass in GameRandom so anything random (like loot) can be rolled properly.
     * In world generation, Necesse often uses a seeded random so the same world seed
     * produces the same results every time.
     */
    public ExamplePreset(GameRandom random) {

        // Create a preset that is 11 tiles wide and 11 tiles tall.
        // The Preset parent class uses this to create arrays for tiles/objects/rotations.
        super(11, 11);

        /*
         * This is a PRESET script string.
         *
         * It's basically a "saved blueprint" of a structure.
         * The game can export these, and you can paste them into code like this.
         *
         * The important parts
         *
         * width / height
         *   - Size of the structure.
         *
         * tileIDs + tiles
         *   - "tileIDs" is a list of tile types used in this preset.
         *   - "tiles" is the full grid.
         *   - Each number in "tiles" refers to an entry from tileIDs.
         *
         * objectIDs + objects
         *   - Same idea as tiles, but for objects
         *   - "objectIDs" is the palette.
         *   - "objects" is the full grid.
         *
         * rotations
         *   - Rotation for each placed object (same length/order as the objects grid).
         *   - Most objects use rotation 0/1/2/3 for directions.
         *
         * ...Clear flags...
         *   - These tell the game whether it should clear decorations/walls/etc when stamping the preset.
         *
         * The string is huge because it contains *every tile* in the 11x11 grid.
         * 11 x 11 = 121 entries, which matches the long arrays you see.
         */
        String examplePresetScript =
                "PRESET={width=11,height=11," +
                        "tileIDs=[98, exampletile]," +
                        "tiles=[98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98]," +
                        "objectIDs=[0, air, 290, storagebox, 1436, examplewall, 298, walltorch]," +
                        "objects=[1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 298, 0, 0, 0, 0, 0, 0, 0, 298, 1436, 1436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1436, 1436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1436, 1436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1436, 1436, 0, 0, 0, 0, 290, 0, 0, 0, 0, 1436, 1436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1436, 1436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1436, 1436, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1436, 1436, 298, 0, 0, 0, 0, 0, 0, 0, 298, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436, 1436]," +
                        "rotations=[2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2]," +
                        "tileObjectsClear=true,wallDecorObjectsClear=true,tableDecorObjectsClear=true," +
                        "clearOtherWires=false}\n";

        /*
         * applyScript(...) reads that big PRESET string and fills in:
         * - which tiles exist at each coordinate
         * - which objects exist at each coordinate
         * - which rotations the objects use
         *
         * After this line runs, this Preset now "contains" that room layout.
         */
        this.applyScript(examplePresetScript);

        /*
         * Add loot into the storage box inside the preset.
         *
         * The idea here is:
         * Coordinates here are PRESET coordinates, not world coordinates.
         *
         * So (5, 5) means:
         * - 5 tiles from the left edge of the preset
         * - 5 tiles from the top edge of the preset
         *
         * We are assuming the storage box was placed at that coordinate in the script.
         */
        addInventory(ExampleLootTable.exampleLootTable, random, 5, 5);

        /*
         * Optional placement rule:
         *
         * addCanApplyRectEachPredicate checks a rectangle area and decides if the preset is allowed
         * to be stamped there.
         *
         * This can prevent things like:
         * - placing the room on top of an existing base
         * - overwriting important tiles
         *
         * The lambda (level, levelX, levelY, dir) -> ... is a short way to write a function.
         *
         * Our rule says:
         *   "If the world tile is already a floor, do NOT allow the preset to be placed."
         *
         * The ! means "not".
         * So:
         * - if isFloor is true, !isFloor is false then placement fails
         * - if isFloor is false, !isFloor is true then placement is allowed
         */
        addCanApplyRectEachPredicate(0, 0, width, height, 0,
                (level, levelX, levelY, dir) -> !level.getTile(levelX, levelY).isFloor
        );
    }
}
