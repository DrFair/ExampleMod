package examplemod.examples.items.tools;

import necesse.engine.registries.ItemRegistry;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.projectileToolItem.bowProjectileToolItem.BowProjectileToolItem;
import necesse.inventory.lootTable.presets.BowWeaponsLootTable;

public class ExampleBowRangedWeaponItem extends BowProjectileToolItem {

    public ExampleBowRangedWeaponItem() {
        super(
                ItemRegistry.EQUIPMENT_VALUE_GOLD, // Enchant Cost
                BowWeaponsLootTable.bowWeapons // Loot table category
        );
        rarity = Item.Rarity.NORMAL;

        // Core stats
        attackAnimTime.setBaseValue(800); // Attack animation time in milliseconds
        attackDamage.setBaseValue(16) // Base damage
                .setUpgradedValue(1, 120); // Upgraded tier 1 damage
        attackRange.setBaseValue(600); // Attack range
        velocity.setBaseValue(100); // Projectile velocity
        knockback.setBaseValue(25); // Knockback

        // Offsets of the attack item sprite relative to the player arm
        attackXOffset = 8;
        attackYOffset = 20;

        // How much the bow sprite “stretches” while charging
        attackSpriteStretch = 4;

        // Optional
        canBeUsedForRaids = true;
    }

}
