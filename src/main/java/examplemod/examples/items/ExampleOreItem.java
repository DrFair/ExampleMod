package examplemod.examples.items;

import necesse.inventory.item.Item;
import necesse.inventory.item.matItem.MatItem;

public class ExampleOreItem extends MatItem {

    public static final String ID = "exampleore";

    public ExampleOreItem() {
        super(500, Item.Rarity.UNCOMMON);

    }
}
