package examplemod.examples.buffs;

import necesse.entity.levelEvent.mobAbilityLevelEvent.MobHealthChangeEvent;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

public class ExampleArrowBuff extends Buff {

    // Heal interval in milliseconds
    private static final int HEAL_INTERVAL_MS = 250;

    public ExampleArrowBuff() {
        this.canCancel = false;
        this.isVisible = false;
        this.isPassive = false;
        this.shouldSave = false;
    }

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.getGndData().setInt("timePassed", 0);
    }
    @Override
    public void serverTick(ActiveBuff buff) {
        Mob m = buff.owner;
        if (m == null) return;

        int heal = buff.getGndData().getInt("healPerTick");
        if (heal <= 0) return;

        int accum = buff.getGndData().getInt("timePassed");
        accum += 50;

        if (accum < HEAL_INTERVAL_MS) {
            buff.getGndData().setInt("timePassed", accum);
            return;
        }

        accum -= HEAL_INTERVAL_MS;
        buff.getGndData().setInt("timePassed", accum);

        int before = m.getHealth();
        int finalHealth = Math.min(m.getMaxHealth(), before + heal);
        int applied = finalHealth - before;

        if (applied > 0) {
            m.getLevel().entityManager.events.add(new MobHealthChangeEvent(m, finalHealth, applied));
        }
    }
}