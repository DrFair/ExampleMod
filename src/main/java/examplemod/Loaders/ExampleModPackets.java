package examplemod.Loaders;

import examplemod.examples.packets.ExamplePacket;
import examplemod.examples.packets.ExamplePlaySoundPacket;
import necesse.engine.registries.PacketRegistry;

public class ExampleModPackets {

    public static void load() {
        // Register our packets
        PacketRegistry.registerPacket(ExamplePacket.class);

        PacketRegistry.registerPacket(ExamplePlaySoundPacket.class);
    }
}
