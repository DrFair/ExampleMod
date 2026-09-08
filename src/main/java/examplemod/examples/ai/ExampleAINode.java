package examplemod.examples.ai;

import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.AINode;
import necesse.entity.mobs.ai.behaviourTree.AINodeResult;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.entity.mobs.ai.behaviourTree.event.AIEvent;
import necesse.entity.projectile.Projectile;

import java.awt.*;
import java.util.ArrayList;

/**
 * This is essentially a recreation of existing "TeleportOnProjectileHitAINode" class.
 * Simply to explain and comment a bit more on what's going on for learning purposes :)
 * Always returns SUCCESS
 */
public abstract class ExampleAINode<T extends Mob> extends AINode<T> {

    // The next time the mob can teleport away
    protected long nextTeleportTime;

    protected int tileRadius = 5;

    @Override
    protected void onRootSet(AINode<T> root, T mob, Blackboard<T> blackboard) {
        // Runs exactly one time when this node was added to an AI
        blackboard.onBeforeHit(e -> {
            if (mob.isClient()) return;
            // If hit by a projectile, not on cooldown, and we teleported to a new position
            if (e.event.attacker instanceof Projectile && nextTeleportTime <= mob.getTime() && findNewPosition(mob)) {
                // Prevent the hit and don't show any damage number or play hit sound
                e.event.prevent();
                e.event.showDamageTip = false;
                e.event.playHitSound = false;
                nextTeleportTime = mob.getTime() + 10000; // 10 seconds cooldown
                // We submit a path reset event to all other AI nodes
                blackboard.submitEvent("resetPathTime", new AIEvent());
            }
        });
    }

    public boolean findNewPosition(T mob) {
        // First we get the center tile of the mob
        int tileX = mob.getTileX();
        int tileY = mob.getTileY();
        // Move offset is used for larger mobs, to get the center position of
        // where this mob should move relative to a target tile
        Point moveOffset = mob.getPathMoveOffset();

        // Next we iterate over all tiles in the defined tile radius
        ArrayList<Point> possiblePositions = new ArrayList<>();
        for (int x = tileX - tileRadius; x <= tileX + tileRadius; x++) {
            for (int y = tileY - tileRadius; y <= tileY + tileRadius; y++) {
                int mobX = x * 32 + moveOffset.x;
                int mobY = y * 32 + moveOffset.y;
                // If the mob does not collide with the level at this position,
                // we add it to the list of possible teleport positions
                if (!mob.collidesWith(mob.getLevel(), mobX, mobY)) {
                    possiblePositions.add(new Point(mobX, mobY));
                }
            }
        }

        // Next we randomly select a position from the list of possible positions and attempt to teleport there
        while (!possiblePositions.isEmpty()) {
            int index = GameRandom.globalRandom.nextInt(possiblePositions.size());
            Point point = possiblePositions.remove(index);
            if (teleport(mob, point.x, point.y)) {
                return true;
            }
        }

        // If we didn't find a position, return false
        return false;
    }

    public abstract boolean teleport(T mob, int x, int y);

    @Override
    public void init(T mob, Blackboard<T> blackboard) {
        // Runs every tick when running the parent AI node.
        // Here we can reset something that is running in tick method, if we want to.
    }

    @Override
    public AINodeResult tick(T mob, Blackboard<T> blackboard) {
        return AINodeResult.SUCCESS; // Always return SUCCESS
    }

}