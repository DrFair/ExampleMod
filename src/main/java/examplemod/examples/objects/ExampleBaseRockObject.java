package examplemod.examples.objects;

import necesse.level.gameObject.RockObject;

import java.awt.*;

public class ExampleBaseRockObject extends RockObject {

    public ExampleBaseRockObject() {
        super(
                "examplebaserock", // Texture for the base rock
                new Color(92, 37, 23), // Minimap color
                "examplestone", // Dropped stone stringID
                "objects", "landscaping" // Item categories
        );
        toolTier = 0; // Tier of pickaxe required to mine this rock
        // Specific tool tiers are stored in ItemRegistry class. For example, Ivy can be accessed like this:
        // toolTier = ItemRegistry.IVY_TOOL_TIER;
    }

}
