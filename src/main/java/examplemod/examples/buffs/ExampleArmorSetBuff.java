package examplemod.examples.buffs;

import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.setBonusBuffs.SimpleSetBonusBuff;

public class ExampleArmorSetBuff extends SimpleSetBonusBuff {
    public ExampleArmorSetBuff() {
        super(
                new ModifierValue<>(BuffModifiers.ALL_DAMAGE, 0.10f),
                new ModifierValue<>(BuffModifiers.SPEED, 0.10f)
        );
    }

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        this.canCancel = false;
        this.isPassive = true;
        this.isVisible = true;
        super.init(buff, eventSubscriber);
    }
}
