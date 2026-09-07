package examplemod.loaders;

import necesse.engine.journal.JournalEntry;
import necesse.engine.registries.JournalRegistry;
import necesse.engine.util.LevelIdentifier;

public class ExampleModJournal {

    public static void load() {
        // Surface
        JournalEntry exampleBiomeJournalSurface = JournalRegistry.registerJournalEntry(
                "examplebiomesurface",
                new JournalEntry(ExampleModBiomes.EXAMPLE_BIOME, LevelIdentifier.SURFACE_IDENTIFIER)
        );
        // Content lists inside the journal page
        exampleBiomeJournalSurface.addBiomeLootEntry("examplelog");
        exampleBiomeJournalSurface.addMobEntries("examplemob");

        // Caves
        JournalEntry exampleBiomeJournalCave = JournalRegistry.registerJournalEntry(
                "examplebiomecave",
                new JournalEntry(ExampleModBiomes.EXAMPLE_BIOME, LevelIdentifier.CAVE_IDENTIFIER)
        );
        // Content lists inside the journal page
        exampleBiomeJournalCave.addBiomeLootEntry("exampleore","examplestone");
        exampleBiomeJournalCave.addMobEntries("examplemob");

        // Deep Caves
        JournalEntry exampleBiomeJournalDeepCave = JournalRegistry.registerJournalEntry(
                "examplebiomedeepcave",
                new JournalEntry(ExampleModBiomes.EXAMPLE_BIOME, LevelIdentifier.DEEP_CAVE_IDENTIFIER)
        );
        // Content lists inside the journal page
        exampleBiomeJournalDeepCave.addBiomeLootEntry("exampleore","examplestone");
        exampleBiomeJournalDeepCave.addMobEntries("examplemob");
    }

}
