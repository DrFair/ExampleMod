package examplemod.examples;

import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.ServerClient;

public class ExamplePacket extends Packet {

    public final int playerSlot;
    public final int someInteger;
    public final boolean someBoolean;
    public final String someString;
    public final Packet someContent;

    // MUST HAVE - Used for construction in registry
    public ExamplePacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        // Important that it's same order as written in
        playerSlot = reader.getNextByteUnsigned(); // Since player slots never go over 255
        someInteger = reader.getNextInt();
        someBoolean = reader.getNextBoolean();
        someString = reader.getNextString();
        someContent = reader.getNextContentPacket();
    }

    public ExamplePacket(ServerClient client, int someInteger, boolean someBoolean, String someString, Packet someContent) {
        this.playerSlot = client.slot;
        this.someInteger = someInteger;
        this.someBoolean = someBoolean;
        this.someString = someString;
        this.someContent = someContent;

        PacketWriter writer = new PacketWriter(this);
        // Important that it's same order as read in
        writer.putNextByteUnsigned(playerSlot);
        writer.putNextInt(someInteger);
        writer.putNextBoolean(someBoolean);
        writer.putNextString(someString);
        writer.putNextContentPacket(someContent);

        // Examples how to send packets:
//        client.sendPacket(this); // To a single client
//        server.network.sendToAllClients(packet); // To all clients
    }

    @Override
    public void processClient(NetworkPacket packet, Client client) {
        // Do some stuff with the packet
    }
}



