package examplemod.examples.settlement.settlers;

import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
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
        // Use an existing icon for now, or add your own under mobs/icons/
        this.texture = GameTexture.fromFile("mobs/icons/human");
    }

    @Override
    public GameMessage getAcquireTip() {
        return new LocalMessage("settlement", "foundinvillagetip");
    }

    @Override
    public void addNewRecruitSettler(ServerSettlementData data, boolean isRandomEvent,
                                     TicketSystemList<Supplier<HumanMob>> ticketSystem) {
        // Weight controls how often they appear as recruits
        ticketSystem.addObject(isRandomEvent ? 50 : 25, getNewRecruitMob(data));
    }
}
