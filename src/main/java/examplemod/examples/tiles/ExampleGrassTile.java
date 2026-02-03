package examplemod.examples.tiles;

import java.awt.Color;
import java.awt.Point;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.gfx.gameTexture.GameTextureSection;
import necesse.inventory.lootTable.LootItemInterface;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.gameObject.GameObject;
import necesse.level.gameTile.TerrainSplatterTile;
import necesse.level.maps.Level;
import necesse.level.maps.regionSystem.SimulatePriorityList;

public class ExampleGrassTile extends TerrainSplatterTile {
    // You can tweak these to change growth/spread speeds
    public static double growChance   = GameMath.getAverageSuccessRuns(7000.0D);
    public static double spreadChance = GameMath.getAverageSuccessRuns(850.0D);

    private final GameRandom drawRandom = new GameRandom();

    public ExampleGrassTile() {
        // IMPORTANT: this string must match your texture name in resources/tiles/
        // (e.g. resources/tiles/examplegrass.png)
        super(false, "examplegrasstile");

        this.mapColor = new Color(70, 120, 40); // minimap color
        this.canBeMined = true;
        this.isOrganic = true;
    }

    @Override
    public LootTable getLootTable(Level level, int tileX, int tileY) {
        // Option A: drop vanilla grassseed
        // return new LootTable(new ChanceLootItem(0.04F, "grassseed"));

        // Option B: drop your own seed item (if you register one)
        return new LootTable(new LootItemInterface[]{
                new ChanceLootItem(0.04F, "examplegrassseed")
        });
    }

    @Override
    public void addSimulateLogic(Level level, int x, int y, long ticks, SimulatePriorityList list, boolean sendChanges) {
        addSimulateGrow(level, x, y, growChance, ticks, "examplegrass", list, sendChanges);
    }

    // Same helper pattern vanilla uses (simplified)
    public static void addSimulateGrow(Level level, int tileX, int tileY, double chance, long ticks,
                                       String growObjectID, SimulatePriorityList list, boolean sendChanges) {
        if (level.getObjectID(tileX, tileY) == 0) {
            double runs = Math.max(1.0D, GameMath.getRunsForSuccess(chance, GameRandom.globalRandom.nextDouble()));
            long remainingTicks = (long)(ticks - runs);
            if (remainingTicks > 0L) {
                GameObject obj = ObjectRegistry.getObject(ObjectRegistry.getObjectID(growObjectID));
                if (obj.canPlace(level, tileX, tileY, 0, false) == null) {
                    list.add(tileX, tileY, remainingTicks, () -> {
                        if (obj.canPlace(level, tileX, tileY, 0, false) == null) {
                            obj.placeObject(level, tileX, tileY, 0, false);
                            level.objectLayer.setIsPlayerPlaced(tileX, tileY, false);
                            if (sendChanges) level.sendObjectUpdatePacket(tileX, tileY);
                        }
                    });
                }
            }
        }
    }

    @Override
    public double spreadToDirtChance() {
        // This is what makes dirt convert into your grass when adjacent
        return spreadChance;
    }

    @Override
    public void tick(Level level, int x, int y) {
        if (!level.isServer()) return;

        // Grow your grass object on empty tiles
        if (level.getObjectID(x, y) == 0 && GameRandom.globalRandom.getChance(growChance)) {
            GameObject grassObj = ObjectRegistry.getObject(ObjectRegistry.getObjectID("examplegrass"));
            if (grassObj.canPlace(level, x, y, 0, false) == null) {
                grassObj.placeObject(level, x, y, 0, false);
                level.objectLayer.setIsPlayerPlaced(x, y, false);
                level.sendObjectUpdatePacket(x, y);
            }
        }
    }

    @Override
    public Point getTerrainSprite(GameTextureSection terrainTexture, Level level, int tileX, int tileY) {
        int tile;
        synchronized (drawRandom) {
            tile = drawRandom.seeded(getTileSeed(tileX, tileY)).nextInt(terrainTexture.getHeight() / 32);
        }
        return new Point(0, tile); // column 0, random row
    }

    @Override
    public int getTerrainPriority() {
        return 100; // same as vanilla grass
    }
}