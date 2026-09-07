package examplemod.examples.ai;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.composites.SelectorAINode;
import necesse.entity.mobs.ai.behaviourTree.decorators.InverterAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.WandererAINode;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionPlayerChaserAI;

// Extends the SelectorAINode class, which basically is an "OR" parent. Specifically, it does this:
// Run child #1, if it returns SUCCESS then stop and return SUCCESS.
// If it returns FAILURE, run the next child until finding one that returns SUCCESS
public abstract class ExampleAI<T extends Mob> extends SelectorAINode<T> {

    // We store the different child nodes in variables, so that we can easily access them later if needed

    // Plays a sound when then boss appears
    public final ExampleAINode<T> soundPlay;

    // AI that does: find target -> chase -> when colliding with the target, call attackTarget().
    // In this case, attackTarget call simply damages the target. This can be overridden for something custom.
    public final CollisionPlayerChaserAI<T> chaser;

    // “walk around randomly” node. This is what happens when there’s no target to chase.
    public final WandererAINode<T> wanderer;

    public ExampleAI(int searchDistance, GameDamage damage, int knockback, int wanderFrequency) {
        // This AI is pretty similar to CollisionPlayerChaserWandererAI,
        // but with added teleport mechanic and no escape node.

        // 1) Teleport / reposition leaf (highest priority).
        // Since it always returns SUCCESS, we use inverter node to invert it to FAILURE
        addChild(new InverterAINode<>(soundPlay = new ExampleAINode<T>() {
            @Override
            public boolean teleport(T mob, int x, int y) {
                return ExampleAI.this.teleport(mob, x, y);
            }
        }));

        // 2) Chase + attack (second priority).
        addChild(chaser = new CollisionPlayerChaserAI<T>(searchDistance, damage, knockback));

        // 3) Wander around if we aren’t teleporting, and we aren’t chasing anyone (last priority)
        addChild(wanderer = new WandererAINode<>(wanderFrequency));
    }

    public abstract boolean teleport(T mob, int x, int y);

}
