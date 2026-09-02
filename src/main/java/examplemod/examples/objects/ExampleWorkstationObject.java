package examplemod.examples.objects;

import examplemod.Loaders.ExampleModTech;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.registries.ObjectRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.DrawOptionsList;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.recipe.Tech;
import necesse.level.gameObject.container.CraftingStationObject;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;
import necesse.level.maps.multiTile.MultiTile;
import necesse.level.maps.multiTile.SidedRotationMultiTile;

import java.awt.*;
import java.util.List;

public class ExampleWorkstationObject extends CraftingStationObject {

    // This class is also an example of a multi tile object. Since the workstation takes up 2x1 tiles.
    // The other object is stored in ExampleWorkstation2Object

    // First we assign our variables we will be using later

    // All objects run the loadTextures() method when the game loads all of its resources
    // Dedicated servers does not load or draw any textures (this will always be null)
    // Here we just declare the variable that will be loaded later
    public GameTexture texture;

    // Here we declare the objectID for the other object (ExampleWorkstation2Object), which will be assigned later
    protected int counterID;

    public ExampleWorkstationObject() {
        super(new Rectangle(32, 32));
        mapColor = new Color(87, 22, 76);
        isLightTransparent = true;

        // Here we change the hover hitbox to be 16 tiles higher than the tile it is at
        hoverHitbox = new Rectangle(0, -16, 32, 48);
    }

    @Override
    public void loadTextures() {
        // As explained above, here we load the texture from the objects folder
        texture = GameTexture.fromFile("objects/exampleworkstation");
    }

    @Override
    public Tech[] getCraftingTechs() {
        // Here we define which crafting techs we want this crafting station to be able to craft
        // In this case, we use our own registered example tech
        return new Tech[] { ExampleModTech.EXAMPLE_TECH };
    }

    @Override
    public MultiTile getMultiTile(int rotation) {
        // Since this is a multi tile, we here have to define how that multi tile behaves
        // Both in terms of how big it is, and also what happens when it is rotated/mirrored in presets, etc.

        // We use SidedRotationMultiTile, because that fixes offset position when mirrored in presets
        return new SidedRotationMultiTile(
                0, 0, // This object is placed at (0,0) in the multi tile
                2, 1, // The entire multi tile is 2 tiles wide and 1 tile high
                rotation,
                true, // This object is the master object (the one that's placed, etc.)
                // Lastly, we define the object IDs that is part of this multi tile. The total parameters
                // we give here have to match the total size of the multi tile. In this case,
                // that is 2*1 = 2. If the multi tile was 2x2 tiles, it would be 4, etc.
                // The other which we add them matter as well. It has to first be the top left one, then
                // the one to the right of that, wrapping around and starting on the next row. If this was
                // a 2x2 multi tile, it would look like this:
                // topLeftID, topRightID, bottomLeftID, bottomRightID

                // It's here we use the counterID assigned when we register the objects
                getID(), counterID
        );
    }

    @Override
    public Rectangle getCollision(Level level, int x, int y, int rotation) {
        // Since we want the collision to be different based on rotation, we override it here and
        // return the desired collision
        // Remember that the rectangle we return should always be within the 32x32 tile size

        if (rotation == 0) { // Facing north
            // A shorter/wider box, shifted in from left and down.
            // Starts 6px in and 6px down, 26px wide, 20px tall.
            return new Rectangle(x * 32 + 6, y * 32 + 6, 26, 20);
        } else if (rotation == 1) { // Facing east
            // The tallest version (almost fills the tile vertically).
            // Starts 4px in and 4px down, 24px wide, 28px tall.
            return new Rectangle(x * 32 + 4, y * 32 + 4, 24, 28);
        } else if (rotation == 2) { // Facing south
            // Similar size to rotation 1 but shifted left a bit.
            // Starts at the left edge, 6px down, 26px wide, 20px tall.
            return new Rectangle(x * 32, y * 32 + 6, 26, 20);
        } else { // Facing west
            // A taller box that starts at the top of the tile.
            // 4px inset from the left, 24px wide, 26px tall.
            return new Rectangle(x * 32 + 4, y * 32, 24, 26);
        }
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY,
                             TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        // This is where we setup the drawables and add them to the drawables list for the next frame

        // First we collect the variables we need for setup
        // The screen coordinates, relative the to camera we should draw it at
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // The current lighting of the tile
        GameLight light = level.getLightLevel(tileX, tileY);

        // The rotation of the object
        int rotation = level.getObjectRotation(tileX, tileY);

        // Now we setup our draw options list and iterate through our rotations to add
        // the correct draw options for each one
        DrawOptionsList options = new DrawOptionsList();
        if (rotation == 0) { // Facing north
            // Here's what's going on:
            // First we initialize a draw of the loaded texture
            // Next, we assign which pixels from that texture should be drawn
            // Next, we add the damage overlay (when you mine it)
            // Next, we assign lighting to the drawn texture
            // And lastly, we position the drawn texture on the screen
            // That is then added to the draw options list, which is used later for actually drawing it

            // The values passed into sections and pos is just me looking at the raw texture, and figuring
            // out which pixels should be drawn and which offset it should be drawn with
            options.add(texture.initDraw()
                    .section(0, 32, 3 * 32, 5 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));
        } else if (rotation == 1) { // Facing east
            options.add(texture.initDraw()
                    .section(0, 32, 0, 2 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));
        } else if (rotation == 2) { // Facing south
            options.add(texture.initDraw()
                    .section(32, 2 * 32, 5 * 32, 7 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY - 32));
        } else { // Facing west
            options.add(texture.initDraw()
                    .section(32, 2 * 32, 2 * 32, 3 * 32)
                    .addObjectDamageOverlay(this, level, tileX, tileY)
                    .light(light)
                    .pos(drawX, drawY));
        }

        // Necesse draws objects using LevelSortedDrawable so they sort correctly in front or behind other things
        // We add the drawable entry for this tile, and inside it, we draw our options list
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                // Draw order within the tile
                // 16 = middle of tile because 1 tile = 32
                return 16;
            }

            @Override
            public void draw(TickManager tickManager) {
                // Actually draw everything we queued up above
                options.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation,
                            float alpha, PlayerMob player, GameCamera camera) {
        // Drawing preview is very similar to addDrawables, however this time we don't add
        // the drawables to a list, we just draw them directly with an alpha and no lighting
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        if (rotation == 0) { // Facing north
            texture.initDraw()
                    .section(0, 32, 3 * 32, 5 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY - 32); // Instead of assigning a screen position, we draw it directly
        } else if (rotation == 1) { // Facing east
            texture.initDraw()
                    .section(0, 32, 0, 2 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY - 32);
        } else if (rotation == 2) { // Facing south
            texture.initDraw()
                    .section(32, 2 * 32, 5 * 32, 7 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY - 32);
        } else { // Facing west
            texture.initDraw()
                    .section(32, 2 * 32, 2 * 32, 3 * 32)
                    .alpha(alpha)
                    .draw(drawX, drawY);
        }
    }

    // Call this from your mod init to register BOTH pieces
    public static int[] register() {
        ExampleWorkstationObject main = new ExampleWorkstationObject();
        ExampleWorkstation2Object part = new ExampleWorkstation2Object();

        int mainID = ObjectRegistry.registerObject("exampleworkstation", main, 10f, true);
        int partID = ObjectRegistry.registerObject("exampleworkstation2", part, 0f, false);

        // Link them together (this is the key)
        main.counterID = partID;
        part.counterID = mainID;

        return new int[] { mainID, partID };
    }

}