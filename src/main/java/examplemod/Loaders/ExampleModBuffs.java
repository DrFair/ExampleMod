package examplemod.Loaders;

import examplemod.examples.ExampleBuff;
import necesse.engine.registries.BuffRegistry;

public class ExampleModBuffs {
        public static void load(){
            // Register our buff
            BuffRegistry.registerBuff("examplebuff", new ExampleBuff());
        }
}
