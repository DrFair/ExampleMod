package examplemod.examples.items;
import necesse.inventory.item.Item;
import necesse.inventory.item.placeableItem.consumableItem.potionConsumableItem.SimplePotionItem;
import examplemod.examples.ExampleBuff;

public class ExamplePotionItem extends SimplePotionItem {
    public ExamplePotionItem() {
        super(100,Rarity.COMMON,"examplebuff",100, new String[] { "examplepotionitemtip" });
    }

}