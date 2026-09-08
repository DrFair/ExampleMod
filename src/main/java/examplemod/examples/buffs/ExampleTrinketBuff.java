package examplemod.examples.buffs;

import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.armorBuffs.trinketBuffs.SimpleTrinketBuff;

/**
 * Trinket buff:
 * When a player wears the trinket, this buff is applied
 */
public class ExampleTrinketBuff extends SimpleTrinketBuff {

    public ExampleTrinketBuff(){
    }

    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        // Apply modifiers here

        // Gives spelunker buff, which lights up ores
        activeBuff.setModifier(BuffModifiers.SPELUNKER,true);
    }

}