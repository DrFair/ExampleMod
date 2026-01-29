package examplemod.examples.items.armor;

import necesse.engine.registries.DamageTypeRegistry;
import necesse.inventory.item.Item;
import necesse.inventory.item.armorItem.SetHelmetArmorItem;
import necesse.inventory.lootTable.presets.ArmorSetsLootTable;
import necesse.inventory.lootTable.presets.HeadArmorLootTable;

public class ExampleHelmetArmorItem extends SetHelmetArmorItem {
    public ExampleHelmetArmorItem() {
        super(
                3,                    //armor value
                DamageTypeRegistry.MELEE,       //damage class for enchant scaling etc
                300,                            //enchant cost
                HeadArmorLootTable.headArmor,   //head armor loot category
                ArmorSetsLootTable.armorSets,   //armor SETS loot category
                Item.Rarity.UNCOMMON,
                "examplehelmet",     //helmet texture name
                "examplechestplate",            //chest item STRING ID
                "exampleboots",                 //boots item STRING ID
                "examplearmorsetbonus"          //buff STRING ID
        );
    }
}
