package examplemod.examples.items.armor;

import necesse.inventory.item.Item;
import necesse.inventory.item.armorItem.BootsArmorItem;
import necesse.inventory.lootTable.presets.FeetArmorLootTable;

public class ExampleBootsArmorItem extends BootsArmorItem {
    public ExampleBootsArmorItem() {

        super(2,                      //armorValue
                300,                            //enchantCost
                Item.Rarity.UNCOMMON,           //rarity
                "exampleboots",                 //textureName
                FeetArmorLootTable.feetArmor);  //lootTableCategory
    }
}
