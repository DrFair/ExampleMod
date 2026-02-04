package examplemod.examples.maps.incursion;

import examplemod.ExampleMod;
import examplemod.examples.ExamplePreset;
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
import necesse.level.maps.presets.Preset;

import java.awt.*;

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

        //entrance + perks (anything that must never be overwritten by perk presets)
        PresetGeneration entranceAndPerkPresets = new PresetGeneration(this);

        //your own structures (custom rooms, etc.)
        PresetGeneration structurePresets = new PresetGeneration(this);

        // Generate entrance (this reserves space inside entranceAndPerkPresets)
        int spawnSize = 32;
        Point entranceMid = IncursionBiome.generateEntrance(
                this,
                entranceAndPerkPresets,
                cg.random,
                spawnSize,
                cg.rockTile,
                "exampletile",
                "exampletile",
                "exampleobject"
        );

        // reserve the entrance space in structurePresets too, so your own structures don't overwrite the entrance area.
        int ex = entranceMid.x - spawnSize / 2;
        int ey = entranceMid.y - spawnSize / 2;
        structurePresets.addOccupiedSpace(ex, ey, spawnSize, spawnSize);


        //EXAMPLE PRESET
        Preset examplePreset = new ExamplePreset(cg.random);
        Point placedAt = structurePresets.findRandomValidPositionAndApply(
                cg.random,
                2500,//realistically this would be lower if you didn't want it to be guaranteed
                examplePreset,
                8,
                true,// randomizeMirrorX
                true,  // randomizeMirrorY
                true,  // randomizeRotation
                false  // overrideCanPlace (false = respect canApply rules)
        );

        if (placedAt != null) {
            structurePresets.addOccupiedSpace(placedAt.x, placedAt.y, examplePreset.width, examplePreset.height);
            entranceAndPerkPresets.addOccupiedSpace(placedAt.x, placedAt.y, examplePreset.width, examplePreset.height);
        }

        /*
        Perk presets use entranceAndPerkPresets, so they avoid the entrance as well as any presets
        you added to structurePresets
        */
        generatePresetsBasedOnPerks(altarData, entranceAndPerkPresets, cg.random, baseBiome);

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