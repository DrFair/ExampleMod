package examplemod.Loaders;

import examplemod.examples.tiles.ExampleGrassTile;
import examplemod.examples.tiles.ExampleTile;
import necesse.engine.registries.TileRegistry;

public class ExampleModTiles {

    public static void load(){
        // Register our tiles
        TileRegistry.registerTile("exampletile", new ExampleTile(), 1, true);
        TileRegistry.registerTile("examplegrasstile", new ExampleGrassTile(),1,false,false,true);
    }
}
