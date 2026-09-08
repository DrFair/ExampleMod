package examplemod.examples.objects;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class ExampleObject extends GameObject {

    // This is just a simple object cosmetic object, explaining a bit of how objects work

    // All objects run the loadTextures() method when the game loads all of its resources
    // Dedicated servers does not load or draw any textures (this will always be null)
    // Here we just declare the variable that will be loaded later
    protected GameTexture texture;

    public ExampleObject() {
        // In the super, we define the collision relative to the tile it's placed on
        // Tiles are 32x32 pixels in size, so defining an area outside that bounds does not work
        // In this case, collision is in the center of the tile, but does not cover it completely (24x24, centered)
        super(new Rectangle(4, 4, 24, 24));

        // By default, you can target objects only by hovering over their tile location (32x32)
        // Here we change that, so that the hover hitbox also covers the tile above it
        hoverHitbox = new Rectangle(0, -32, 32, 64);

        // It can be broken by all tools
        toolType = ToolType.ALL;

        // It lets light pass through it
        isLightTransparent = true;

        // Defines what color it has on the minimap, etc.
        // It also defines which color particles come out when we break it
        // This can be overridden by setting debrisColor field though
        mapColor = new Color(31, 150, 148); // Also applies as debris color if not set

        // We set the category that this object should be part of
        // You can see the registered category stringIDs in the ItemCategory class. Link:
        /// {@link necesse.inventory.item.ItemCategory}
        setItemCategory("objects", "columns");
        // Same with crafting category (where they are displayed in the workstation)
        setCraftingCategory("objects", "columns");
    }

    @Override
    public void loadTextures() {
        super.loadTextures();
        // As explained above, here we load the texture from the objects folder in resources
        texture = GameTexture.fromFile("objects/exampleobject");
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        // Necesse has an asynchronous level rendering pipeline. This means that we calculate and setup as
        // much as possible for the next frame, at the same time we are rendering the previous frame
        // This also means that everything happening in here will be subject to concurrency, so anything
        // that is reused has to be considered for that

        // First we collect the variables we need for setup
        // The screen coordinates, relative the to camera we should draw it at
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // The current lighting of the tile
        GameLight light = level.getLightLevel(tileX, tileY);

        // The rotation of the object (not used in this example)
        // int rotation = level.getObjectRotation(tileX, tileY);

        // The most simple form of texture drawables setup
        // We initialize the texture for drawing, set the lighting and the position on the screen
        // We could select a specific part of the texture by calling texture.initDraw().sprite(...)
        // See ExampleWorkstationObject for a more complex example of this
        TextureDrawOptions options = texture.initDraw()
                .light(light)
                .pos(drawX, drawY - texture.getHeight() + 32);

        // Necesse draws objects using LevelSortedDrawable so they sort correctly in front or behind other things
        // We add the drawable entry for this tile, and inside it, we draw what we have set up
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                // Basically where this will be sorted on the Y axis (when it will be behind the player etc.)
                // Should be in [0 - 32] range
                return 16; // 16 is the center of the tile
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha, PlayerMob player, GameCamera camera) {
        // Drawing preview is very similar to addDrawables, however this time we don't add
        // the drawables to a list, we just draw them directly with an alpha and no lighting
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        texture.initDraw()
                .alpha(alpha)
                .draw(drawX, drawY - texture.getHeight() + 32);
    }

}
