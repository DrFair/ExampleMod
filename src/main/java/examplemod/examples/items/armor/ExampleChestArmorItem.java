package examplemod.examples.items.armor;

import necesse.engine.modifiers.ModifierValue;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.armorItem.ArmorModifiers;
import necesse.inventory.item.armorItem.ChestArmorItem;
import necesse.inventory.item.upgradeUtils.FloatUpgradeValue;
import necesse.inventory.lootTable.presets.BodyArmorLootTable;

public class ExampleChestArmorItem extends ChestArmorItem {

    // Additional stats besides armor value the chestpiece gives (used in getter below)
    public FloatUpgradeValue healthRegen = new FloatUpgradeValue()
            .setBaseValue(1f) // Base: 1 health per second
            .setUpgradedValue(1, 4f); // Tier 1: 4 health per second

    public ExampleChestArmorItem() {
        super(
                4, // Armor value
                ItemRegistry.EQUIPMENT_VALUE_GOLD, // Enchant cost. See explanation in ExampleSwordMeleeWeaponItem
                Item.Rarity.UNCOMMON, // Rarity
                "examplechest", // Body texture name (loaded from resources/player/armor/...)
                "examplearms", // Arms texture name (loaded from resources/player/armor/...)
                BodyArmorLootTable.bodyArmor // Loot table category
        );
    }

    // Here we can return what stats other than armor the piece gives when equipped
    @Override
    public ArmorModifiers getArmorModifiers(InventoryItem item, Mob mob) {
        return new ArmorModifiers(
                new ModifierValue<>(BuffModifiers.COMBAT_HEALTH_REGEN_FLAT, healthRegen.getValue(getUpgradeTier(item)))
        );
    }

}
