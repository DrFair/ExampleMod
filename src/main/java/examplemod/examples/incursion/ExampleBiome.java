package examplemod.examples.incursion;

import necesse.engine.AbstractMusicList;
import necesse.engine.MusicList;
import necesse.engine.registries.MusicRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.level.maps.Level;
import necesse.level.maps.biomes.Biome;
import necesse.level.maps.biomes.MobSpawnTable;

// A minimalist biome used solely for the ExampleIncursion
// the Example Mob is used here as the enemy spawn
public class ExampleBiome extends Biome {

    public static  MobSpawnTable critters = new MobSpawnTable()
            .include(Biome.defaultCaveCritters);

    public static  MobSpawnTable mobs = new MobSpawnTable()
            .add(100,"examplemob");

    @Override
    public AbstractMusicList getLevelMusic(Level level, PlayerMob perspective) {
        return new MusicList(MusicRegistry.ForestPath);
    }

    @Override
    public MobSpawnTable getCritterSpawnTable(Level level) {
        return critters;
    }

    @Override
    public MobSpawnTable getMobSpawnTable(Level level) {
        return mobs;
    }

}