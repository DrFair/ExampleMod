package examplemod.examples.objects;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptionsList;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.gameObject.container.CraftingStationObject;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.multiTile.SidedRotationMultiTile;

import java.awt.*;
import java.util.List;

public class ExampleWorkstation2Object extends CraftingStationObject {

    // This is the secondary object to ExampleWorkstationObject
    // See that class for explanations
    // Although, check out the alternated version of getMultiTile below

    public GameTexture texture;
    protected int counterID;

    public ExampleWorkstation2Object() {
        super(new Rectangle(32, 32));
        mapColor = new Color(87, 22, 76);
        isLightTransparent = true;
        hoverHitbox = new Rectangle(0, -16, 32, 48);
    }

    @Override
    public void loadTextures() {
        texture = GameTexture.fromFile("objects/exampleworkstation");
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        return new SidedRotationMultiTile(
                1, 0, // The position of this object is different from the master object
                2, 1, // Same size
                rotation,
                false, // Not the master this time
                counterID, getID() // objectIDs are the same final IDs assigned (swapped variables)
        );
    }

    @Override
    public Rectangle getCollision(Level level, int x, int y, int rotation) {
        // Basically reverse of the master object
        if (rotation == 0) { // Facing north
            return new Rectangle(x * 32, y * 32 + 6, 26, 20);
        } else if (rotation == 1) { // Facing east
            return new Rectangle(x * 32 + 4, y * 32, 24, 26);
        } else if (rotation == 2) { // Facing south
            return new Rectangle(x * 32 + 6, y * 32 + 6, 26, 20);
        } else { // Facing west
            return new Rectangle(x * 32 + 4, y * 32 + 4, 24, 28);
        }
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY,
                             TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        GameLight light = level.getLightLevel(tileX, tileY);
        int rotation = level.getObjectRotation(tileX, tileY);

        DrawOptionsList options = new DrawOptionsList();
        if (rotation == 0) { // Facing north
            options.add(texture.initDraw()
                    .section(32, 2 * 32, 3 * 32, 5 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));
        } else if (rotation == 1) { // Facing east
            options.add(texture.initDraw()
                    .section(0, 32, 2 * 32, 3 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));
        } else if (rotation == 2) { // Facing south
            options.add(texture.initDraw()
                    .section(0, 32, 5 * 32, 7 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));
        } else { // Facing west
            options.add(texture.initDraw()
                    .section(32, 2 * 32, 0, 2 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));
        }

        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16;
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation,
                            float alpha, PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        if (rotation == 0) { // Facing north
            texture.initDraw()
                    .section(32, 2 * 32, 3 * 32, 5 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY - 32);
        } else if (rotation == 1) { // Facing east
            texture.initDraw()
                    .section(0, 32, 2 * 32, 3 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY);
        } else if (rotation == 2) { // Facing south
            texture.initDraw()
                    .section(0, 32, 5 * 32, 7 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY - 32);
        } else { // Facing west
            texture.initDraw()
                    .section(32, 2 * 32, 0, 2 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY - 32);
        }
    }

}