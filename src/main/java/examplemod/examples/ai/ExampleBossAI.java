package examplemod.examples.ai;

import examplemod.examples.mobs.ExampleBossMob;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.util.gameAreaSearch.GameAreaStream;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.Blackboard;
import necesse.entity.mobs.ai.behaviourTree.composites.SequenceAINode;
import necesse.entity.mobs.ai.behaviourTree.decorators.IsolateRunningAINode;
import necesse.entity.mobs.ai.behaviourTree.leaves.RemoveOnNoTargetNode;
import necesse.entity.mobs.ai.behaviourTree.leaves.TargetFinderAINode;
import necesse.entity.mobs.ai.behaviourTree.util.TargetFinderDistance;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.hostile.bosses.bossAIUtils.AttackStageManagerNode;
import necesse.entity.mobs.hostile.bosses.bossAIUtils.FlyToOppositeDirectionAttackStage;
import necesse.entity.mobs.hostile.bosses.bossAIUtils.FlyToRandomPositionAttackStage;
import necesse.entity.mobs.hostile.bosses.bossAIUtils.IdleTimeAttackStage;

import java.awt.*;

// Extends the SequenceAINode class, which basically is an "AND" parent. Specifically, it does this:
// Run child #1, if it returns FAILURE then stop and return FAILURE.
// If it returns SUCCESS, run the next child until finding one that returns FAILURE
public class ExampleBossAI<T extends ExampleBossMob> extends SequenceAINode<T> {

    public ExampleBossAI() {
        super();

        // We despawn the boss if it has no target for 5 seconds
        addChild(new RemoveOnNoTargetNode<>(TickManager.ticksPerSec * 5));

        // We add a target finder, which looks for all players within a 100 tile radius
        addChild(new TargetFinderAINode<T>(100 * 32) {
            @Override
            public GameAreaStream<? extends Mob> streamPossibleTargets(T mob, Point base, TargetFinderDistance<T> distance) {
                return TargetFinderAINode.streamPlayers(mob, base, distance);
            }
        });

        // Now we add our attack stages. This makes the AI rotate between the attacks in the order they are added
        // and wrap back to the beginning when completed the last stage.
        AttackStageManagerNode<T> attackStages = new AttackStageManagerNode<>();
        // We need to isolate the running node, since we still want the target finder
        // to run while an attack stage returning RUNNING.
        addChild(new IsolateRunningAINode<>(attackStages));

        // We add the different stages in the order we want them to happen

        // Fly/run to a random position within 300 units of the current target.
        // And don't go to next stage until we arrive.
        attackStages.addChild(new FlyToRandomPositionAttackStage<>(true, 300));

        // We idle for ½-2 seconds, based on how low on health we are
        attackStages.addChild(new IdleTimeAttackStage<>(500, 2000));

        // We "charge" the current target by flying to the opposite direction of the target, 200 units away from it
        // No random angle offset
        attackStages.addChild(new ChargeTargetStage());

        // Idle again, based on how low on health we are
        attackStages.addChild(new IdleTimeAttackStage<>(500, 2000));

        // Fly to new position
        attackStages.addChild(new FlyToRandomPositionAttackStage<>(true, 300));

        // Do 3 quick charges in a row, without any idle time
        attackStages.addChild(new ChargeTargetStage());
        attackStages.addChild(new ChargeTargetStage());
        attackStages.addChild(new ChargeTargetStage());

        // Idle again
        attackStages.addChild(new IdleTimeAttackStage<>(500, 2000));

        // Go back to the beginning

    }

    // Here we define the custom attack stages we want to use.
    // This stage class extends FlyToOppositeDirectionAttackStage, which charge the
    // current target by flying to the opposite direction of the target.
    public class ChargeTargetStage extends FlyToOppositeDirectionAttackStage<T> {

        public ChargeTargetStage() {
            super(
                    true, // We don't skip to the next stage before we have arrived
                    250, // How many units in opposite direction
                    0 // No random angle offset
            );
        }

        @Override
        public void onStarted(T mob, Blackboard<T> blackboard) {
            super.onStarted(mob, blackboard);
            // Play the charge sound for all clients
            mob.chargeSoundAbility.runAndSend();
            // We give a movespeed burst buff for 5 seconds when we start the charge
            mob.buffManager.addBuff(new ActiveBuff(BuffRegistry.MOVE_SPEED_BURST, mob, 5f, null), true);
        }

        @Override
        public void onEnded(T mob, Blackboard<T> blackboard) {
            super.onEnded(mob, blackboard);
            // When we end this attack stage, we remove the move speed burst buff
            mob.buffManager.removeBuff(BuffRegistry.MOVE_SPEED_BURST, true);
        }
    }

}
