package examplemod.examples.tiles;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.gfx.gameTexture.GameTextureSection;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.gameObject.GameObject;
import necesse.level.gameTile.GrassTile;
import necesse.level.gameTile.TerrainSplatterTile;
import necesse.level.maps.Level;
import necesse.level.maps.regionSystem.SimulatePriorityList;

import java.awt.*;

/**
 * ExampleGrassTile which extends TerrainSplatterTile
 * This is a ground tile.
 * It does 3 main things:
 *  1) Sometimes drops a seed sometimes when mined.
 *  2) Can grow a grass object on top of it ("examplegrass").
 *  3) Can spread onto nearby dirt tiles.
 */
public class ExampleGrassTile extends TerrainSplatterTile {

    // How often the grass OBJECT should grow on this tile
    // Takes an average of 7000 seconds to grow a grass
    public static double growChance = GameMath.getAverageSuccessRuns(7000);

    // How often this TILE should spread onto dirt next to it
    // Takes an average of 850 seconds for this to spread to dirt
    public static double spreadChance = GameMath.getAverageSuccessRuns(850);

    // Used only for picking a random sprite row (visual variation)
    private final GameRandom drawRandom = new GameRandom();

    public ExampleGrassTile() {
        // isFloor parameter defines how some other systems interact with it
        // For example if settlers are happy with it in their rooms
        // Texture file: resources/tiles/examplegrasstile_splat.png
        // The texture is a very specific format. It consists of several rows that looks similar,
        // but also a bit different. This gives some variance in which texture is drawn.
        // TerrainSplatterTile asks which row/column to use in our getTerrainSprite method below.
        // The texture name we give here does not include the "_splat" part for backwards compatibility reasons
        super(false, "examplegrasstile");

        mapColor = new Color(140, 0, 133); // Minimap color
        canBeMined = true; // Player can mine/remove it
        isOrganic = true; // Marks it as organic. Defines how some other systems interact with it
    }

    @Override
    public LootTable getLootTable(Level level, int tileX, int tileY) {
        // 4% chance to drop a grass seed when mined
        return new LootTable(new ChanceLootItem(0.04f, "examplegrassseed"));
    }

    @Override
    public void addSimulateLogic(Level level, int x, int y, long ticks,
                                 SimulatePriorityList list, boolean sendChanges) {
        // This happens when a chunk is loaded with this tile that has not been loaded in a while
        // GrassTile has a helper function for this that we can use:
        GrassTile.addSimulateGrow(level, x, y, growChance, ticks, "examplegrass", list, sendChanges);
    }

    @Override
    public double spreadToDirtChance() {
        // Controls how fast dirt turns into this grass tile when nearby
        // The actual logic is handled by the dirt tile itself
        return spreadChance;
    }

    @Override
    public void tick(Level level, int x, int y) {
        // This happens about once a second. Note: This is not time synced between server and client
        // Mostly used for random events and visual stuff like particles, etc.

        // Only the server should change the world
        if (!level.isServer()) return;

        // If there is no object on the tile and our random chance passes
        if (level.getObjectID(x, y) == 0 && GameRandom.globalRandom.getChance(growChance)) {

            // Check if the grass object can be placed. If the canPlace returns null, it means there is no reason
            // it cannot be placed. If it returns a string, that string is the reason it cannot be placed
            GameObject grassObj = ObjectRegistry.getObject("examplegrass");
            if (grassObj.canPlace(level, x, y, 0, false) == null) {
                // Place the object and send an update packet to clients about it
                grassObj.placeObject(level, x, y, 0, false);
                level.sendObjectUpdatePacket(x, y);
            }
        }
    }

    @Override
    public Point getTerrainSprite(GameTextureSection terrainTexture, Level level, int tileX, int tileY) {
        // Pick a random row for the sprite, but keep it consistent per tile position
        int row;
        synchronized (drawRandom) {
            // The reason this is handled in a synchronized segment, is because this method is run on
            // different threads on the same time to faster setup rendering of the next frame
            // See ExampleObject.addDrawables for a bit more explanation on this

            row = drawRandom.seeded(getTileSeed(tileX, tileY))
                    .nextInt(terrainTexture.getHeight() / 32);
        }
        // We only have one column, so we return 0 for the column and the random row we picked
        return new Point(0, row);
    }

    @Override
    public int getTerrainPriority() {
        // TerrainPriority is used to determine which tiles should draw on top,
        // when they're next to each other and overlapping
        return TerrainSplatterTile.PRIORITY_TERRAIN;
    }

}