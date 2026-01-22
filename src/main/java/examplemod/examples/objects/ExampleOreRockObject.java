package examplemod.examples.objects;
import necesse.level.gameObject.RockObject;
import necesse.level.gameObject.RockOreObject;

import java.awt.Color;

/**
 * Example ore rock that uses our ExampleIncursionDeepRockObject as its parent rock.
 */
public class ExampleOreRockObject extends RockOreObject {

    public ExampleOreRockObject(RockObject parentRock) {
        super(
                parentRock,
                "oremask",
                "exampleore",
                new Color(90, 40, 160),
                "exampleore",
                1,
                3,
                2,
                true,
                "objects", "landscaping");
    }
}
