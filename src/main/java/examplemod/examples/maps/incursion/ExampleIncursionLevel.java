package examplemod.examples.maps.incursion;

import examplemod.ExampleMod;
import examplemod.examples.presets.ExamplePreset;
import necesse.engine.GameEvents;
import necesse.engine.events.worldGeneration.GenerateCaveLayoutEvent;
import necesse.engine.events.worldGeneration.GeneratedCaveOresEvent;
import necesse.engine.registries.IncursionPerksRegistry;
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
import necesse.level.maps.presets.Preset;

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
        CaveGeneration cg = new CaveGeneration(this, "deeprocktile", "examplebaserock");
        cg.random.setSeed(incursionData.getUniqueID());

        GameEvents.triggerEvent(
                new GenerateCaveLayoutEvent(this, cg),
                e -> cg.generateLevel(0.38F, 4, 3, 6)
        );

        // Keeps track of occupied space when trying to place presets
        PresetGeneration presets = new PresetGeneration(this);

        // Generate entrance (this reserves space inside presets)
        int spawnSize = 32;
        boolean hasBiggerArenaPerk = altarData.hasPerk(IncursionPerksRegistry.BIGGER_ARENA);
        IncursionBiome.generateEntrance(
                this,
                presets,
                cg.random,
                spawnSize,
                cg.rockTile,
                "exampletile",
                "exampletile",
                "exampleobject",
                hasBiggerArenaPerk
        );

        // Perk presets avoid the entrance preset, since we pass presets as presetGeneration parameter
        generatePresetsBasedOnPerks(altarData, incursionData, presets, cg.random, baseBiome);

        // We add an example preset to the level. We can either decide to do this before or after perk
        // presets. Depending on how important we think it is as part of generation. If not important,
        // then add it after the perks like this
        Preset examplePreset = new ExamplePreset(cg.random);
        presets.findRandomValidPositionAndApply(
                cg.random,
                250, // It tries to place randomly anywhere with this many attempts
                examplePreset,
                8, // How many tiles around the edge of the level it should be within
                true, // randomizeMirrorX
                true, // randomizeMirrorY
                true, // randomizeRotation
                false // overrideCanPlace (false = respect canApply rules)
        );

        // This call clears all invalid objects/tiles, so that there are no cut in half beds, etc.
        GenerationTools.checkValid(this);

        // For extraction incursions, guarantee example ore veins for objectives
        if (incursionData instanceof BiomeExtractionIncursionData) {
            cg.generateGuaranteedOreVeins(40, 4, 8, ObjectRegistry.getObjectID("exampleorerock"));
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