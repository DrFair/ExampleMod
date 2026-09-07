package examplemod.loaders;

import examplemod.examples.settlement.settlers.ExampleSettler;
import necesse.engine.registries.SettlerRegistry;


public class ExampleModSettlers {

    public static void load() {
        // Register our settler used by ExampleHumanMob
        SettlerRegistry.registerSettler("examplesettler", new ExampleSettler());
    }

}
