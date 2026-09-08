package examplemod.examples.items.materials;

import necesse.inventory.item.placeableItem.tileItem.GrassSeedItem;

/**
 * A seed item that turns dirt into our custom grass tile when placed.
 * Extends GrassSeedItem, which handles it all for us
 */
public class ExampleGrassSeedItem extends GrassSeedItem {

    public ExampleGrassSeedItem() {
        // This must match your tile's stringID (registered in ExampleModTiles)
        super("examplegrasstile");
    }

}
