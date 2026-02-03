package examplemod.examples.items.materials;

import necesse.inventory.item.placeableItem.tileItem.GrassSeedItem;

/**
 * A seed item that turns dirt into our custom grass tile when placed.
 *
 * Vanilla uses GrassSeedItem for grass seeds. It handles:
 *   Only placing on dirt
 *   Tile placement + preview
 *   Consuming the item (unless in god mode)
 *   "Grass seed" style tooltip and crafting ingredients
 */
public class ExampleGrassSeedItem extends GrassSeedItem {

    public ExampleGrassSeedItem() {
        // This must match your TileRegistry stringID
        // i.e. TileRegistry.registerTile("examplegrasstile", ...)
        super("examplegrasstile");
    }
}
