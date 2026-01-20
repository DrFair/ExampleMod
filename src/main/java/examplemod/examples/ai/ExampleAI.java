package examplemod.examples.ai;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.composites.SelectorAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.CollisionChaserAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.WandererAINode;
import necesse.entity.mobs.ai.behaviourTree.trees.CollisionPlayerChaserAI;

public class ExampleAI<T extends Mob> extends SelectorAINode<T> {
   public final ExampleAILeaf<T> teleporter;

    /*
     This node handles "find player -> chase -> try to attack"
     We keep a reference so we can use its damage/knockback settings later.
    */
    public final CollisionPlayerChaserAI<T> chaser;

    // This node handles "walk around randomly" when there's nothing to chase.
    public final WandererAINode<T> wanderer;

    public ExampleAI(int searchDistance, GameDamage damage, int knockback, int wanderFrequency) {
        /*
         A SelectorAINode tries its children in order.
         First one that can run/works is the behaviour the mob uses.

         So: we add CHASING first, because we want chasing to "win" whenever possible.
        */


        this.teleporter = new ExampleAILeaf<>(8, 10);
        {
            // 8 tiles = 256px open-space check, search within 10 tiles if it needs to move.
        }
        addChild(this.teleporter);
        this.chaser = new CollisionPlayerChaserAI<T>(searchDistance, damage, knockback) {

            // CollisionPlayerChaserAI has an attackTarget method it calls when in range.
            // We override it so we can route the attack logic to OUR method below.
            @Override
            public boolean attackTarget(T mob, Mob target) {
                // "ExampleAI.this" means "the outer ExampleAI instance"
                // (because we're inside an anonymous inner class right now).
                return ExampleAI.this.attackTarget(mob, target);
            }
        };
        addChild(this.chaser);

        /*
         If the chaser doesn't have a target (or can't chase),
         the selector will try the next child: wandering.
        */
        this.wanderer = new WandererAINode<>(wanderFrequency);
        addChild(this.wanderer);
    }

    /*
     attack logic.
     We keep it outside the chaser node so it's easy to change later.
    */
    public boolean attackTarget(T mob, Mob target) {
        /*
         simpleAttack is a helper that does a basic melee attack:
         - checks if the mob can attack right now
         - applies damage
         - applies knockback
         - returns true if an attack happened
        */
        return CollisionChaserAINode.simpleAttack(
                mob,
                target,
                // Use the damage/knockback values that were passed into the chaser constructor.
                this.chaser.damage,
                this.chaser.knockback
        );
    }
}
