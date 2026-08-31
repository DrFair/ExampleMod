package examplemod.examples.mobs;

import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.friendly.human.humanShop.HumanShop;
import necesse.inventory.InventoryItem;

import java.util.Collections;
import java.util.List;

public class ExampleHumanMob extends HumanShop {

    // MUST HAVE an empty constructor
    public ExampleHumanMob() {
        super(
                500, // Max health when not part of a player settlement
                200, // Max health when part of a player settlement
                "examplesettler" // The settler stringID registered in ExampleModSettlers
        );

        // Unlock the job type for THIS settler only
        this.jobTypeHandler.getPriority("examplejobtype").disabledBySettler = false;
    }

    // Cost to recruit this as a settler
    @Override
    public List<InventoryItem> getRecruitItems(ServerClient client) {
        // If you return null, it means you cannot recruit them

        // If trapped, it's free to recruit (returns empty list)
        if (isTrapped()) return Collections.emptyList();

        // Simple recruit cost (you can make this random like vanilla does)
        return Collections.singletonList(new InventoryItem("exampleitem", 10));
    }

}