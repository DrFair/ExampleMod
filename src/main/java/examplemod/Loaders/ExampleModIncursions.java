package examplemod.Loaders;

import examplemod.ExampleMod;
import examplemod.examples.incursion.ExampleBiome;
import examplemod.examples.incursion.ExampleIncursionBiome;
import examplemod.examples.incursion.ExampleIncursionLevel;
import necesse.engine.registries.BiomeRegistry;
import necesse.engine.registries.IncursionBiomeRegistry;
import necesse.engine.registries.LevelRegistry;

public class ExampleModIncursions {
    public static void load() {

        // Register a simple biome that will not appear in natural world gen.
        ExampleMod.EXAMPLE_BIOME = BiomeRegistry.registerBiome("exampleincursion", new ExampleBiome(), false);

        // Register the incursion biome with tier requirement 1.
        IncursionBiomeRegistry.registerBiome("exampleincursion", new ExampleIncursionBiome(), 1);

        // Register the level class used for the incursion.
        LevelRegistry.registerLevel("exampleincursionlevel", ExampleIncursionLevel.class);
    }
}
