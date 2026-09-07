package examplemod.Loaders;

import examplemod.examples.maps.biomes.ExampleBiome;
import examplemod.examples.presets.ExampleWorldPreset;
import necesse.engine.registries.BiomeRegistry;
import necesse.engine.registries.WorldPresetRegistry;

public class ExampleModBiomes {

    public static ExampleBiome EXAMPLE_BIOME;

    public static void load() {
        // Register a simple biome that will not appear in natural world gen.
        EXAMPLE_BIOME = BiomeRegistry.registerBiome("examplebiome", new ExampleBiome(), false);

        // Here we also register our presets, which we want to be part of procedural generation.
        // Take a look inside the ExampleWorldPreset class to see how they work
        WorldPresetRegistry.registerPreset("exampleworldpreset", new ExampleWorldPreset());
    }

}



