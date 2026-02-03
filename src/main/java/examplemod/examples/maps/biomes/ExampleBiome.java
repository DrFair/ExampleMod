package examplemod.examples.maps.biomes;

import necesse.engine.AbstractMusicList;
import necesse.engine.MusicList;
import necesse.engine.registries.MusicRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.util.GameRandom;
import necesse.engine.world.biomeGenerator.BiomeGeneratorStack;
import necesse.entity.mobs.PlayerMob;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.Biome;
import necesse.level.maps.biomes.MobSpawnTable;
import necesse.level.maps.regionSystem.Region;

import java.awt.Color;

/**
 * Example overworld biome (1.1.x "infinite surface" style).
 *
 * Key idea for new modders:
 * ------------------------
 * In Necesse 1.1.x, the overworld surface is generated in *regions* by the game's SurfaceLevel.
 * SurfaceLevel does NOT create a special "ExampleBiomeLevel" per biome anymore.
 *
 * Instead, SurfaceLevel asks the Biome for:
 *  - which ground tile to paint (getGenerationTerrainTileID)
 *  - what "noise/vein" patterns to use for placing objects (initializeGeneratorStack)
 *  - what objects to place in each region (generateRegionSurfaceTerrain)
 *
 * So: if you want a biome that changes the overworld terrain + trees, these are the methods to override.
 */
public class ExampleBiome extends Biome {

    // --------------------------
    // Spawn tables
    // --------------------------
    // MobSpawnTable controls what can spawn in the biome.
    // Vanilla provides some defaults; we "include" them and then add our own entries.

    /** Small critters (e.g. butterflies, squirrels) for surface regions of this biome. */
    public static final MobSpawnTable surfaceCritters = new MobSpawnTable()
            .include(Biome.defaultSurfaceCritters);

    /** Critters for cave levels (if the player is in caves). */
    public static final MobSpawnTable caveCritters = new MobSpawnTable()
            .include(Biome.defaultCaveCritters);

    /** Hostile/neutral mobs for the surface. We add our custom mob "examplemob". */
    public static final MobSpawnTable surfaceMobs = new MobSpawnTable()
            .include(Biome.defaultSurfaceMobs)
            .add(30, "examplemob"); // weight: higher = more likely relative to other entries

    /** Hostile/neutral mobs for caves. Usually a different balance than the surface. */
    public static final MobSpawnTable caveMobs = new MobSpawnTable()
            .include(Biome.defaultCaveMobs)
            .add(100, "examplemob");

    public ExampleBiome() {
        super();

        // Generation weight decides how often this biome is chosen when the world is generating new regions.
        // Vanilla values are typically around ~0.5 to ~1.5. Keep it in that range while testing.
        // (Huge numbers can cause problems with spawn finding and biome distribution.)
        this.setGenerationWeight(1.0F);
    }

    // --------------------------
    // Overworld world generation hooks (SurfaceLevel -> Biome)
    // --------------------------

    /**
     * This is the *base ground tile* that SurfaceLevel paints for this biome in new surface regions.
     *
     * We look up our custom tile by string ID. If it isn't registered (returns -1),
     * we fall back to vanilla grass so the game doesn't break and we can still load the world.
     */
    @Override
    public int getGenerationTerrainTileID() {
        int exampleGrass = TileRegistry.getTileID("examplegrasstile");
        if (exampleGrass == -1) {
            // -1 means "not found in registry" (usually a typo or missing registration).
            return TileRegistry.grassID;
        }
        return exampleGrass;
    }

    /**
     * The BiomeGeneratorStack is a helper that stores "noise patterns" used during worldgen.
     *
     * Think of it like: "make a map of blobs/veins for trees", then when placing objects
     * we can say "place trees only on those blobs" to get natural clumps.
     *
     * This method is called as part of generator setup, not every tick.
     */
    @Override
    public void initializeGeneratorStack(BiomeGeneratorStack stack) {
        super.initializeGeneratorStack(stack);

        // Register a simplex-based "vein" pattern named "exampleTrees".
        // We'll reference this by name later when placing our trees.
        //
        // The parameters control clump size / branching / frequency.
        // If you want denser or larger clumps, we can tweak these values.
        stack.addRandomSimplexVeinsBranch("exampleTrees", 2.0F, 0.2F, 1.0F, 0);
    }

    /**
     * Called when SurfaceLevel is generating a specific *region* of the overworld surface.
     *
     * This is where you place objects like:
     *  - trees
     *  - grass tufts
     *  - flowers
     *  - rocks, etc.
     *
     * IMPORTANT: This runs during world generation for new/unexplored regions.
     * It does NOT retroactively change already-generated terrain.
     */
    @Override
    public void generateRegionSurfaceTerrain(Region region, BiomeGeneratorStack stack, GameRandom random) {
        super.generateRegionSurfaceTerrain(region, stack, random);

        // Cache our terrain tile ID so we place objects only on the correct ground.
        final int grassTile = getGenerationTerrainTileID();

        // Place our custom tree object using the "exampleTrees" vein pattern.
        // This creates forest-like clusters instead of a perfectly even distribution.
        stack.startPlaceOnVein(this, region, random, "exampleTrees")
                .onlyOnTile(grassTile)   // only place on our biome's land tile
                .chance(0.10D)           // density inside valid vein areas (tweak for more/less)
                .placeObject("exampletree");

        // Place vanilla "grass" decoration objects on top of our tile.
        // This is purely visual and helps the biome feel "alive".
        stack.startPlace(this, region, random)
                .chance(0.40D)           // overall density of grass objects
                .onlyOnTile(grassTile)
                .placeObject("grass");
    }

    /**
     * Used by some debug tools/views to show biomes as solid colors.
     * Not required for gameplay, but helpful when testing generation.
     */
    @Override
    public Color getDebugBiomeColor() {
        return new Color(128, 0, 128);
    }

    // --------------------------
    // Ambient music + spawns
    // --------------------------

    /**
     * Music selection for this biome. Here we reuse vanilla forest music.
     * You can swap to a different MusicRegistry path if you want.
     */
    @Override
    public AbstractMusicList getLevelMusic(Level level, PlayerMob perspective) {
        return new MusicList(MusicRegistry.ForestPath);
    }

    /**
     * Critter spawns depend on whether the current level is a cave level.
     */
    @Override
    public MobSpawnTable getCritterSpawnTable(Level level) {
        return level.isCave ? caveCritters : surfaceCritters;
    }

    /**
     * Mob spawns depend on whether the current level is a cave level.
     */
    @Override
    public MobSpawnTable getMobSpawnTable(Level level) {
        return level.isCave ? caveMobs : surfaceMobs;
    }
}
