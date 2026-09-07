package examplemod.loaders;

import examplemod.examples.packets.ExamplePacket;
import necesse.engine.registries.PacketRegistry;

public class ExampleModPackets {

    public static void load() {
        // Register our packets. In this case we only have one
        PacketRegistry.registerPacket(ExamplePacket.class);
    }

}
