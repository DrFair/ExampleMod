package examplemod.examples;

import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.commands.parameterHandlers.ItemParameterHandler;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ItemRegistry;
import necesse.inventory.item.Item;

public class ExampleChatCommand extends ModularChatCommand {

    public ExampleChatCommand() {
        super("example", "An example chat command", PermissionLevel.ADMIN, false,
                // Parameter handlers are used for autocomplete, and will parse the input into their type
                new CmdParameter("item", new ItemParameterHandler(), true),
                new CmdParameter("string", new PresetStringParameterHandler("option1", "otheroption"), false));
    }

    // Args will correspond with the parameters parsed objects
    // client will be null when ran from server, serverClient won't be null if ran from a server client
    @Override
    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
        // Parse the arguments, their index will be the same that they are passed to in constructor
        Item item = (Item) args[0]; // Optional parameters are still parsed
        String string = (String) args[1];

        // Do stuff with the parsed args
        if (item == null) {
            commandLog.add("No item given");
        } else {
            commandLog.add("Item: " + ItemRegistry.getDisplayName(item.getID()));
        }
        commandLog.add("String: " + string);
    }
}
