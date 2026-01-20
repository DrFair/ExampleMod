package examplemod.examples.ai;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.AINode;
import necesse.entity.mobs.ai.behaviourTree.AINodeResult;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.level.maps.IncursionLevel;
import necesse.level.maps.Level;

/**
 Runs once after spawn:
 If we are in an IncursionLevel and the mob is NOT inside the entrance open-space,
 teleport it to a valid tile near the incursion return portal (entrance centre).

  "Entrance centre" = return portal position:
  IncursionBiome.generateEntrance(...) clears an open area and calls addReturnPortalOnTile(...)
 which ends up setting IncursionLevel returnPortalPosition/ID.
 */
public class ExampleAILeaf<T extends Mob> extends AINode<T> {

    private boolean didCheck = false;

    // How close the mob must be to the entrance centre to count as "in the entrance area"
    private final int openRadiusTiles;

    // How far around the portal we will search to find a valid teleport tile
    private final int searchRadiusTiles;

    public ExampleAILeaf(int openRadiusTiles, int searchRadiusTiles) {
        this.openRadiusTiles = Math.max(1, openRadiusTiles);
        this.searchRadiusTiles = Math.max(this.openRadiusTiles, searchRadiusTiles);
    }

    @Override
    protected void onRootSet(AINode<T> aiNode, T t, Blackboard<T> blackboard) {

    }

    @Override
    public void init(T mob, Blackboard<T> blackboard) {
        // Nothing to init; we just run once in tick().
    }

    @Override
    public AINodeResult tick(T mob, Blackboard<T> blackboard) {
        // Only do this once per mob instance.
        if (didCheck) return AINodeResult.FAILURE;
        didCheck = true;

        // Only do this on the server (positions are authoritative there).
        if (!mob.isServer()) return AINodeResult.FAILURE;

        Level level = mob.getLevel();
        if (!(level instanceof IncursionLevel)) return AINodeResult.FAILURE;

        IncursionLevel incursion = (IncursionLevel) level;

        // Entrance centre is the return portal position.
        Point portalPos = incursion.getReturnPortalPosition();
        if (portalPos == null) return AINodeResult.FAILURE;

        float centerX = portalPos.x;
        float centerY = portalPos.y;

        // If already inside the entrance open space, do nothing.
        float openRadiusPx = this.openRadiusTiles * 32.0f;
        if (mob.getDistance(centerX, centerY) <= openRadiusPx) {
            return AINodeResult.FAILURE;
        }

        // Otherwise teleport to a nearby valid spot close to the portal.
        Point2D.Float dest = findValidTeleportPos(incursion, mob, centerX, centerY, this.searchRadiusTiles);

        if (dest != null) {
            // "Direct" position set (sends movement packet if needed).
            mob.stopMoving();
            mob.setPos(dest.x, dest.y, true);
        }

        // Return FAILURE so parent Selector continues to chase/wander this same tick.
        return AINodeResult.FAILURE;
    }

    private static Point2D.Float findValidTeleportPos(IncursionLevel level, Mob mob, float centerX, float centerY, int searchRadiusTiles) {
        int centerTileX = GameMath.getTileCoordinate(centerX);
        int centerTileY = GameMath.getTileCoordinate(centerY);

        // Try rings from 0 outward (0 = centre)
        for (int r = 0; r <= searchRadiusTiles; r++) {
            ArrayList<Point> ring = buildRing(centerTileX, centerTileY, r);

            // Randomise the order a bit so we don't always pick the same spot
            while (!ring.isEmpty()) {
                Point p = ring.remove(GameRandom.globalRandom.nextInt(ring.size()));

                // Avoid liquid/shore like vanilla spawn logic
                if (level.isLiquidTile(p.x, p.y)) continue;
                if (level.isShore(p.x, p.y)) continue;

                int px = p.x * 32 + 16;
                int py = p.y * 32 + 16;

                // Must not collide with the map, objects, etc.
                if (mob.collidesWith(level, px, py)) continue;

                // Also avoid landing inside another mob/player
                if (mob.collidesWithAnyMob(level, px, py)) continue;

                return new Point2D.Float(px, py);
            }
        }

        // No valid tile found
        return null;
    }

    private static ArrayList<Point> buildRing(int cx, int cy, int r) {
        ArrayList<Point> points = new ArrayList<>();
        if (r == 0) {
            points.add(new Point(cx, cy));
            return points;
        }

        // Top and bottom edges
        for (int dx = -r; dx <= r; dx++) {
            points.add(new Point(cx + dx, cy - r));
            points.add(new Point(cx + dx, cy + r));
        }
        // Left and right edges (excluding corners already added)
        for (int dy = -r + 1; dy <= r - 1; dy++) {
            points.add(new Point(cx - r, cy + dy));
            points.add(new Point(cx + r, cy + dy));
        }

        return points;
    }
}
