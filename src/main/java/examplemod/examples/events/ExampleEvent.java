package examplemod.examples.events;

import necesse.engine.events.GameEvent;
import necesse.level.maps.Level;

/**
 * A simple custom "game event" for our mod.
 *
 * This is NOT a LevelEvent (it does not exist in the world, does not tick, and is not drawn).
 * Instead, it's a lightweight message object you pass through your own event system
 * (for example: GameEvents.triggerEvent(...)) so other parts of your mod can react.
 *
 * Think of it like: "something happened" + "here is the data about what happened".
 */
public class ExampleEvent extends GameEvent {

    /**
     * The level where the thing happened
     */
    public final Level level;

    /**
     * Which connected player this event is about.
     */
    public final int clientSlot;

    /**
     * Example payload data listeners can use.
     */
    public final String message;

    public ExampleEvent(Level level, int clientSlot) {
        this.level = level;
        this.clientSlot = clientSlot;

        // Simple demo message that listeners can read.
        this.message = "PING: this message was sent from the ExampleEvent";
    }
}
