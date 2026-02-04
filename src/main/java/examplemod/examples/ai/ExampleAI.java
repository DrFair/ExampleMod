package examplemod.examples.ai;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.composites.SelectorAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.CollisionChaserAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.WandererAINode;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionPlayerChaserAI;

public class ExampleAI<T extends Mob> extends SelectorAINode<T> {

    // My custom “fix spawn position” leaf.
    // This runs first so the mob gets put somewhere sensible before doing anything else.
    public final ExampleAILeaf<T> teleporter;

    // Vanilla AI that does: find player -> chase -> when close enough, call attackTarget().
    // We keep it as a field so we can reuse the damage/knockback values from it.
    public final CollisionPlayerChaserAI<T> chaser;

    // Vanilla “walk around randomly” node. This is what happens when there’s no player to chase.
    public final WandererAINode<T> wanderer;

    public ExampleAI(int searchDistance, GameDamage damage, int knockback, int wanderFrequency) {

        // A Selector is basically: "try child #1, if it can run then use it,
        // otherwise try child #2, otherwise child #3..."
        //
        // So the ORDER we add children is the ORDER of priority.

        // 1) Teleport / reposition leaf (highest priority).
        // (In my leaf: 8 tiles = how far to check for open space, 10 tiles = how far to search for a valid spot)
        this.teleporter = new ExampleAILeaf<>(8, 10);
        addChild(this.teleporter);

        // 2) Chase + attack (second priority).
        this.chaser = new CollisionPlayerChaserAI<T>(searchDistance, damage, knockback) {

            // The chaser decides WHEN it should attack, but it asks us HOW to attack.
            // So we override this and forward it to our own method below.
            @Override
            public boolean attackTarget(T mob, Mob target) {
                return ExampleAI.this.attackTarget(mob, target);
            }
        };
        addChild(this.chaser);

        // 3) Wander around if we aren’t teleporting, and we aren’t chasing anyone.
        this.wanderer = new WandererAINode<>(wanderFrequency);
        addChild(this.wanderer);
    }

    // This is the “how to attack” part used by the chaser above.
    // Keeping it here makes it easy to change later (special attacks, effects, etc).
    public boolean attackTarget(T mob, Mob target) {

        // simpleAttack is the vanilla helper for a basic melee hit.
        // It handles cooldown/range stuff internally and returns true if an attack happened.
        return CollisionChaserAINode.simpleAttack(
                mob,
                target,
                this.chaser.damage,     // use the damage we configured in the chaser
                this.chaser.knockback   // use the knockback we configured in the chaser
        );
    }
}
