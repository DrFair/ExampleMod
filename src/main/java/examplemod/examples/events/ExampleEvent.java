package examplemod.examples.events;

import necesse.engine.events.GameEvent;
import necesse.level.maps.Level;

/*
 * ExampleEvent is a small "notification" object for our mod.
 *
 * Compared to a LevelEvent
 *   it does not exist in the world
 *   it does not tick
 *   it does not draw anything
 *
 * Instead, we create one and pass it through GameEvents.triggerEvent(...)
 * so any registered listeners can react.
 */
public class ExampleEvent extends GameEvent {

    // The level where the event happened
    public final Level level;

    // The slot id of the player this event relates to
    public final int clientSlot;

    // Simple data payload for the demo
    public final String message;

    public ExampleEvent(Level level, int clientSlot) {
        this.level = level;
        this.clientSlot = clientSlot;

        // For a real mod you would usually pass the message into the constructor.
        this.message = "PING: this message was sent from the ExampleEvent";
    }
}
