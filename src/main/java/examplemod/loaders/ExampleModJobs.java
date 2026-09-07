package examplemod.loaders;

import examplemod.examples.settlement.jobs.ExampleLevelJob;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.registries.JobTypeRegistry;
import necesse.engine.registries.LevelJobRegistry;
import necesse.entity.mobs.job.JobType;

public class ExampleModJobs {

    public static void load() {
        // 1) Register the job type
        JobTypeRegistry.registerType("examplejobtype",
                new JobType(
                        true,  // canChangePriority (shows in settlement UI)
                        true,  // defaultDisabledBySettler (locked for normal settlers)
                        new LocalMessage("jobs", "examplejobname"),
                        new LocalMessage("jobs", "examplejobtip")
                )
        );

        // 2) Register our ExampleLevelJob //DEBUG
        LevelJobRegistry.registerJob("examplejob", ExampleLevelJob .class, ExampleLevelJob::handler, "examplejobtype");
    }

}
