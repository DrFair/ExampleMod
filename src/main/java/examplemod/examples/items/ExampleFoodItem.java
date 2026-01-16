package examplemod.examples.items;

import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.item.Item;
import necesse.inventory.item.placeableItem.consumableItem.food.FoodConsumableItem;
import necesse.level.maps.levelData.settlementData.settler.Settler;

public class ExampleFoodItem extends FoodConsumableItem {
    public ExampleFoodItem() {
        super(
                250,        // stack size
                Item.Rarity.COMMON,  // rarity
                Settler.FOOD_FINE,   // food tier
                20,                  // nutrition
                480,                 // buff duration in seconds
                new ModifierValue<>(BuffModifiers.MAX_HEALTH_FLAT, 10),
                new ModifierValue<>(BuffModifiers.SPEED, 0.05f)
        );

        // Configure additional properties after super()
        this.spoilDuration(480);                  // spoil duration in minutes
        this.addGlobalIngredient("anycookedfood");// NOTE: returns Item
    }
}