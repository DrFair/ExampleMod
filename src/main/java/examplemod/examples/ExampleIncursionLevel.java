package examplemod.examples;

import examplemod.ExampleMod;
import necesse.engine.GameEvents;
import necesse.engine.events.worldGeneration.GenerateCaveLayoutEvent;
import necesse.engine.events.worldGeneration.GeneratedCaveOresEvent;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.world.WorldEntity;
import necesse.level.maps.IncursionLevel;
import necesse.level.maps.generationModules.CaveGeneration;
import necesse.level.maps.generationModules.GenerationTools;
import necesse.level.maps.generationModules.PresetGeneration;
import necesse.level.maps.incursion.AltarData;
import necesse.level.maps.incursion.BiomeExtractionIncursionData;
import necesse.level.maps.incursion.BiomeMissionIncursionData;
import necesse.level.maps.incursion.IncursionBiome;

/**
 * Example incursion level.
 * Demonstrates what is required for a working incursion:
 * cave generation, entrance creation, and ore placement.
 */
public class ExampleIncursionLevel extends IncursionLevel {

    /**
     * A constructor with this signature (LevelIdentifier, int, int, WorldEntity) is required and is used for loading, etc.
     */
    public ExampleIncursionLevel(LevelIdentifier identifier, int width, int height, WorldEntity worldEntity) {
        super(identifier, width, height, worldEntity);
        this.baseBiome = ExampleMod.EXAMPLE_BIOME;
        this.isCave = true;
    }

    /**
     * Constructor used when an incursion is generated and entered.
     * Creates a fixed-size level and immediately generates its contents.
     */
    public ExampleIncursionLevel(LevelIdentifier identifier, BiomeMissionIncursionData incursionData, WorldEntity worldEntity, AltarData altarData) {
        super(identifier, 150, 150, incursionData, worldEntity);
        this.baseBiome = ExampleMod.EXAMPLE_BIOME;
        this.isCave = true;
        generateLevel(incursionData, altarData);
    }

    public void generateLevel(BiomeMissionIncursionData incursionData, AltarData altarData) {
        // Create the cave generator using deep rock tiles for floors and walls
        CaveGeneration cg = new CaveGeneration(this, "deeprocktile", "deeprock");

        // Seed the generator so this incursion layout is deterministic per mission
        cg.random.setSeed(incursionData.getUniqueID());

        // Fire the cave layout generation event, allowing mods or perks to modify
        // or cancel cave generation before the default logic runs
        GameEvents.triggerEvent(
                new GenerateCaveLayoutEvent(this, cg),
                e -> {
                    cg.generateLevel(0.38F, 4, 3, 6);
                }
        );

        // Used to reserve space so later generation steps avoid overwriting the entrance
        PresetGeneration entranceAndPerkPresets = new PresetGeneration(this);

        // Generate an incursion entrance that clears terrain,
        // blends edges, reserves space, and places the return portal
        IncursionBiome.generateEntrance(
                this,
                entranceAndPerkPresets,
                cg.random,
                32,
                cg.rockTile,
                "exampletile",
                "exampletile",
                "exampleobject"
        );

        // Now call incursion perks to generate their presets
        generatePresetsBasedOnPerks(altarData, entranceAndPerkPresets, cg.random, baseBiome);

        // This call clears all invalid objects/tiles, so that there are no cut in half beds, etc.
        GenerationTools.checkValid(this);

        // For extraction incursions, guarantee tungsten ore veins for objectives
        if (incursionData instanceof BiomeExtractionIncursionData) {
            cg.generateGuaranteedOreVeins(40, 4, 8, ObjectRegistry.getObjectID("tungstenoredeeprock"));
        }
        // Generate upgrade shard and alchemy shard ores
        cg.generateGuaranteedOreVeins(75, 6, 12, ObjectRegistry.getObjectID("upgradesharddeeprock"));
        cg.generateGuaranteedOreVeins(75, 6, 12, ObjectRegistry.getObjectID("alchemysharddeeprock"));

        // Last call incursion perks to generate their ores
        generateOresBasedOnPerks(altarData, cg, this, baseBiome, cg.random);

        // Notify listeners that cave ore generation has completed
        GameEvents.triggerEvent(new GeneratedCaveOresEvent(this, cg));
    }
}