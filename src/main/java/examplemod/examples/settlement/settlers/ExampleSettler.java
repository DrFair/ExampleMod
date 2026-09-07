package examplemod.examples.settlement.settlers;

import necesse.engine.localization.message.GameMessage;
import necesse.engine.util.TicketSystemList;
import necesse.entity.mobs.friendly.human.HumanMob;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.levelData.settlementData.ServerSettlementData;
import necesse.level.maps.levelData.settlementData.settler.Settler;

import java.util.function.Supplier;

public class ExampleSettler extends Settler {

    public ExampleSettler() {
        super("examplehuman"); // Must match the human mob registered stringID in ExampleModMobs
    }

    @Override
    public void loadTextures() {
        // By default, this method loads from "mobs/icons/<stringID>".
        // In our case we just want the existing human icon:
        texture = GameTexture.fromFile("mobs/icons/human");
    }

    @Override
    public GameMessage getAcquireTip() {
        // If we want a tooltip showing how you can find this settler
        // We don't spawn ours in villages, so no tooltip here
//        return new LocalMessage("settlement", "foundinvillagetip");
        return null;
    }

    @Override
    public void addNewRecruitSettler(ServerSettlementData data, boolean isRandomEvent,
                                     TicketSystemList<Supplier<HumanMob>> ticketSystem) {
        // Weight controls how often they appear as recruits

        // If conditions are met, every second visitor are guaranteed to be a recruit (not exotic merchant or pawnbroker)
        // When this is the case, isRandomEvent is set to false. All other cases it's true
        ticketSystem.addObject(isRandomEvent ? 50 : 25, getNewRecruitMob(data));
    }
}
