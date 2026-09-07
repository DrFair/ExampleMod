package examplemod.examples.objects;

import examplemod.ExampleMod;
import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.PlayerMob;
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
 * - You can interact with this object to change the example settings
 */
public class ExampleConfigObject extends GameObject {

    private GameTexture texture;

    public ExampleConfigObject() {
        super(new Rectangle(32, 32));
        this.isSolid = true;
    }

    @Override
    public void loadTextures() {
        super.loadTextures();
        texture = GameTexture.fromFile("objects/exampleconfigobject");
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
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return true;
    }

    @Override
    public void interact(Level level, int x, int y, PlayerMob player) {
        // This interact method will run both on the server and the client. In this case we only want something
        // to happen when runs on the server, so we do this if check below.

        // In general, when the player does an action you want to verify that it's a valid action on the server.
        // The base game already do this in the case of the interact method. All the client does is send a packet
        // that they want to interact with this object. Then the server checks if the object actually exists,
        // if they're in range etc. You can see this happens in the PacketObjectInteract class.
        // And the server then runs the effect that happens when a client interacted with this object
        if (player.isServerClient()) {
            // Increment server settings value
            ExampleMod.SETTINGS.exampleInt += 1;

            // Save server settings back to disk (server.cfg + cfg/mods/<modid>.cfg)
            Settings.saveServerSettings();

            // Send the message to the client that sent the packet about the settings
            ServerClient client = player.getServerClient();
            client.sendChatMessage("[ExampleMod] Server config updated (saved):");
            client.sendChatMessage("exampleBoolean: " + ExampleMod.SETTINGS.exampleBoolean);
            client.sendChatMessage("exampleInt: " + ExampleMod.SETTINGS.exampleInt);
            client.sendChatMessage("exampleString: " + ExampleMod.SETTINGS.exampleString);
        }
    }

}