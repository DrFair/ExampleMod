package examplemod.loaders;

import examplemod.examples.events.ExampleLevelEvent;
import necesse.engine.registries.LevelEventRegistry;

public class ExampleModEvents {

    public static void load() {
        // Register our Level Event to the registry
        LevelEventRegistry.registerEvent("examplelevelevent", ExampleLevelEvent.class);
    }

}



