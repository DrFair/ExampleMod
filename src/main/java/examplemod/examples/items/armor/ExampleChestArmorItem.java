package examplemod.examples.items.armor;

import necesse.inventory.item.Item;
import necesse.inventory.item.armorItem.ChestArmorItem;
import necesse.inventory.lootTable.presets.BodyArmorLootTable;

public class ExampleChestArmorItem extends ChestArmorItem {
    public ExampleChestArmorItem() {
        super(4,                        //armorValue
                300,                              //enchantCost
                Item.Rarity.UNCOMMON,             //rarity
                "examplechest",                   //bodyTextureName
                "examplearms",                    //armsTextureName
                BodyArmorLootTable.bodyArmor);    //lootTableCategory

    }
}
