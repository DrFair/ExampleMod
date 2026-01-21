package examplemod.examples.packets;

import examplemod.ExampleMod;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.sound.SoundEffect;

/**
 *  SERVER -> CLIENT packet:
 * Tells the receiving client to play the example sound at a specific world position (x, y).
 */

public class ExamplePlaySoundPacket extends Packet {
    public final float x;
    public final float y;

    // Decode (CLIENT receiving)
    public ExamplePlaySoundPacket(byte[] data) {
        super(data);
        PacketReader r = new PacketReader(this);
        x = r.getNextFloat();
        y = r.getNextFloat();
    }

    // Encode (SERVER sending)
    public ExamplePlaySoundPacket(float x, float y) {
        this.x = x;
        this.y = y;

        PacketWriter w = new PacketWriter(this);
        w.putNextFloat(x);
        w.putNextFloat(y);
    }

    // Runs ONLY on client
    @Override
    public void processClient(NetworkPacket packet, Client client) {
        if (ExampleMod.EXAMPLESOUNDSETTINGS != null) {
            ExampleMod.EXAMPLESOUNDSETTINGS.play(SoundEffect.effect(x, y));
        }
    }
}
