package examplemod.Loaders;

import examplemod.examples.buffs.*;
import necesse.engine.registries.BuffRegistry;

public class ExampleModBuffs {
        public static void load(){
            // Register our buff
            BuffRegistry.registerBuff("examplebuff", new ExampleBuff());
            BuffRegistry.registerBuff("examplearmorsetbonus", new ExampleArmorSetBuff());
            BuffRegistry.registerBuff("examplearrowbuff", new ExampleArrowBuff());
        }
}
