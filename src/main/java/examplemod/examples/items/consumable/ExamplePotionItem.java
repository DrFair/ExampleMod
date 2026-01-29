package examplemod.examples.items.consumable;

import necesse.inventory.item.placeableItem.consumableItem.potionConsumableItem.SimplePotionItem;

public class ExamplePotionItem extends SimplePotionItem {

    public ExamplePotionItem() {
        super(100,Rarity.COMMON,"examplebuff",100, "examplepotionitemtip");
    }

}