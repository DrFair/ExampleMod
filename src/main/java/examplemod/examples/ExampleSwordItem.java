package examplemod.examples;

import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.swordToolItem.SwordToolItem;

// Extends SwordToolItem
public class ExampleSwordItem extends SwordToolItem {

    // Weapon attack textures are loaded from resources/player/weapons/<itemStringID>

    public ExampleSwordItem() {
        super(400, null);
        rarity = Item.Rarity.UNCOMMON;
        attackAnimTime.setBaseValue(300); // 300 ms attack time
        attackDamage.setBaseValue(20) // Base sword damage
                .setUpgradedValue(1, 95); // Upgraded tier 1 damage
        attackRange.setBaseValue(120); // 120 range
        knockback.setBaseValue(100); // 100 knockback
    }

}
