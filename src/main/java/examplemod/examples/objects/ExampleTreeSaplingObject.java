package examplemod.examples.objects;

import necesse.level.gameObject.TreeSaplingObject;

import java.awt.*;

public class ExampleTreeSaplingObject extends TreeSaplingObject {

    public ExampleTreeSaplingObject(){
        super(
                "examplesapling", // Texture name,
                new Color(122, 0, 121), // The map and debris color
                "exampletree", // Grown object stringID
                30 * 60, // Min grow time in seconds - 30 minutes
                45 * 60, // Max grow time in seconds - 45 minutes
                true, // Can be used as "Any sapling" ingredient
                "examplegrasstile"
        );
    }

}
