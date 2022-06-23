package examplemod.examples;

import necesse.inventory.item.toolItem.swordToolItem.CustomSwordToolItem;

// Extends CustomSwordToolItem
public class ExampleSwordItem extends CustomSwordToolItem {

    // Weapon attack textures are loaded from resources/player/weapons/<itemStringID>

    public ExampleSwordItem() {
        super(Rarity.UNCOMMON, 300, 20, 120, 100, 400);
    }

}
