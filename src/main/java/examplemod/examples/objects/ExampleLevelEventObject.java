package examplemod.examples.objects;

import examplemod.examples.events.ExampleEvent;
import examplemod.examples.events.ExampleLevelEvent;
import necesse.engine.GameEvents;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.Rectangle;
import java.util.List;

/*
 * Basic placeable object that:
 *  draws a single 32x32 sprite in the world
 *
 *  spawns a LevelEvent that sends a chat message (event handles the message, not the object)
 *  and also fires an ExampleEvent which triggers an event listener to run its code
 */
public class ExampleLevelEventObject extends GameObject {

    // Loaded once from your mod resources in loadTextures()
    private GameTexture texture;

    public ExampleLevelEventObject() {
        // 32x32 collision/selection box
        super(new Rectangle(32, 32));
        this.isSolid = true;
    }

    @Override
    public void loadTextures() {
        super.loadTextures();

        // Loads: src/main/resources/objects/exampleleveleventobject.png
        this.texture = GameTexture.fromFile("objects/exampleleveleventobject");
    }

    @Override
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList,
                             Level level, int tileX, int tileY, TickManager tickManager,
                             GameCamera camera, PlayerMob perspective) {

        // Lighting at this tile (so the sprite matches world lighting)
        GameLight light = level.getLightLevel(tileX, tileY);

        // Convert tile coords -> screen draw coords
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        // Build the draw options once, then draw them inside the drawable
        final TextureDrawOptionsEnd opts = this.texture.initDraw()
                .sprite(0, 0, 32)     // first sprite, 32x32
                .light(light)
                .pos(drawX, drawY);

        // Add a drawable so the engine draws it in correct Y-sort order
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            @Override
            public int getSortY() {
                return 16; // typical "middle of the tile" sort value for 1-tile objects
            }

            @Override
            public void draw(TickManager tickManager) {
                opts.draw();
            }
        });
    }

    @Override
    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha,
                            PlayerMob player, GameCamera camera) {

        // This is the translucent "ghost" preview when placing the object
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);

        this.texture.initDraw()
                .sprite(0, 0, 32)
                .light(light)
                .alpha(alpha)
                .draw(drawX, drawY);
    }

    @Override
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return true;
    }

    @Override
    public void interact(Level level, int x, int y, PlayerMob player) {

        /*
         * interact(...) is called on BOTH sides in multiplayer:
         *   client: when you click / interact locally
         *   server: when the server processes the interaction
         *
         * Anything that changes game state (spawning events, sending chat, triggering mod logic)
         * should be done on the SERVER to avoid double-running and desync.
         */
        if (level.isServer()) {

            /*
             * In multiplayer, a PlayerMob on the server is tied to a ServerClient.
             * The "slot" is a simple way to identify which connected client/player we mean.
             * We'll pass this into our event so it knows who to target.
             */
            int clientSlot = player.getServerClient().slot;

            /*
             * Spawn our LevelEvent.
             *
             * We keep the object simple: it just creates the event.
             * The ExampleLevelEvent itself contains the "what happens" logic (like sending a message).
             */
            ExampleLevelEvent ev = new ExampleLevelEvent(clientSlot);

            /*
             * addHidden(...) means "server-only":
             *  the event is added to the level's event manager
             *  it will NOT send a spawn packet to clients
             *
             * Use addHidden when the event is purely server logic (like chat/logging).
             * Use events.add(ev) if you want clients to also receive/tick/draw the event.
             */
            level.entityManager.events.addHidden(ev);


            /*
             * This is an example of triggering an event (in this case ExampleEvent)
             * which will fire the event listener for ExampleEvent to run its code
             */
            GameEvents.triggerEvent(new ExampleEvent(level, clientSlot));
        }

        // Always call super unless you specifically want to block default behavior.
        super.interact(level, x, y, player);
    }
}
