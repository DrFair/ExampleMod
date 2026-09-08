package examplemod.loaders;

import examplemod.examples.events.ExampleLevelEvent;
import necesse.engine.registries.LevelEventRegistry;

public class ExampleModEvents {

    public static void load() {
        // Register our LevelEvent
        LevelEventRegistry.registerEvent("examplelevelevent", ExampleLevelEvent.class);
    }

}



