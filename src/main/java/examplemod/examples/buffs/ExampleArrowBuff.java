package examplemod.examples.buffs;

import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.gfx.gameFont.FontOptions;
import necesse.level.maps.hudManager.floatText.DamageText;

import java.awt.*;

public class ExampleArrowBuff extends Buff {

    public ExampleArrowBuff() {
        canCancel = false;
        isVisible = false;
        shouldSave = true;
    }

    @Override
    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        // No modifiers to set in the buff
    }

    @Override
    public void clientTick(ActiveBuff buff) {
        super.clientTick(buff);
        // We run the tickHealing in both client and server game ticks
        tickHealing(buff);
    }

    @Override
    public void serverTick(ActiveBuff buff) {
        super.serverTick(buff);
        // We run the tickHealing in both client and server game ticks
        tickHealing(buff);
    }

    public void tickHealing(ActiveBuff buff) {
        // We get the health given per game tick
        float healthPerGameTick = buff.getGndData().getFloat("healthPerGameTick");

        // In case the health given per tick is lower than 1, we need to accumulate it over time until
        // we can heal at least 1 health point. We do this in the form of a buffer

        // First we get what the buffer is right now (from previous ticks)
        float healBuffer = buff.getGndData().getFloat("nextHealBuffer");
        // Next we add the health this tick
        healBuffer += healthPerGameTick;

        // If we have more than 1 health to give, we do it
        if (healBuffer >= 1) {
            // We get the lowest value of the healAmount
            int healAmount = (int) Math.floor(healBuffer);

            // Calculate the new health of the owner
            Mob owner = buff.owner;

            // Update the health
            owner.setHealth(owner.getHealth() + healAmount, buff.getAttacker());

            // Show the green heal text:
            // Font options is what the font should look like
            FontOptions fontOptions = new FontOptions(12)
                    .outline() // It should have an outline
                    .color(Color.GREEN); // It should be green

            // The text goes up by a random amount
            int heightIncrease = GameRandom.globalRandom.getIntBetween(25, 45);

            // And we add the text to the levels hud manager
            owner.getLevel().hudManager.addElement(new DamageText(owner, healAmount, fontOptions, heightIncrease));

            // Reduce the buffer
            healBuffer -= healAmount;
        }

        // Finally, save back the buffer amount for next tick
        buff.getGndData().setFloat("nextHealBuffer", healBuffer);
    }

}