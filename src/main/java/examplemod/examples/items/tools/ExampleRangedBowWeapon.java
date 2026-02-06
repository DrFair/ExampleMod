package examplemod.examples.items.tools;

import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.projectileToolItem.bowProjectileToolItem.BowProjectileToolItem;
import necesse.inventory.lootTable.presets.BowWeaponsLootTable;

public class ExampleRangedBowWeapon extends BowProjectileToolItem {
    public ExampleRangedBowWeapon() {
        // (enchantCost, lootTableCategory)
        super(100, BowWeaponsLootTable.bowWeapons);

        this.rarity = Item.Rarity.NORMAL;

        // Core stats
        this.attackAnimTime.setBaseValue(800);                 // ms per shot
        this.attackDamage.setBaseValue(12.0F);                 // base bow damage (arrows further modify)
        this.attackRange.setBaseValue(600);                    // tiles-ish range value used by bows
        this.velocity.setBaseValue(100);                       // base projectile velocity (arrows further modify)
        this.knockback.setBaseValue(25);

        // Sprite offsets (tune until it looks right in-hand)
        this.attackXOffset = 8;
        this.attackYOffset = 20;

        // How much the bow sprite “stretches” while charging
        this.attackSpriteStretch = 4;

        // Optional
        this.canBeUsedForRaids = true;
    }
}
