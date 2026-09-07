package examplemod.examples.presets;

import examplemod.loaders.ExampleModBiomes;
import necesse.engine.gameLoop.tickManager.PerformanceTimerManager;
import necesse.engine.util.GameRandom;
import necesse.engine.world.biomeGenerator.BiomeGeneratorStack;
import necesse.engine.world.worldPresets.LevelPresetsRegion;
import necesse.engine.world.worldPresets.WorldPreset;
import necesse.level.maps.Level;
import necesse.level.maps.presets.Preset;
import necesse.level.maps.presets.PresetUtils;

import java.awt.*;

public class ExampleWorldPreset extends WorldPreset {

    // Here we define the area that our preset will cover. This is later used to do area checks, etc.
    protected Dimension size = new Dimension(11, 11);

    /*
        The world is divided up into regions called WorldPresetsRegion, in which it stores
        a LevelPresetsRegion for each "layer" (surface/cave) of the world.
        Each WorldPresetRegion are 1024x1024 tiles big, and this is where the presets that are supposed
        to be placed in that region is calculated.
        You cannot place presets that overlap with 2 different world preset regions.

        Since calculating what presets should be placed can be expensive, it's important to consider each of
        our steps and how important they are for our preset to generate.
     */

    @Override
    public boolean shouldAddToRegion(LevelPresetsRegion levelPresetsRegion) {
        // This is the first step that is taken to consider a world preset.
        // In here, we return true if we want our preset to be considered for a specific LevelPresetsRegion.
        // In this case, we only want it to be considered if the level is the surface,
        // and has any of our example biome in it:
        return levelPresetsRegion.identifier.isSurface()
                && levelPresetsRegion.hasAnyOfBiome(ExampleModBiomes.EXAMPLE_BIOME.getID());
    }

    @Override
    public void addToRegion(GameRandom gameRandom, LevelPresetsRegion levelPresetsRegion, BiomeGeneratorStack biomeGeneratorStack, PerformanceTimerManager performanceTimerManager) {
        // This is the second step. Which is only called if shouldAddToRegion returned true.
        // In here, we essentially adds our future generation to the LevelPresetsRegion:
        // We calculate how many and where it should be placed, register and occupy the space inside the
        // LevelPresetsRegion and tell it what should happen when it's time to generate it.

        // There are some debug tools in the game you can use here. To see which presets are being generated:
        // F10 menu -> Dev tools -> Show presets region bounds
        // It will toggle showing the bounds of presets that are being generated on your minimap/big map.
        // It will also open a search box at the top-left, which you can use to filter for specific presets
        // (Changing level will reset this search box, so you have to toggle again to open it)

        // First, we calculate how many presets we want to place total
        // This helper method allows us to normalize based on how much of the biome is present in the LevelPresetsRegion
        // The "pointsPerRegion" defines how many presets we want to place per region. A region covers 16x16 tiles,
        // so this means on average every 100 region (160x160 tiles), we want to place our preset.
        int total = getTotalBiomePoints(gameRandom, levelPresetsRegion, ExampleModBiomes.EXAMPLE_BIOME, 0.01f);

        // We then try to place that many
        for (int i = 0; i < total; i++) {
            // Now we set up the things we want to pass into the helper method "findRandomBiomePresetTile"
            // LevelPresetsRegion keeps track of different "boards", of occupied tiles. This is useful if we
            // want our preset to be able to override mini biomes, but not villages, etc.
            // Some of the boards that the base game uses are:
            // "villages" - NPC villages, pirate villages, etc.
            // Addition to that is "villagespadding", which is a bit of extra space around villages
            // "minibiomes" - Things like spider nests, charred forest, vampire crypts, etc.
            // "loot" - Things like chest rooms, ruins with loot, etc.
            // In this case, we don't want our preset to override these boards:
            String[] boardsToCheck = { "villages", "minibiomes", "loot" };

            // When finding an area that we want to check, we do a validity check of that area.
            // This is where it can become very expensive if we do a lot of checks in here.
            // isValidPosition(tileX, tileY) is ran for each tile that it wants to check.
            ValidTilePredicate validCheck = new ValidTilePredicate() {
                @Override
                public boolean isValidPosition(int tileX, int tileY) {
                    // In here, there are several different helper methods we can use.
                    // In our example we want to make sure that our preset doesn't spawn
                    // in the ocean, on rivers or on beaches.
                    // Since our preset is relatively small, we can do a simple corner check (4 tile checks total).

                    // We can't check for specific tiles, because the world is not generated yet.
                    // But BiomeGeneratorStack has a bunch of different things we can use to check for validity.

                    // If our preset was bigger, we could use runGridCheck(..), which allows us to pass a
                    // resolution in tiles that we want to cover. For example, passing 10 as the resolution,
                    // it will do checks every 10 tiles within the size that we pass.

                    // We pass the size in as well for the corner checks
                    return runCornerCheck(tileX, tileY, size.width, size.height, new ValidTilePredicate() {
                        @Override
                        public boolean isValidPosition(int tileX, int tileY) {
                            // We make sure that the surface is not an ocean, river or beach on the tile we check
                            return !biomeGeneratorStack.isSurfaceOceanOrRiverOrBeach(tileX, tileY);
                        }
                    });
                }
            };

            // We want to do a total of 100 random attempts to find a valid tile.
            // Balacing the amount of attempts and how strict we are in our valid checks is key to
            // keeping a stable framerate while moving through the world
            int totalAttempts = 100;

            // We then call the helper method to find a random tile that is valid for our preset to be placed on.
            Point tile = findRandomBiomePresetTile(gameRandom, levelPresetsRegion, biomeGeneratorStack, ExampleModBiomes.EXAMPLE_BIOME, totalAttempts, size, boardsToCheck, validCheck);

            // We check if we found a valid tile
            if (tile != null) {
                // If you're generating large mini biomes, there are more efficient ways of doing this.
                // Like generating preset/biome on a per-region basis. You can check out FishianMiniBiomeWorldPreset
                // and how it uses RegionTileWorldPresetGenerator to do this. But that is out of the
                // scope of this example mod

                // Now we add the preset we want to place, and we choose to occupy the "loot" board
                // with our preset as well. Our place function is what handles the placing of the preset when
                // the region we cover is being generated
                levelPresetsRegion.addPreset(this, tile.x, tile.y, size, "loot", new LevelPresetsRegion.WorldPresetPlaceFunction() {
                    @Override
                    public void place(GameRandom random, Level level, PerformanceTimerManager timer) {
                        // Ensure that all regions within our bounds are generated
                        ensureRegionsAreGenerated(level, tile.x, tile.y, size.width, size.height);

                        // We construct our ExamplePreset and pass the seeded random to determine loot
                        Preset preset = new ExamplePreset(random);

                        // If we want to randomize the rotation/mirror, we can use a helper method here:
                        preset = PresetUtils.randomizeRotationAndMirror(preset, random);

                        // Finally we apply our preset
                        preset.applyToLevel(level, tile.x, tile.y);
                    }
                });
            }
        }
    }

}
