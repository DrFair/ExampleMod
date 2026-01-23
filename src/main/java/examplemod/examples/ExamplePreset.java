package examplemod.examples;

import necesse.engine.util.GameRandom;
import necesse.level.maps.presets.Preset;

public class ExamplePreset extends Preset {
    // Pass a GameRandom so loot is randomized
    public ExamplePreset(GameRandom random) {
        //width and height of our preset
        super(11, 11);

        /*string output of the preset from the game decoded from url safe base64 zlib compressed text
        you don't actually need to decompress this but it makes showing whats going on easier */
        String examplePresetScript = "PRESET={width=11,height=11,tileIDs=[98, exampletile],tiles=[98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98, 98],objectIDs=[0, air, 290, storagebox, 85, woodwall, 298, walltorch],objects=[85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 298, 0, 0, 0, 0, 0, 0, 0, 298, 85, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 85, 0, 0, 0, 0, 290, 0, 0, 0, 0, 85, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 85, 85, 298, 0, 0, 0, 0, 0, 0, 0, 298, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85],rotations=[2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2],tileObjectsClear=true,wallDecorObjectsClear=true,tableDecorObjectsClear=true,clearOtherWires=false}\n";

        //actually apply the preset from examplePresetScript onto the world
        this.applyScript(examplePresetScript);

        //Fill the storage box with loot from our example loot table
        addInventory(ExampleLootTable.exampleloottable, random,5,5);

        // Optional: only allow placing if the whole area isn’t floor already (example rule)
        addCanApplyRectEachPredicate(0, 0, width, height, 0, (level, levelX, levelY, dir) ->
                !level.getTile(levelX, levelY).isFloor
        );
    }
}
