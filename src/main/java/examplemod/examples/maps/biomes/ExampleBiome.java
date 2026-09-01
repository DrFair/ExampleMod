package examplemod.examples.maps.biomes;

import examplemod.Loaders.ExampleModObjects;
import examplemod.Loaders.ExampleModTiles;
import examplemod.examples.ExampleLootTable;
import necesse.engine.AbstractMusicList;
import necesse.engine.MusicList;
import necesse.engine.registries.MusicRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.util.GameRandom;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.world.biomeGenerator.BiomeGeneratorStack;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.Biome;
import necesse.level.maps.biomes.MobSpawnTable;
import necesse.level.maps.presets.RandomCaveChestRoom;
import necesse.level.maps.presets.caveRooms.CaveRuins;
import necesse.level.maps.regionSystem.Region;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A biome controls stuff like:
 * - World generation features of this biome
 * - Mob spawns and biome specific mob drops
 * - What music is playing in the biome
 */
public class ExampleBiome extends Biome {

    // Here we construct the mob spawn table for later use
    public static MobSpawnTable mobSpawnTable = new MobSpawnTable()
            .add(100, "examplemob");


    // Set up the loot interface for our boss summon extra drop
    public static LootTable randomExampleBossSummonDrop = new LootTable(
            // 10% chance to drop
            new ChanceLootItem(0.1f, "examplebosssummonitem")
    );

    public ExampleBiome() {
        super();
        // Setting the generation weight makes this biome spawn in the world
        setGenerationWeight(1);
    }

    // =========================================================================
    // In generation, it uses these next getters as base to figure out what tiles/objects to spawn
    // Since these getters are used very often during generation, we want it to be very optimized
    // Because of this, we have stored the ID of the tile we want to use in the registry

    @Override
    public int getGenerationTerrainTileID() {
        return ExampleModTiles.EXAMPLE_GRASS_TILE_ID;
    }

    @Override
    public int getGenerationCaveTileID() {
        return ExampleModTiles.EXAMPLE_TILE_ID;
    }

    @Override
    public int getGenerationCaveRockObjectID() {
        return ExampleModObjects.EXAMPLE_BASE_ROCK_ID;
    }

    @Override
    public int getGenerationDeepCaveTileID() {
        // If we ever add a separate deep version, change it here
        return ExampleModTiles.EXAMPLE_TILE_ID;
    }

    @Override
    public int getGenerationDeepCaveRockObjectID() {
        // If we ever add a separate deep version, change it here
        return ExampleModObjects.EXAMPLE_BASE_ROCK_ID;
    }

    // =========================================================================
    // The way generator stacks works, is that we first set up the branches/veins in the initialize method
    // We then later use these branches in the generate methods below

    @Override
    public void initializeGeneratorStack(BiomeGeneratorStack stack) {
        super.initializeGeneratorStack(stack);

        // Trees on the surface
        stack.addRandomSimplexVeinsBranch("exampleTrees", 2f, 0.2f, 1f, 0);

        // Ore veins underground
        stack.addRandomVeinsBranch("exampleCaveOre", 0.6f, 3, 6, 0.4f, 2, false);
        stack.addRandomVeinsBranch("exampleDeepCaveOre", 0.6f, 3, 6, 0.4f, 2, false);
    }

    @Override
    public void generateRegionSurfaceTerrain(Region region, BiomeGeneratorStack stack, GameRandom random) {
        super.generateRegionSurfaceTerrain(region, stack, random);

        // On the surface, we use our exampleTrees vein we initialized above
        // The stack has a factory-style place system like seen below
        // This can also be used to place tiles, mobs, etc.
        // Or you can use the customPlace(..) in the end to iterate through the valid tiles

        int grassTile = getGenerationTerrainTileID();

        stack.startPlaceOnVein(this, region, random, "exampleTrees")
                .onlyOnTile(grassTile)
                .chance(0.1f) // 10% chance for each valid spot
                .placeObject("exampletree");

        stack.startPlace(this, region, random)
                .chance(0.4f) // 40% chance for each valid spot
                .onlyOnTile(grassTile)
                .placeObject("examplegrass");
    }

