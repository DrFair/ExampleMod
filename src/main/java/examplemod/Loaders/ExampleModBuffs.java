package examplemod.Loaders;

import examplemod.examples.buffs.ExampleArmorSetBuff;
import examplemod.examples.buffs.ExampleArrowBuff;
import examplemod.examples.buffs.ExampleBuff;
import examplemod.examples.buffs.ExampleTrinketBuff;
import necesse.engine.registries.BuffRegistry;

public class ExampleModBuffs {

    public static void load() {
        // Register our buff
        BuffRegistry.registerBuff("examplebuff", new ExampleBuff());

        // Register our armor set bonus, used in ExampleHelmetArmorItem
        BuffRegistry.registerBuff("examplearmorsetbonusbuff", new ExampleArmorSetBuff());

        // Register our Arrow Buff
        BuffRegistry.registerBuff("examplearrowbuff", new ExampleArrowBuff());

        // Register our Trinket Buff
        BuffRegistry.registerBuff("exampletrinketbuff",new ExampleTrinketBuff());
    }

}
