package examplemod.Loaders;

import examplemod.examples.events.ExampleEvent;
import examplemod.examples.events.ExampleLevelEvent;
import necesse.engine.GameEventListener;
import necesse.engine.GameEvents;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.LevelEventRegistry;

public class ExampleModEvents {
    public static void load() {
        // Register our Level Event to the registry
        LevelEventRegistry.registerEvent("examplelevelevent", ExampleLevelEvent.class);

        // Register our ExampleEvent Listener
        GameEvents.addListener(ExampleEvent.class, new GameEventListener<ExampleEvent>() {
            @Override
            public void onEvent(ExampleEvent event) {
                if (event.level == null || !event.level.isServer()) return;

                ServerClient client = event.level.getServer().getClient(event.clientSlot);
                if (client != null) {
                    client.sendChatMessage(event.message);
                    client.sendChatMessage("PONG: this message was sent from the ExampleEvent Listener ");
                }
            }
        });

    }
}



