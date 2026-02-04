package examplemod.Loaders;

import examplemod.examples.maps.incursion.ExampleIncursionBiome;
import examplemod.examples.maps.incursion.ExampleIncursionLevel;
import necesse.engine.registries.IncursionBiomeRegistry;
import necesse.engine.registries.LevelRegistry;

public class ExampleModIncursions {
    public static void load() {

        // Register the incursion biome with tier requirement 1.
        IncursionBiomeRegistry.registerBiome("exampleincursion", new ExampleIncursionBiome(), 1);

        // Register the level class used for the incursion.
        LevelRegistry.registerLevel("exampleincursionlevel", ExampleIncursionLevel.class);
    }
}
