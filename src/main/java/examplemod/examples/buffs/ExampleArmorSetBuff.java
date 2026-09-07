package examplemod.examples.buffs;

import necesse.engine.modifiers.ModifierValue;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.setBonusBuffs.SimpleSetBonusBuff;

/**
 * Set bonus buff:
 * When a player wears the full armor set, this buff is applied
 */
public class ExampleArmorSetBuff extends SimpleSetBonusBuff {

    public ExampleArmorSetBuff() {
        // The parent class (SimpleSetBonusBuff) takes the stat boosts here.
        super(
                new ModifierValue<>(BuffModifiers.ALL_DAMAGE, 0.10f), // +10% damage
                new ModifierValue<>(BuffModifiers.SPEED, 0.10f) // +10% speed
        );
    }

}