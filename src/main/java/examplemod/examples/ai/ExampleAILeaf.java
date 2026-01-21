package examplemod.examples.ai;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import examplemod.examples.packets.ExamplePlaySoundPacket;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.AINode;
import necesse.entity.mobs.ai.behaviourTree.AINodeResult;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.level.maps.IncursionLevel;
import necesse.level.maps.Level;

/*
    This is a "run once after spawn" AI leaf.

    The problem it solves:
    • Sometimes a mob spawns in a bad spot in an Incursion (not in the cleared entrance area)
    • If that happens, we move it into the entrance area so the fight / behaviour starts properly

    What we consider the "entrance area":
    • The centre is the Incursion return portal position
    • The biome generator clears open space around that portal
*/
public class ExampleAILeaf<T extends Mob> extends AINode<T> {

    // We only want to do the check once per mob.
    private boolean didCheck = false;

    // How close you need to be to the portal to count as "in the entrance area"
    // (in tiles, converted to pixels later).
    private final int openRadiusTiles;

    // How far we search around the portal to find a safe landing spot.
    private final int searchRadiusTiles;

    public ExampleAILeaf(int openRadiusTiles, int searchRadiusTiles) {
        // Just making sure we don't get silly values.
        this.openRadiusTiles = Math.max(1, openRadiusTiles);
        this.searchRadiusTiles = Math.max(this.openRadiusTiles, searchRadiusTiles);
    }

    @Override
    protected void onRootSet(AINode<T> aiNode, T t, Blackboard<T> blackboard) {
        // Nothing needed here for this leaf.
    }

    @Override
    public void init(T mob, Blackboard<T> blackboard) {
        // Nothing to init. We just do everything in tick() one time.
    }

    @Override
    public AINodeResult tick(T mob, Blackboard<T> blackboard) {

        // Don’t keep doing this every tick. We only run it once.
        if (didCheck) return AINodeResult.FAILURE;
        didCheck = true;

        // Only do this on the server.
        // The server is the “real” source of truth for mob position.
        if (!mob.isServer()) return AINodeResult.FAILURE;

        Level level = mob.getLevel();

        // This only applies to Incursion levels.
        if (!(level instanceof IncursionLevel)) return AINodeResult.FAILURE;

        IncursionLevel incursion = (IncursionLevel) level;

        // The "entrance centre" is the return portal position.
        // If this is null, something is wrong / not generated yet, so bail.
        Point portalPos = incursion.getReturnPortalPosition();
        if (portalPos == null) return AINodeResult.FAILURE;

        float centerX = portalPos.x;
        float centerY = portalPos.y;

        // If we're already close enough to the portal, we count as “in the entrance area”.
        // (Tiles -> pixels. Tiles are 32x32 in Necesse.)
        float openRadiusPx = this.openRadiusTiles * 32.0f;
        if (mob.getDistance(centerX, centerY) <= openRadiusPx) {
            // Already fine, do nothing.
            return AINodeResult.FAILURE;
        }

        // We’re NOT in the entrance area, so we need to move the mob.
        // Look for a valid spot near the portal.
        Point2D.Float dest = findValidTeleportPos(incursion, mob, centerX, centerY, this.searchRadiusTiles);

        // If we found somewhere safe, teleport the mob there.
        if (dest != null) {

            // Stop any current movement so we don't fight with pathing.
            mob.stopMoving();

            // Move instantly.
            // The "true" here is important: it makes sure the move is synced properly.
            mob.setPos(dest.x, dest.y, true);

            // Sounds must be played on clients, not on the server.
            // So we send a packet to nearby clients telling them to play the sound here.
            mob.getLevel().getServer().network.sendToClientsWithEntity(
                    new ExamplePlaySoundPacket(mob.x, mob.y),
                    mob
            );
        }

        // We always return FAILURE here because this leaf isn't meant to "take over" the AI.
        // It's just a one-time fix, then the parent Selector can move on to chase/wander normally.
        return AINodeResult.FAILURE;
    }

    /*
        Find a safe tile near the portal to teleport to.

        We search in "rings" around the centre:
        • r = 0 is the centre tile
        • r = 1 is the tiles touching it
        • r = 2 is the next ring out, etc

        For each tile we check:
        • Not liquid
        • Not shore
        • Mob doesn't collide with the map
        • Mob doesn't collide with another mob/player
     */
    private static Point2D.Float findValidTeleportPos(IncursionLevel level, Mob mob, float centerX, float centerY, int searchRadiusTiles) {
        int centerTileX = GameMath.getTileCoordinate(centerX);
        int centerTileY = GameMath.getTileCoordinate(centerY);

        // Try rings from centre outward until we find something.
        for (int r = 0; r <= searchRadiusTiles; r++) {
            ArrayList<Point> ring = buildRing(centerTileX, centerTileY, r);

            // Shuffle-ish: pick random points so we don't always choose the same spot every time.
            while (!ring.isEmpty()) {
                Point p = ring.remove(GameRandom.globalRandom.nextInt(ring.size()));

                // Don't teleport into water/shore.
                if (level.isLiquidTile(p.x, p.y)) continue;
                if (level.isShore(p.x, p.y)) continue;

                // Convert tile coords to world pixel coords.
                // +16 puts us in the centre of the tile.
                int px = p.x * 32 + 16;
                int py = p.y * 32 + 16;

                // Make sure the mob can actually stand there.
                if (mob.collidesWith(level, px, py)) continue;

                // Also don't land inside another mob/player.
                if (mob.collidesWithAnyMob(level, px, py)) continue;

                // This spot looks good.
                return new Point2D.Float(px, py);
            }
        }

        // Couldn't find a valid spot.
        return null;
    }

    /*
        Build a list of tile points that make up a square "ring" around (cx, cy).

        r = 0: just the centre
        r = 1: the outer edge of a 3x3 square
        r = 2: the outer edge of a 5x5 square
        etc
     */
    private static ArrayList<Point> buildRing(int cx, int cy, int r) {
        ArrayList<Point> points = new ArrayList<>();

        if (r == 0) {
            points.add(new Point(cx, cy));
            return points;
        }

        // Top + bottom edges
        for (int dx = -r; dx <= r; dx++) {
            points.add(new Point(cx + dx, cy - r));
            points.add(new Point(cx + dx, cy + r));
        }

        // Left + right edges (skip corners because top/bottom already added them)
        for (int dy = -r + 1; dy <= r - 1; dy++) {
            points.add(new Point(cx - r, cy + dy));
            points.add(new Point(cx + r, cy + dy));
        }

        return points;
    }
}
