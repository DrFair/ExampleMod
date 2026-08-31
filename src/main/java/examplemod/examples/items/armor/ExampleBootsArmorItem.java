package examplemod.examples.items.armor;

import necesse.engine.registries.ItemRegistry;
import necesse.inventory.item.Item;
import necesse.inventory.item.armorItem.BootsArmorItem;
import necesse.inventory.lootTable.presets.FeetArmorLootTable;

public class ExampleBootsArmorItem extends BootsArmorItem {

    public ExampleBootsArmorItem() {
        super(
                2, // Armor value
                ItemRegistry.EQUIPMENT_VALUE_GOLD, // Enchant cost. See explanation in ExampleSwordMeleeWeaponItem
                Item.Rarity.UNCOMMON, // Rarity
                "exampleboots", // Texture name (loaded from resources/player/armor/...)
                FeetArmorLootTable.feetArmor // Loot table category
        );
    }

}
