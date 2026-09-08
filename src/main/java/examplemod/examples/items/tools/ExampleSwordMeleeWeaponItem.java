package examplemod.examples.items.tools;

import necesse.engine.registries.ItemRegistry;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.swordToolItem.SwordToolItem;
import necesse.inventory.lootTable.presets.CloseRangeWeaponsLootTable;

// Extends SwordToolItem
public class ExampleSwordMeleeWeaponItem extends SwordToolItem {

    // Weapon attack textures are loaded from resources/player/weapons/<itemStringID>

    public ExampleSwordMeleeWeaponItem() {
        super(
                ItemRegistry.EQUIPMENT_VALUE_GOLD, // Enchant cost
                CloseRangeWeaponsLootTable.closeRangeWeapons // Loot table category
        );
        // Enchant cost also defines the general "value" of the equipment. This is used for how much it's
        // prioritized by settlers for changing their weapon, and when determining which raid should spawn and
        // with what gear they spawn

        rarity = Item.Rarity.UNCOMMON; // Rarity
        attackAnimTime.setBaseValue(300); // 300 ms attack time
        attackDamage.setBaseValue(20) // Base Sword damage
                .setUpgradedValue(1, 95); // Upgraded Tier 1 Damage
        attackRange.setBaseValue(120); // 120 Range
        knockback.setBaseValue(100); // 100 Knockback
    }

}
