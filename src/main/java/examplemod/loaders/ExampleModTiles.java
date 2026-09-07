package examplemod.loaders;

import examplemod.examples.tiles.ExampleGrassTile;
import examplemod.examples.tiles.ExampleTile;
import necesse.engine.registries.TileRegistry;

public class ExampleModTiles {

    public static int EXAMPLE_TILE_ID;
    public static int EXAMPLE_GRASS_TILE_ID;

    public static void load() {
        // Register our tiles
        EXAMPLE_TILE_ID = TileRegistry.registerTile("exampletile", new ExampleTile(), 1, true);
        EXAMPLE_GRASS_TILE_ID = TileRegistry.registerTile("examplegrasstile", new ExampleGrassTile(),1,false,false,true);
    }

}
