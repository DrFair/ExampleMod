package examplemod.Loaders;

import examplemod.examples.ExampleChatCommand;
import necesse.engine.commands.CommandsManager;

public class ExampleModCommands {
    public static void load(){

        // Register our server chat command
        CommandsManager.registerServerCommand(new ExampleChatCommand());
    }
}
