package examplemod.examples.items.materials;

import necesse.inventory.item.Item;
import necesse.inventory.item.matItem.MatItem;

public class ExampleOreItem extends MatItem {

    // Super simple ore material item. Nothing special about this one

    public ExampleOreItem() {
        super(
                500, // Max stack size
                Item.Rarity.UNCOMMON // Rarity
        );

    }
}
