package examplemod.examples.items.consumable;

import necesse.inventory.item.placeableItem.consumableItem.potionConsumableItem.SimplePotionItem;

public class ExamplePotionItem extends SimplePotionItem {

    public ExamplePotionItem() {
        super(
                100, // Max stack size
                Rarity.COMMON, // Item rarity
                "examplebuff", // Buff stringID to apply
                120, // Buff duration in seconds
                "examplepotionitemtip" // Localization key for tooltip (under itemtooltip category)
        );
    }

}