package examplemod.examples.objects;

import necesse.level.gameObject.RockObject;
import necesse.level.gameObject.RockOreObject;

import java.awt.*;

/**
 * Example ore rock that uses our ExampleIncursionDeepRockObject as its parent rock.
 */
public class ExampleOreRockObject extends RockOreObject {

    public ExampleOreRockObject(RockObject parentRock) {
        super(
                parentRock,
                "oremask", // Ore mask image
                "exampleore", // Ore texture name
                new Color(90, 40, 160), // Minimap Color
                "exampleore", // Dropped ore stringID
                1, // Min ores dropped
                3, // Max ores dropped
                2, // Placed dropped ore - not actually ued right now
                true, // Is incursion extraction mission object
                "objects", "landscaping" // Item categories
        );
    }

}
