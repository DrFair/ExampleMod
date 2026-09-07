package examplemod.loaders;

import examplemod.examples.mobs.ExampleBossMob;
import examplemod.examples.mobs.ExampleHumanMob;
import examplemod.examples.mobs.ExampleMob;
import examplemod.examples.mobs.ExampleSummonWeaponMob;
import necesse.engine.registries.MobRegistry;

public class ExampleModMobs {

    public static void load() {
        // Register base example mob
        MobRegistry.registerMob(
                "examplemob", // The stringID of the mob
                ExampleMob.class, // The mob class which contains the empty constructor
                true // If the mob can be killed or not, and should count in player stats
        );

        // Register boss mob. This tile we also add isBossMob parameter and set that to true
        MobRegistry.registerMob("exampleboss", ExampleBossMob.class,true,true);

        // Register summon weapon mob. This time we set coundKillStat to false, because you cannot kill this mob
        MobRegistry.registerMob("examplesummon", ExampleSummonWeaponMob.class, false);

        // Register example human mob (ExampleHumanMob that uses ExampleSettler for settler settings and is capable of our ExampleLevelJob)
        MobRegistry.registerMob("examplehuman", ExampleHumanMob.class, true);
    }

}
