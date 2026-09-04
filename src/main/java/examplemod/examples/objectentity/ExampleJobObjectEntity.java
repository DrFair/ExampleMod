package examplemod.examples.objectentity;

import examplemod.examples.settlement.jobs.ExampleLevelJob;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObject;

public class ExampleJobObjectEntity extends ObjectEntity {

    // Config variables which are global for all entities
    // We define these as public static so that others can change them if they want to
    public static int TILE_RADIUS = 15;
    public static int TILES_PER_SECOND = 100;

    // The current state of the scan
    // Since these values are only used by the server, we don't need to sync them with clients
    // See in serverTick() how these are used
    protected float tilesToScanBuffer;
    protected int currentDeltaX;
    protected int currentDeltaY;

    public ExampleJobObjectEntity(Level level, int tileX, int tileY) {
        // The type we define here is used to verify corruption on level loading, etc.
        // Make sure it's always the same for the same object
        super(level, "examplejobobjectentity", tileX, tileY);
        // By default, object entities will be saved onto a level. So we don't need to say this:
        // shouldSave = true;

        // We initialize the current scan delta tiles to be the beginning of the radius
        currentDeltaX = -TILE_RADIUS;
        currentDeltaY = -TILE_RADIUS;
    }

    @Override
    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        // Here we add the data we want to be persistent between loads, etc.
        // Save data is a string key-value system, with the possibility of adding more
        // save data branches as the value. SaveData have adders for all the basic stuff,
        // but if you need to add something custom, you can create your own string to data parser

        // Here we want to save the buffer as well as the current scan tiles
        save.addFloat("tilesToScanBuffer", tilesToScanBuffer);
        save.addInt("currentDeltaX", currentDeltaX);
        save.addInt("currentDeltaY", currentDeltaY);
    }

    @Override
    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        // Here we load the data that was saved in addSaveData
        // Something to keep in mind is that we should never trust the load data. Which means we should always
        // expect data to be corrupt or missing. LoadData getters have default ways to handle this

        // This is how we load it:
        tilesToScanBuffer = save.getFloat(
                "tilesToScanBuffer", // The identifier we assigned in addSaveData
                tilesToScanBuffer, // If the data is corrupted or missing, we will just keep the current value
                false // We don't want to print a warning if the data is corrupted or missing
        );
        // If we simply used save.getFloat("tilesToScanBuffer"), it would throw exceptions that we have to deal with

        // Same thing with current scan tiles
        currentDeltaX = save.getInt("currentDeltaX", currentDeltaX, false);
        currentDeltaY = save.getInt("currentDeltaY", currentDeltaY, false);
    }

    @Override
    public void serverTick() {
        super.serverTick();

        // We advance the scan evenly across game ticks. This makes sure that we have a smooth framerate and
        // no stutters from high compute ticks

        // First we found out how many tiles per tick that is
        float tilesPerTick = (float) TILES_PER_SECOND / TickManager.ticksPerSec;
        // Next we add that number to our buffer
        tilesToScanBuffer += tilesPerTick;

        // And for each tile we have left to tick, we do that
        while (tilesToScanBuffer >= 1) {
            tilesToScanBuffer -= 1; // Reduce the buffer as we're handling the ticks

            Level level = getLevel();
            // Calculate the current tile we want to process
            int currentTileX = tileX + currentDeltaX;
            int currentTileY = tileY + currentDeltaY;

            // Advance scan cursor (square area)
            currentDeltaX++;
            if (currentDeltaX > TILE_RADIUS) {
                currentDeltaX = -TILE_RADIUS;
                currentDeltaY++;
                if (currentDeltaY > TILE_RADIUS) {
                    currentDeltaY = -TILE_RADIUS;
                }
            }

            // Handle the current tile
            if (!level.isTileWithinBounds(currentTileX, currentTileY)) continue;

            GameObject object = level.getObject(currentTileX, currentTileY);
            boolean isPlayerPlaced = level.objectLayer.isPlayerPlaced(currentTileX, currentTileY);
            if (!isValidObject(object, isPlayerPlaced)) continue;

            // Add your example job
            level.jobsLayer.addJob(new ExampleLevelJob(currentTileX, currentTileY, this));
        }
    }

    public boolean isValidObject(GameObject object, boolean isPlayerPlaced) {
        // Don’t clear decorative / player-placed grass
        if (isPlayerPlaced) return false;
        // Only clear grass objects
        return object.isGrass;
    }

    // Helper method used in ExampleLevelJob
    public boolean isValidLevelObject(LevelObject levelObject) {
        return isValidObject(levelObject.object, levelObject.isPlayerPlaced);
    }

}
