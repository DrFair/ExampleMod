package examplemod.examples;

import examplemod.examples.events.ExampleEvent;
import examplemod.examples.events.ExampleLevelEvent;
import necesse.engine.GameEvents;
import necesse.engine.network.server.ServerClient;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

import java.awt.*;

public class ExampleObjectEntity extends ObjectEntity {
    // Tracks whether a player was on it last tick (so we only trigger once per step-on)
    private boolean pressed = false;

    // Small cooldown to avoid rapid re-triggers if the player jitters on the edge
    private long nextTriggerTime = 0L;


    public ExampleObjectEntity(Level level, int tileX, int tileY) {
        // Create an ObjectEntity instance for the object placed at (tileX, tileY) on this level.
        // The string is this ObjectEntity's type/id used by the engine for identification (save/load/sync).
        super(level, "examplelevelevent", tileX, tileY);

        // no need to save this is only a demo
        this.shouldSave = false;
    }

    private Rectangle getWorldTileRect() {
        // Full tile area in world pixels
        return new Rectangle(tileX * 32, tileY * 32, 32, 32);
    }

    @Override
    public void serverTick() {
        super.serverTick();

        // Get the level
        Level level = getLevel();

        // Get the current time
        long now = level.getTime();
        Rectangle tileRect = getWorldTileRect();

        boolean hasPlayerOnTile = false;


        // Check all connected server clients
        for (ServerClient client : level.getServer().getClients()) {
            if (client == null || client.playerMob == null) continue;

            // we want to specifically target the player rather than any mob
            if (client.playerMob.getCollision().intersects(tileRect)) {
                hasPlayerOnTile = true;
                break;
            }
        }

        // if there's a player on the tile, and it's not pressed, and it's time to check
        if (hasPlayerOnTile && !pressed && now >= nextTriggerTime) {
            pressed = true;
            nextTriggerTime = now + 300; // 300 time units cooldown (tweak as you like)

            int px = tileX * 32 + 16;
            int py = tileY * 32 + 16;

            // If your ExampleLevelEvent targets a player slot, pick the first player standing on it:
            int targetSlot = -1;
            for (ServerClient client : level.getServer().getClients()) {
                if (client != null && client.playerMob != null && client.playerMob.getCollision().intersects(tileRect)) {
                    targetSlot = client.slot;

                    break;
                }
            }

            /*
             * This is an example of triggering a level event (in this case ExampleLevelEvent).
             * Use events.add(...) when both client and server need to be sent it.
             * Use events.addHidden(...) when only the server needs to be sent it.
             */
            level.entityManager.events.add(new ExampleLevelEvent(targetSlot, px, py));

            /*
             * This is an example of triggering an event (in this case ExampleEvent)
             * which will fire the event listener for ExampleEvent to run its code
             */
            GameEvents.triggerEvent(new ExampleEvent(level, targetSlot));
        }

        // Reset when nobody is standing on it
        if (!hasPlayerOnTile) {
            pressed = false;
        }
    }
}

