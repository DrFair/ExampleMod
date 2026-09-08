package examplemod.examples.items.materials;

import necesse.inventory.item.matItem.MatItem;

public class ExampleMaterialItem extends MatItem {

    // Super simple example material item. Nothing special about this one

    public ExampleMaterialItem() {
        super(
                100, // Max stack size
                Rarity.UNCOMMON // Rarity
        );
    }

}
