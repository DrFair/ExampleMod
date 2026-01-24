package examplemod.examples.objects;

import necesse.level.gameObject.WallObject;
import necesse.inventory.item.toolItem.ToolType;

import java.awt.Color;



public final class ExampleWallWindowDoorObject {
    private ExampleWallWindowDoorObject() {}

    public static int EXAMPLEWALL;
    public static int EXAMPLEDOOR;
    public static int EXAMPLEDOOROPEN;
    public static int EXAMPLEWINDOW;

    public static void registerWallsDoorsWindows() {
        // registers wall, door pair and window objecs
        int[] ids = WallObject.registerWallObjects(
                "example",// prefix
                "examplewall",        // texture name
                "walloutlines",      // outline texture
                0.0F,                // tool tier
                new Color(255, 220, 80),
                ToolType.PICKAXE,
                -1.0F,  // wall broker value
                -1.0F,               // door broker value
                true,                // obtainable
                true                 // count in stats
        );

        EXAMPLEWALL = ids[0];
        EXAMPLEDOOR = ids[1];
        EXAMPLEDOOROPEN = ids[2];
        EXAMPLEWINDOW   = ids[3];
    }
}