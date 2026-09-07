package examplemod.examples.items.materials;

import necesse.inventory.item.placeableItem.StonePlaceableItem;

/**
 * Stone item dropped by our example rocks
 * Extends StonePlaceableItem, which allows it to be placed on dirt to make gravel
 */
public class ExampleStoneItem extends StonePlaceableItem {

   public ExampleStoneItem() {
       super(5000); // Max stack size
   }

}
