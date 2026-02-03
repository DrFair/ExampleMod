package examplemod.Loaders;

import examplemod.ExampleMod;
import examplemod.examples.maps.biomes.ExampleBiome;
import necesse.engine.registries.BiomeRegistry;

public class ExampleModBiomes {
    public static void load() {
        // Register a simple biome that will not appear in natural world gen.
        ExampleMod.EXAMPLE_BIOME = BiomeRegistry.registerBiome("examplebiome", new ExampleBiome(), false);
    }
}



