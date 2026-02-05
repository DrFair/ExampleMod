package examplemod.examples.events;

import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.server.ServerClient;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.GameDamage;
import necesse.entity.particle.Particle;

import java.awt.Color;

/**
 * LevelEvent
 *  Server: sends a chat message to a specific player.
 *  Client: spawns a quick burst of particles at a world position.
 *  events.addHidden(ev)  server only
 *  events.add(ev)        server + clients
 */
public class ExampleLevelEvent extends LevelEvent {

    // Which ServerClient (player) to message. -1 = invalid/unset.
    private int targetSlot = -1;

    // Message to send.
    private String message = "";

    // World position (pixels) where the particle effect should appear.
    private int worldX;
    private int worldY;

    // Simple lifetime for the client effect (in ticks).
    private int ticksLeft = 20;

    // Small guard so the server only sends the message once.
    private boolean sentServerMessage = false;

    // Required empty constructor for registry/network spawning
    public ExampleLevelEvent() {
    }

    /**
     * targetSlot player to message
     * worldX     particle position X in pixels (tileX * 32 + 16 is tile center)
     * worldY     particle position Y in pixels
     */
    public ExampleLevelEvent(int targetSlot, int worldX, int worldY) {
        this.targetSlot = targetSlot;
        this.worldX = worldX;
        this.worldY = worldY;
        this.message = "ZING: this message was sent from the ExampleLevelEvent";
    }

    @Override
    public void init() {
        super.init();

        // Server side: send the message once.
        if (isServer() && !sentServerMessage) {
            sentServerMessage = true;

            //target the specific client that triggered the event and make sure it's not returned null
            ServerClient client = level.getServer().getClient(targetSlot);
            if (client != null) {

                GameDamage dmg = new GameDamage(30F);

                //Apply damage
                int healthBefore = client.playerMob.getHealth();
                client.playerMob.isServerHit(dmg, worldX, worldY, 0.0F, null);
                int healthAfter = client.playerMob.getHealth();

                //How much damage was actually taken
                int damageTaken = healthBefore - healthAfter;

                if (damageTaken > 0) {
                    //Restore that amount clamped to current max health, and compute the healing applied because this is a demo, and we aren't mean xD
                    int finalHealth = Math.min(client.playerMob.getMaxHealth(), healthAfter + damageTaken);
                    int healApplied = finalHealth - healthAfter;

                    //send a health change event to apply
                    level.entityManager.events.add(new MobHealthChangeEvent(client.playerMob, finalHealth, healApplied));
                }

                client.sendChatMessage(message);
            }

            // Do NOT call over() here if you want clients to see the event.
            // Let it exist long enough for the spawn packet to be processed.
        }
    }

    @Override
    public void serverTick() {
        super.serverTick();

        // If this event was added as hidden, it will never go to clients,
        // so we can end it right away after doing server work.
        if (sentServerMessage) {
            over();
        }
    }

    @Override
    public void clientTick() {
        super.clientTick();

        // Quick particle burst for a short time, then end.
        if (ticksLeft-- <= 0) {
            over();
            return;
        }

        Color c = new Color(120, 200, 255);

        for (int i = 0; i < 6; i++) {
            float ox = (float) (Math.random() * 24.0 - 12.0); // -12..12 px
            float oy = (float) (Math.random() * 24.0 - 12.0);

            level.entityManager
                    .addParticle(worldX + ox, worldY + oy, Particle.GType.COSMETIC)
                    .color(c)
                    .sizeFades(30, 60)
                    .fadesAlphaTime(250, 150)
                    .lifeTime(350);
        }
    }

    @Override
    public void setupSpawnPacket(PacketWriter writer) {
        super.setupSpawnPacket(writer);

        /*
         * setupSpawnPacket(...) is called when the server spawns this LevelEvent to clients
         * (when you use level.entityManager.events.add/level.entityManager.events.addHidden)
         *
         * Anything you want the client-side version of this event to know must be written here.
         * The client will read these values in applySpawnPacket(...) in the exact same order.
         */

        // Who the event should target (which player/server client slot)
        writer.putNextInt(targetSlot);

        // Any text payload we want associated with the event
        writer.putNextString(message);

        // World position for effects (these should be pixel coords, not tile coords)
        writer.putNextInt(worldX);
        writer.putNextInt(worldY);

        // How long the client-side effect should run
        writer.putNextInt(ticksLeft);
    }

    @Override
    public void applySpawnPacket(PacketReader reader) {
        super.applySpawnPacket(reader);

        /*
         * applySpawnPacket(...) is called on the client when it receives the spawn packet
         * for this LevelEvent.
         *
         * Make sure you read values back in the same order you wrote them in setupSpawnPacket(...),
         * otherwise you'll desync fields and get confusing bugs.
         */

        // Read target player slot + message payload
        targetSlot = reader.getNextInt();
        message = reader.getNextString();

        // Read effect position + lifetime
        worldX = reader.getNextInt();
        worldY = reader.getNextInt();
        ticksLeft = reader.getNextInt();
    }
}
