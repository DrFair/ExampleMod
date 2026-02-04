package examplemod.Loaders;

import examplemod.ExampleMod;
import examplemod.examples.mobs.ExampleBossMob;
import examplemod.examples.mobs.ExampleMob;
import necesse.engine.sound.SoundSettings;
import necesse.engine.sound.gameSound.GameSound;
import necesse.gfx.gameTexture.GameTexture;

public class ExampleModResources {
    public static void load(){
        // Sometimes your textures will have a black or other outline unintended under rotation or scaling
        // This is caused by alpha blending between transparent pixels and the edge
        // To fix this, run the preAntialiasTextures gradle task
        // It will process your textures and save them again with a fixed alpha edge color

        ExampleMob.texture = GameTexture.fromFile("mobs/examplemob");
        ExampleBossMob.texture = GameTexture.fromFile("mobs/examplebossmob");

        //initialising the sound to be used by our boss mob
        ExampleMod.EXAMPLESOUND = GameSound.fromFile("examplesound");

        // Optional settings (volume/pitch/falloff) – used when playing via SoundSettings
        ExampleMod.EXAMPLESOUNDSETTINGS = new SoundSettings(ExampleMod.EXAMPLESOUND)
                .volume(0.8f)
                .basePitch(1.0f)
                .pitchVariance(0.08f)
                .fallOffDistance(900);
    }
}

