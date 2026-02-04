package examplemod.examples.events;

import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.server.ServerClient;
import necesse.entity.levelEvent.LevelEvent;

/**
 * Very simple LevelEvent demo:
 *  Server-side only: sends a chat message to a specific player, then ends.
 *
 * Note: If you spawn this with events.addHidden(...), clients will never receive it,
 * so the packet methods below won't be used (they're included for when/if you use events.add(...)).
 */
public class ExampleLevelEvent extends LevelEvent {

    // Which ServerClient (player) to message. -1 = invalid/unset.
    private int targetSlot = -1;

    // Message to send (kept non-null for safety).
    private String message = "";

    // Required empty constructor for registry/network spawning
    public ExampleLevelEvent() {
    }

    public ExampleLevelEvent(int targetSlot) {
        this.targetSlot = targetSlot;
        this.message = "ZING: this message was sent from the ExampleLevelEvent";
    }

    @Override
    public void init() {
        super.init();

        // Only the server can access ServerClient and send chat packets.
        if (isServer()) {
            ServerClient client = level.getServer().getClient(targetSlot);
            if (client != null) {
                client.sendChatMessage(message);
            }

            // We're done immediately (no ticking needed).
            over();
        }
    }

    @Override
    public void setupSpawnPacket(PacketWriter writer) {
        super.setupSpawnPacket(writer);

        // Write fields in a fixed order...
        writer.putNextInt(targetSlot);
        writer.putNextString(message);
    }

    @Override
    public void applySpawnPacket(PacketReader reader) {
        super.applySpawnPacket(reader);

        // ...and read them back in the same order.
        targetSlot = reader.getNextInt();
        message = reader.getNextString();
    }
}
