package examplemod.examples.objects;
import necesse.level.gameObject.RockObject;

import java.awt.Color;

public class ExampleBaseRockObject extends RockObject {

    public ExampleBaseRockObject() {
        super("examplebaserock", new Color(92, 37, 23), "examplestone", "objects", "landscaping");
        this.toolTier = 5.0F;
    }
}
