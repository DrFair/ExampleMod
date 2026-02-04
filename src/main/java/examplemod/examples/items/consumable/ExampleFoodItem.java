package examplemod.examples.items.consumable;

import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.inventory.item.Item;
import necesse.inventory.item.placeableItem.consumableItem.food.FoodConsumableItem;
import necesse.level.maps.levelData.settlementData.settler.Settler;

public class ExampleFoodItem extends FoodConsumableItem {

    public ExampleFoodItem() {
        super(
                250, // Stack size
                Item.Rarity.COMMON, // Rarity
                Settler.FOOD_FINE, // Food tier
                20, // Nutrition
                480, // Buff duration in seconds
                // Then we define the modifiers this food gives when consumed
                new ModifierValue<>(BuffModifiers.MAX_HEALTH_FLAT, 10),
                new ModifierValue<>(BuffModifiers.SPEED, 0.05f)
        );

        // Configure additional properties after super()
        spoilDuration(480); // Spoil duration in minutes
        addGlobalIngredient("anycookedfood");
    }

}