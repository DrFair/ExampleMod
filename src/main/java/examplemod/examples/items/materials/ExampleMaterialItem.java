package examplemod.examples.items.materials;

import necesse.inventory.item.matItem.MatItem;

public class ExampleMaterialItem extends MatItem {

    public ExampleMaterialItem() {
        super(
                100, // Max stack size
                Rarity.UNCOMMON // Rarity
        );
    }

}
