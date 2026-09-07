package examplemod.examples.objectentity;

import necesse.entity.mobs.GameDamage;
import necesse.entity.objectEntity.TrapObjectEntity;
import necesse.entity.projectile.TrapArrowProjectile;
import necesse.level.maps.Level;

import java.awt.*;

/*
 * Arrow trap logic.
 * When this trap is triggered by a wire, it shoots an arrow in the direction it faces.
 */

/**
 * This object entity extends TrapObjectEntity, which handles things like cooldown, wire activation, etc
 */
public class ExampleTrapObjectEntity extends TrapObjectEntity {

    // The damage the arrow will deal when it hits something
    // GameDamage can be customized a lot like this:
    public static final GameDamage DAMAGE = new GameDamage(
            40f, // 40 base damage
            100f, // 100 armor penetration (ignores all armor at 100 or below)
            0f, // Has a +0% base crit chance
            2f, // Deals 2x damage to players
            1f // Multiplies the final damage (after armor pen is calculated) by 1 (default)
    );

    public ExampleTrapObjectEntity(Level level, int x, int y) {
        // Cooldown in milliseconds (1000ms = 1 second)
        super(level, x, y, 1000);

        // No need to save anything in this trap
        shouldSave = false;
    }

    @Override
    public void triggerTrap(int wireID, int dir) {
        // Only the server should spawn projectiles
        // Also, don't fire again while we're still on cooldown
        if (isClient() || onCooldown()) return;

        // If a different wire is active at the same time, ignore this trigger
        if (otherWireActive(wireID)) return;

        // This getter is used to get the tile right in from of our current tile
        Point startTile = getPos(tileX, tileY, dir);

        // Converts the object direction to a simple (x,y) direction
        Point fireDirection = getFireDirection(dir);

        // Convert tile coordinates into level coordinates (32 pixels per tile)
        // This will result in the level coordinates of the top-left corner of the tile in front of the trap
        int xPos = startTile.x * 32;
        int yPos = startTile.y * 32;

        // Now we shift the spawn position a bit so the arrow looks like it comes from the correct edge
        if (fireDirection.x == 0) xPos += 16; // Shooting up/down: Center of the tile
        else if (fireDirection.x == -1) xPos += 30; // Shooting left: Near the left edge
        else if (fireDirection.x == 1) xPos += 2; // Shooting right: Near the right edge

        if (fireDirection.y == 0) yPos += 16; // Shooting left/right: Center of the tile
        else if (fireDirection.y == -1) yPos += 30; // Shooting up: Near the top edge
        else if (fireDirection.y == 1) yPos += 2; // Shooting down: Near the bottom edge

        // Create and spawn the projectile
        // The "target" is just one step in the direction we're firing
        getLevel().entityManager.projectiles.add(new TrapArrowProjectile(
                xPos, yPos,
                xPos + fireDirection.x,
                yPos + fireDirection.y,
                DAMAGE,
                null
        ));

        // Start the cooldown so it can't fire again instantly
        startCooldown();
    }

    private Point getFireDirection(int dir) {
        if (dir == 0) return new Point(0, -1); // Up
        if (dir == 1) return new Point(1, 0); // Right
        if (dir == 2) return new Point(0, 1); // Down
        if (dir == 3) return new Point(-1, 0); // Left
        return new Point(0, 0);
    }

}