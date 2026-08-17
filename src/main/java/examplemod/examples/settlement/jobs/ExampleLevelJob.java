package examplemod.examples.settlement.jobs;

import necesse.engine.localization.message.LocalMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.save.LoadData;
import necesse.entity.ObjectDamageResult;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.entity.mobs.job.*;
import necesse.entity.mobs.job.activeJob.MineObjectActiveJob;
import necesse.level.maps.LevelObject;
import necesse.level.maps.levelData.jobs.MineObjectLevelJob;
import necesse.level.maps.levelData.jobs.TileLevelJob;

/**
 * A simple settlement job:
 * "Go to this tile and clear the grass object there."
 * We extend MineObjectLevelJob because Necesse already has a job type for
 * destroying an object at a tile.
 */
public class ExampleLevelJob extends MineObjectLevelJob {

    // Create a new job at a tile position
    public ExampleLevelJob(int tileX, int tileY) {
        super(tileX, tileY);
    }

    // Create a job from saved data (not used if shouldSave() returns false)
    public ExampleLevelJob(LoadData save) {
        super(save);
    }

    @Override
    public boolean isValid() {
        // Use the base checks (it will call isValidObject on the current object)
        return super.isValid();
    }

    @Override
    public boolean isValidObject(LevelObject object) {
        // Do NOT let settlers clear objects that a player placed.
        if (getLevel().objectLayer.isPlayerPlaced(this.tileX, this.tileY)) return false;

        // Only allow this job to target grass objects.
        return object.object != null && object.object.isGrass;
    }

    @Override
    public boolean isSameJob(TileLevelJob other) {
        // When adding a job to a tile, the game checks this to see if another job already exists that is the same.
        // The super method checks for job ID/type and the tile. If you had anything custom to check for,
        // we should do it here.
        return super.isSameJob(other);
    }

    @Override
    public boolean shouldSave() {
        // Don't save this job. The settlement can recreate it later if needed.
        return false;
    }

    /**
     * This builds the actual steps the settler will do.
     * Here we only add ONE step: mine/destroy the grass object.
     */
    public static <T extends ExampleLevelJob> JobSequence getJobSequence(
            EntityJobWorker worker, FoundJob<T> foundJob
    ) {
        // Get the current object at the job tile (might be null if it changed)
        LevelObject target = foundJob.job.getObject();

        // Message shown for the job (in settlement UI)
        LocalMessage msg = new LocalMessage(
                "activities",
                "examplejob",
                "target",
                (target != null && target.object != null)
                        ? target.object.getLocalization()
                        : new StaticMessage("N/A")
        );

        // A list of work steps
        GameLinkedListJobSequence sequence = new GameLinkedListJobSequence(msg, false);

        // Add the work step: go to tile + hit the object until it breaks
        sequence.add(new MineObjectActiveJob(
                worker,
                foundJob.priority,
                foundJob.job.tileX,
                foundJob.job.tileY,
                // Keep working only while the job still exists AND the object is still valid grass
                lo -> (!foundJob.job.isRemoved() && foundJob.job.isValidObject(lo)),
                foundJob.job.reservable, // Reservation (stops 2 settlers trying to do the same tile)
                "farmingscythe", // Item used for the "swing" animation (visual only)
                5, // Damage per hit to the object
                250, // Time per swing (ms)
                0 // Extra delay between swings (ms)
        ) {
            @Override
            public void onObjectDestroyed(ObjectDamageResult result) {
                // Once done with destroying the object, add pickup jobs for any drops that happened
                addItemPickupJobs(foundJob.priority, result, sequence);

                // Remove the job so it doesn't stay posted
                foundJob.job.remove();
            }
        });

        return sequence;
    }

    // The default handler for the job, used when registering the job
    public static JobTypeHandler.SubHandler<ExampleLevelJob> handler(EntityJobWorker worker, JobTypeHandler handler) {
        if (worker instanceof HumanMob) {
            HumanMob humanMob = (HumanMob) worker;

            // We register a job type sub handler for the ExampleLevelJob. And use our sequence getter from above
            JobTypeHandler.SubHandler<ExampleLevelJob> subHandler = handler.setJobHandler(
                    ExampleLevelJob.class,
                    (foundJob) -> getJobSequence(humanMob, foundJob)
            );

            // Here we define when the worker should be able to do this job
            subHandler.setPredicate(() -> {
                // Don't do the job if they are on strike. There are some jobs (like eating), they would
                // still do while they're on strike
                if (humanMob.isOnStrike()) return false;

                // Don't do the job if they have currently completed a mission as are
                // waiting for player pickup (like a Miners mining trip)
                if (humanMob.hasCompletedMission()) return false;

                // Don't do the job if they are a settler and not within their settlement. Like on adventure party, etc.
                if (humanMob.isSettler() && !humanMob.isSettlerWithinSettlement()) return false;

                // Don't do the job if they have a full inventory. The parameter is if the settler inventory full
                // notification should be sent to the players
                if (humanMob.isInventoryFull(true)) return false;

                // No other checks, return true if successful
                return true;
            });

            return subHandler;
        } else {
            // If the worker is not a human, we don't do the job.
            return null;
        }
    }

}