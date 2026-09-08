package examplemod.examples.presets;

import examplemod.examples.ExampleLootTable;
import necesse.engine.util.GameRandom;
import necesse.level.maps.presets.Preset;

/**
 * Presets are predefined builds (tiles, objects, etc), which can be used in world generation
 */
public class ExamplePreset extends Preset {

    /**
     * There are several different ways to construct a preset. The most common and easiest way is to copy a
     * build you've made in the game using either:
     * F10 -> Dev tool -> Copy preset -> Select an area -> Press "Copy to clipboard"
     * or
     * Be in creative mode -> In the tools creative tab -> Use "Select and copy to clipboard" -> Select an area
     *
     * Doing this will copy it to your clipboard, and you can then paste it in a string like I did below.
     *
     * You can also simply pass in the tile width/height of the preset you want to create,
     * and later apply the preset script you have copied
     */
    public ExamplePreset(GameRandom random) {
        super("PRESET = {\n" +
                "\twidth = 11,\n" +
                "\theight = 11,\n" +
                "\ttileIDs = [105, exampletile],\n" +
                "\ttiles = [105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105, 105],\n" +
                "\tobjectIDs = [0, air, 309, walltorch, 1610, examplewall, 300, storagebox],\n" +
                "\tobjects = [1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 309, 0, 0, 0, 0, 0, 0, 0, 309, 1610, 1610, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1610, 1610, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1610, 1610, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1610, 1610, 0, 0, 0, 0, 300, 0, 0, 0, 0, 1610, 1610, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1610, 1610, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1610, 1610, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1610, 1610, 309, 0, 0, 0, 0, 0, 0, 0, 309, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610, 1610],\n" +
                "\trotations = [2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2],\n" +
                "\ttileObjectsClear = true,\n" +
                "\twallDecorObjectsClear = true,\n" +
                "\ttableDecorObjectsClear = true,\n" +
                "\tclearOtherWires = false\n" +
                "}");

        // The data in a preset is stored in arrays with the object IDs, tile IDs, etc.
        // If you have not set anything yet, the default data will be -1, which means pasting the preset
        // will not replace anything on the level. You can also set specific areas of the preset to -1,
        // which allows having presets that are other shapes than square

        // To apply other preset scripts later or at specific offsets, you can use:
        // applyPreset(x, y, ...);

        // Coordinates are from top-left corner

        // And you can set tile/object data with:
        // setTile(x, y, ...);
        // setObject(x, y, ...);


        // You can also add a loot table based on the random seed from the constructor parameter
        // In this case, the script we used have a chest in the middle. So we add loot to that like this:
        addInventory(ExampleLootTable.exampleLootTable, random, 5, 5);

        // When you want to use them in world generation, you can also add placement rules
        // In this case, we do not allow placement on anything that is not floor tiles
        addCanApplyRectEachPredicate(0, 0, width, height, 0,
                (level, levelX, levelY, dir) -> !level.getTile(levelX, levelY).isFloor
        );
        // The reason you give a "dir" (direction) and there is one in the apply lambda as well,
        // is because the preset might be rotated/mirrored. In those cases, the dir in the lambda will
        // be different from the on you gave as a parameter
        // There are several other helper methods like the one above as well


        // If you want to place presets you've created in the surface or caves (infinite procedural generation),
        // this is done through the WorldPresetRegistry. Take a look in ExampleModBiomes where we register our
        // own ExampleWorldPreset for a bunch of comments on how they work
    }

}
