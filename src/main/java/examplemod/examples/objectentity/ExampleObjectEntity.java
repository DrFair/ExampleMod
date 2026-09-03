package examplemod.examples.objectentity;

import examplemod.examples.events.ExampleLevelEvent;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

import java.awt.*;

public class ExampleObjectEntity extends ObjectEntity {

    // Tracks whether a player was on it last tick (so we only trigger once per step-on)
    protected boolean isPressed = false;

    // Small cooldown to avoid rapid re-triggers if the player jitters on the edge
    protected long nextTriggerTime = 0L;

    public ExampleObjectEntity(Level level, int tileX, int tileY) {
        // The type we define here is used to verify corruption on level loading, etc.
        // Make sure it's always the same for the same object
        super(level, "exampleeventtrigger", tileX, tileY);

        // If the cooldown is significant, it may be worth to save it using addSaveData and applyLoadData
        // But in this case there's really no need for it
        shouldSave = false;
    }

    @Override
    public void serverTick() {
        super.serverTick();
        // serverTick runs on the server and main menu at 20 ticks per second (TickManager.ticksPerSec)

        // Get the level
        Level level = getLevel();

        // Get the current time (used later for cooldown management)
        long currentTime = level.getTime();

        // The hitbox covering the full tile under this object
        // Level positions are different from tile positions
        // Each tile is 32x32 in size, so here we convert from tile to level position
        Rectangle hitbox = new Rectangle(
                tileX * 32,
                tileY * 32,
                32,
                32
        );

        // Here we iterate through all the regions which the hitbox overlaps with
        // We add 1 extra region range to avoid edge cases of players being on the edge of regions, etc.
        // We then check if any of the players collision intersects with the hitbox
        PlayerMob target = level.entityManager.players.streamInRegionsShape(hitbox, 1)
                .filter(player -> player.getCollision().intersects(hitbox))
                .findFirst()
                .orElse(null);

        // If a target was found, and it is not currently pressed or on cooldown
        if (target != null && !isPressed && currentTime >= nextTriggerTime) {
            isPressed = true;
            nextTriggerTime = currentTime + 300; // 300 milliseconds cooldown

            /*
             * This is an example of triggering a level event (in this case ExampleLevelEvent)
             * Using events.add(...) will add it to the servers level and send it over to other clients
             * Using events.addHidden(...) will just add it to the servers level without sending it
             */
            level.entityManager.events.add(new ExampleLevelEvent(target.getServerClient(), tileX, tileY));
        }
        // Reset when nobody is standing on it
        if (target == null) {
            isPressed = false;
        }
    }

}

