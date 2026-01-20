package examplemod.examples.objects;
import necesse.level.gameObject.RockObject;

import java.awt.Color;

public class ExampleBaseRockObject extends RockObject {

    public static final String ID = "examplebaserock";

    public ExampleBaseRockObject() {
        super("examplebaserock", new Color(92, 37, 23), "stone", "objects", "landscaping");
        this.toolTier = 5.0F;
    }
}