    @Override
    public void generateRegionCaveTerrain(Region region, BiomeGeneratorStack stack, GameRandom random) {
        super.generateRegionCaveTerrain(region, stack, random);

        // In the cave, we use our exampleCaveOre vein we initialized above
        stack.startPlaceOnVein(this, region, random, "exampleCaveOre")
                .onlyOnObject(getGenerationCaveRockObjectID())
                .placeObjectForced("exampleorerock");

        // If you want crates / small rocks etc, add them here.

        // If you place stuff relating to liquid, like only on shores, only certain distance from
        // the shore, etc. You can call this update to actually calculate that data before placing:
        // region.updateLiquidManager();
    }

    @Override
    public void generateRegionDeepCaveTerrain(Region region, BiomeGeneratorStack stack, GameRandom random) {
        super.generateRegionDeepCaveTerrain(region, stack, random);

        // In the deep cave, we use our exampleDeepCaveOre vein we initialized above
        stack.startPlaceOnVein(this, region, random, "exampleDeepCaveOre")
                .onlyOnObject(getGenerationDeepCaveRockObjectID())
                .placeObjectForced("exampleorerock");

    }

    @Override
    public Color getDebugBiomeColor() {
        // Debug color is only used for debug tools. Specifically in the F10 menu -> Dev tools -> One World tests
        return new Color(128, 0, 128);
    }

    @Override
    public AbstractMusicList getLevelMusic(Level level, PlayerMob perspective) {
        // This biome only plays Forest Path. Even in caves, etc.
        // Here you can do checks if level is a cave, etc. Like: level.isCave / level.isDeepCaveLevel()
        return new MusicList(MusicRegistry.ForestPath);
    }

    @Override
    public LootTable getExtraBiomeMobDrops(LevelIdentifier levelIdentifier) {
        // This is currently only used for showing in the journal
        if (levelIdentifier.isCave()) {
            return randomExampleBossSummonDrop;
        }
        return new LootTable();
    }

    // Add Example Boss Summon Item
    @Override
    public LootTable getExtraMobDrops(Mob mob) {
        LevelIdentifier levelIdentifier = mob.getLevel().getIdentifier();
        // When in regular cave, hostile mobs that are not summoned have a random
        // chance to drop the boss summon item
        if (levelIdentifier.isCave() && mob.isHostile && !mob.isSummoned) {
            return randomExampleBossSummonDrop;
        }
        return super.getExtraMobDrops(mob);
    }

    @Override
    public MobSpawnTable getMobSpawnTable(Level level) {
        // We use the same spawn table for all levels in this biome.
        // Here you can do checks if level is a cave, etc. Like: level.isCave / level.isDeepCaveLevel()
        return mobSpawnTable;
    }

    // =========================================================================
    // Structures / presets

    public RandomCaveChestRoom getNewCaveChestRoomPreset(GameRandom random, AtomicInteger lootRotation) {
        // Here we generate a chest room based on our example loot table and chest room set
        RandomCaveChestRoom preset = new RandomCaveChestRoom(
                random,
                ExampleLootTable.exampleLootTable,
                lootRotation,
                ExampleModObjects.EXAMPLE_CHEST_ROOM_SET
        );
        // Because of a bug in the base game, we have to replace the floor manually
        preset.replaceTile(TileRegistry.stoneFloorID, ExampleModObjects.EXAMPLE_CHEST_ROOM_SET.floor);
        return preset;
    }

    public RandomCaveChestRoom getNewDeepCaveChestRoomPreset(GameRandom random, AtomicInteger unique) {
        // This example biome does not spawn deep cave chest rooms
        return null;
    }

    public CaveRuins getNewCaveRuinsPreset(GameRandom random, AtomicInteger unique) {
        // This example biome does not spawn cave ruins
        return null;
    }

    public CaveRuins getNewDeepCaveRuinsPreset(GameRandom random, AtomicInteger unique) {
        // This example biome does not spawn cave ruins
        return null;
    }

}
