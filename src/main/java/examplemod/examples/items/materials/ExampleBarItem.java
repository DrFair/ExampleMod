package examplemod.examples.items.materials;

import necesse.inventory.item.Item;
import necesse.inventory.item.matItem.MatItem;

public class ExampleBarItem extends MatItem {

    // Super simple bar material item. Nothing special about this one

    public ExampleBarItem() {
        super(
                500, // Max stack size
                Item.Rarity.UNCOMMON // Rarity
        );

    }
}
