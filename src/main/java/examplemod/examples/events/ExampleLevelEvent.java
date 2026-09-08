package examplemod.examples.events;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.ParticleTypeSwitcher;
import necesse.entity.levelEvent.LevelEvent;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.Particle;

import java.awt.*;

/**
 * A LevelEvent is an event happening on a specific level
 * They're quite versatile. A lot of level events are temporary and never saved. But they can be saved to the level
 * Some are only used to on either the server or client to run some logic over time, while others are
 * synced between the server and clients
 * <br>
 *
 * A few things to note about syncing:
 * If an event is over a significant amount of time, and you want newer clients entering the area to
 * get the event, make sure to override isNetworkImportant() and return true. This will also make it so
 * that if you call over() on the server, a packet will be sent to the client, disposing the event as well
 * <br>
 *
 * This level event specifically is used for:
 * - Server: Send a chat message to the target player and give a speed burst buff
 * - Client: Show a burst of particles at the target tile position
 */
public class ExampleLevelEvent extends LevelEvent {

    // The tile position of the event
    protected int tileX, tileY;

    // The client we're targeting with this event
    protected NetworkClient targetClient;

    // Simple lifetime for the client effect (in ticks)
    // This is not synced in the spawn packet, since the event is so short
    protected int ticksLeft = TickManager.ticksPerSec / 2;

    // When spawning particles, there are 3 different particle "types" you can use
    // Depending on importance, they will define if the particle will actually show or not depending on
    // the clients graphics settings
    // The options are:
    // - Cosmetic: Will only show if particle setting is set to "maximum", and there are less than
    // maximum particles in the area already
    // - Important cosmetic: Same as cosmetic, but will also show with "decreased" particle setting
    // - Critical: Will always show, even on "minimal" particle settings
    // Particles will in general not spawn, if spawned outside of the screen area. But it is possible to
    // override this by giving it a "null" type. It's the same as critical, but ignore screen restrictions as well

    // Here we define which types we will use later on. It will iterate through the types for each particle and
    // wrap around to the first one when complete
    protected ParticleTypeSwitcher particleTypeSwitcher = new ParticleTypeSwitcher(
            Particle.GType.COSMETIC,
            Particle.GType.IMPORTANT_COSMETIC,
            Particle.GType.CRITICAL
    );

    // Required empty constructor for registry/network spawning
    public ExampleLevelEvent() {
    }

    public ExampleLevelEvent(ServerClient targetClient, int tileX, int tileY) {
        this.targetClient = targetClient;
        this.tileX = tileX;
        this.tileY = tileY;
    }

    @Override
    public void setupSpawnPacket(PacketWriter writer) {
        super.setupSpawnPacket(writer);
        // setupSpawnPacket(...) is called on the server before it sends a packet with this LevelEvent to clients
        // Anything you want the client-side version of this event to know, must be written here
        // The client will read these values in applySpawnPacket(...) in the exact same order they are written

        // The tile positions of this event
        writer.putNextInt(tileX);
        writer.putNextInt(tileY);

        // Since the server supports a maximum of 250 slots, we use unsigned byte to write the slot
        writer.putNextByteUnsigned(targetClient.slot);
    }

    @Override
    public void applySpawnPacket(PacketReader reader) {
        super.applySpawnPacket(reader);
        // applySpawnPacket(...) is called on the client when it receives the spawn packet for this LevelEvent
        // Make sure the values you read are in the exact same order as you set them up in
        // setupSpawnPacket(...), otherwise you'll desync fields and get confusing bugs

        // Read the tile positions
        tileX = reader.getNextInt();
        tileY = reader.getNextInt();

        // Read target client slot and assign the target client
        int targetSlot = reader.getNextByteUnsigned();
        if (isClient()) {
            targetClient = getClient().getClient(targetSlot);
        } else if (isServer()) {
            // It is possible for the client to send a level event to the server, in
            // very specific and rare cases. Mostly in debugging cases
            targetClient = getServer().getClient(targetSlot);
        }
    }

    @Override
    public void init() {
        super.init();
        // init() happens just after the event has been added to a level

        // If we have no target client, don't run this event
        if (targetClient == null) {
            over();
            return;
        }

        // Server side: Just send the message and give the buff
        if (isServer()) {
            targetClient.getServerClient().sendChatMessage("This message was sent from the ExampleLevelEvent");

            // 5 seconds movement speed burst buff
            ActiveBuff activeBuff = new ActiveBuff(BuffRegistry.MOVE_SPEED_BURST, targetClient.playerMob, 5f, null);

            // We make sure to send the buff to other clients, since this is only ran on the server
            targetClient.playerMob.buffManager.addBuff(activeBuff, true);

            // End the event on the server
            over();
        }
    }

    @Override
    public void clientTick() {
        super.clientTick();
        // Runs every game tick, only on the client

        // Reduce the ticks left and call over when complete
        ticksLeft--;
        if (ticksLeft <= 0) {
            over();
            return;
        }

        Color particleColor = new Color(120, 200, 255);

        // Spawn 4 particles every game tick (20 ticks per second)
        for (int i = 0; i < 4; i++) {
            // Find the top-left level position of the tile
            int levelX = GameMath.getLevelCoordinate(tileX);
            int levelY = GameMath.getLevelCoordinate(tileY);

            // Make the position random within the tile
            levelX += GameRandom.globalRandom.nextInt(32);
            levelY += GameRandom.globalRandom.nextInt(32);

            // Add the particle to the level
            // Here we use our particle type switcher defined above. You can read more about it up there
            // A lot of things can be defined when spawning a particle. Here we use a pretty simple color,
            // size, alpha, height and lifetime
            level.entityManager
                    .addParticle(levelX, levelY, particleTypeSwitcher.next())
                    .color(particleColor)
                    .sizeFades(20, 30)
                    .heightMoves(0, 20)
                    .fadesAlphaTime(250, 150)
                    .lifeTime(400);
        }
    }

    public Point getSaveToRegionPos() {
        // Since this event is sent over the network, we need to define which regions it is part of
        // This is used to determine which clients should receive the event spawn packet
        // If the event is part of multiple regions, we can override getRegionPositions()

        // We convert the tile position to a region position using the levels region manager
        return new Point(
                level.regionManager.getRegionCoordByTile(tileX),
                level.regionManager.getRegionCoordByTile(tileY)
        );
    }

}
