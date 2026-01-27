package examplemod.Loaders;

import examplemod.examples.ExampleTile;
import necesse.engine.registries.TileRegistry;

public class ExampleModTiles {

    public static void load(){
        // Register our tiles
        TileRegistry.registerTile("exampletile", new ExampleTile(), 1, true);
    }
}
