package examplemod.examples;

import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

public class ExampleBuff extends Buff {

    public ExampleBuff() {
        canCancel = true;
        isVisible = true;
        shouldSave = true;
    }

    @Override
    public void init(ActiveBuff activeBuff) {
        // Apply modifiers here
        activeBuff.setModifier(BuffModifiers.SPEED, 0.5f); // +50% speed
    }

    @Override
    public void serverTick(ActiveBuff buff) {
        // You can do server ticks here
    }

    @Override
    public void clientTick(ActiveBuff buff) {
        // You can do client ticks here, like adding particles to buff.owner
    }

}
