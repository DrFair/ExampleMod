package examplemod.examples.items.armor;

import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.inventory.item.Item;
import necesse.inventory.item.armorItem.SetHelmetArmorItem;
import necesse.inventory.lootTable.presets.ArmorSetsLootTable;
import necesse.inventory.lootTable.presets.HeadArmorLootTable;

public class ExampleHelmetArmorItem extends SetHelmetArmorItem {

    public ExampleHelmetArmorItem() {
        super(
                3, // Armor value
                DamageTypeRegistry.MELEE, // Damage class for enchant scaling etc
                ItemRegistry.EQUIPMENT_VALUE_GOLD, // Enchant cost. See explanation in ExampleSwordMeleeWeaponItem
                HeadArmorLootTable.headArmor, // Head armor loot category
                ArmorSetsLootTable.armorSets, // Armor sets loot category
                Item.Rarity.UNCOMMON, // Rarity
                "examplehelmet", // Helmet texture name (loaded from resources/player/armor/...)
                "examplechestplate", // Chest item stringID. Used for set bonus, etc.
                "exampleboots", // Boots item stringID. Used for set bonus, etc.
                "examplearmorsetbonusbuff" // Set bonus buff stringID as defined in ExampleModBuffs
        );
    }

}
