package examplemod.examples.items.materials;

import necesse.inventory.item.matItem.MatItem;

public class ExampleLogItem extends MatItem {

    public ExampleLogItem() {
        super(
                500, // Max stack size
                Rarity.UNCOMMON, // Rarity
                "anylog" // Global ingredient stringIDs
        );

        // Adjust the item category to logs
        setItemCategory("materials", "logs");
    }
}
