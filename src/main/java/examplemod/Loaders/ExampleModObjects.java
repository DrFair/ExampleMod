package examplemod.Loaders;

import examplemod.examples.objects.*;
import necesse.engine.registries.ObjectRegistry;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.WallObject;
import necesse.level.maps.presets.set.ChestRoomSet;
import necesse.level.maps.presets.set.ColumnSet;
import necesse.level.maps.presets.set.WallSet;

import java.awt.*;

public class ExampleModObjects {

    // Expose IDs for other classes (biomes, levels, etc.)
    public static int EXAMPLE_BASE_ROCK_ID = -1;
    public static int EXAMPLE_ORE_ROCK_ID  = -1;

    // Wall and chest room sets are used for generating presets, etc. This will be set later in the load method
    public static WallSet EXAMPLE_WALL_SET = null;
    public static ChestRoomSet EXAMPLE_CHEST_ROOM_SET = null;

    public static void load() {
        // Register our objects

        ObjectRegistry.registerObject("exampleobject", new ExampleObject(), 2, true);


        // Register a rock object
        ExampleBaseRockObject exampleBaseRock = new ExampleBaseRockObject();
        // If you give a negative value as broker value, the game will calculate the broker value based on the recipe for this item's ingredients
        // -1 will be 1*ingredient cost, -2 will be 2 * ingredient cost, etc.
        EXAMPLE_BASE_ROCK_ID = ObjectRegistry.registerObject("examplebaserock", exampleBaseRock, -1f, true);

        // Register an ore rock object that overlays onto our incursion rock
        EXAMPLE_ORE_ROCK_ID = ObjectRegistry.registerObject("exampleorerock", new ExampleOreRockObject(exampleBaseRock), -1f, true);

        // Register a wall object, window object and door object
        WallObject.registerWallObjects(
                "example", // Prefix used for stringIDs
                "examplewall", // Texture name
                0, // Tool tier
                new Color(255, 220, 80), // Map color
                ToolType.PICKAXE, // Tool type used to mine it
                -1f, // Wall broker value
                -1f, // Door broker value
                true // Obtainable
        );
        // If you need the ids, registerWallObjects will return an array with the wall, door, door open and window ids
        // in that order. You can also fetch them later with ObjectRegistry.getID("examplewall"), etc.

        // Register a tree object
        ObjectRegistry.registerObject("exampletree",new ExampleTreeObject(),0,false,false,true);

        // Register a sapling object
        ObjectRegistry.registerObject("examplesapling", new ExampleTreeSaplingObject(),10,true);

        // Register a grass object
        ObjectRegistry.registerObject("examplegrass",new ExampleGrassObject(),1,true);

        // Register an object which uses a level event
        ObjectRegistry.registerObject("exampleleveleventobject", new ExampleLevelEventObject(),1,true);

        // Register ExampleJobObject an object that triggers our new job to happen //DEBUG
        ObjectRegistry.registerObject("examplejobobject",new ExampleJobObject(),1,true);

        // Register an object that uses the mods config file
        ObjectRegistry.registerObject("exampleconfigobject", new ExampleConfigObject(),1,true);

        // Register an example pressure plate object
        ObjectRegistry.registerObject("examplepressureplate",new ExamplePressurePlateObject(),1,true);

        // Get the wall object we want this trap to attach to.
        // ObjectRegistry stores everything as a generic "GameObject"
        // so we fetch by string ID ("examplewall") and cast it to WallObject.
        // Takes the texture of the wall object and overlays our "examplewalltrap"
        WallObject exampleWall = (WallObject) ObjectRegistry.getObject("examplewall");
        ObjectRegistry.registerObject("examplewalltrap",new ExampleWallTrapObject(exampleWall),1,true);
        EXAMPLE_WALL_SET = new WallSet("example");
        EXAMPLE_CHEST_ROOM_SET = new ChestRoomSet(
                "exampletile", // Our tile stringID
                "examplepressureplate", // Our pressure plate stringID
                EXAMPLE_WALL_SET, // Our wall set
                ColumnSet.wood, // For columns, we just use wood
                "storagebox", // Normal storage box as chest
                "examplewalltrap" // Our wall trap stringID
        );

        // Register example workstation
        ExampleWorkstationObject.register();
    }

}
