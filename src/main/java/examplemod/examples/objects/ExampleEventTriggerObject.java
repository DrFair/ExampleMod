package examplemod.examples.objects;

import examplemod.examples.objectentity.ExampleObjectEntity;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

/**
 * See ExampleObject for a simple object and ExampleWorkstationObject for a more complex object with
 * explanations for code without comments here
 * This object is pretty basic:
 * - Draws a 32x32 sprite in the world
 * - Uses ExampleObjectEntity to work as a "pressureplate" which triggers an ExampleLevelEvent
 */
public class ExampleEventTriggerObject extends GameObject {

    private GameTexture texture;

    public ExampleEventTriggerObject() {
        super(new Rectangle()); // No collision
    }

    @Override
    public void loadTextures() {
        super.loadTextures();
        texture = GameTexture.fromFile("objects/exampleeventtriggerobject");
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY, TickManager tickManager,
                             GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        TextureDrawOptionsEnd opts = texture.initDraw()
                .light(light)
                .pos(drawX, drawY);

        // We add it to the tile list instead of the LevelSortedDrawable list
        // This makes it draw right after all the tiles have been drawn, but before any other objects
        tileList.add(tm -> opts.draw());
    }


    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha,
                            PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        texture.initDraw()
                .alpha(alpha)
                .draw(drawX, drawY);
    }

    @Override
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        // GameObject are static objects, sharing data between all other objects of that same type in the world
        // If we want custom data for a specific object, we have to assign it an ObjectEntity
        // Each ObjectEntity is unique to the specific tile and will allow us to define data like items in
        // a chest, cooldown for a trigger, etc.
        return new ExampleObjectEntity(level, x, y);
    }

}
