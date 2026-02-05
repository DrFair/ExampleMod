package examplemod.examples.items.tools;

import necesse.entity.mobs.itemAttacker.FollowPosition;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.summonToolItem.SummonToolItem;
import necesse.inventory.lootTable.presets.SummonWeaponsLootTable;

public class ExampleSummonOrbWeapon extends SummonToolItem {
    public ExampleSummonOrbWeapon() {
        // mobStringID, followPosition, summonSpaceTaken, enchantCost, lootTableCategory
        super("examplesummonmob",
                FollowPosition.PYRAMID,
                1.0F,
                400,
                SummonWeaponsLootTable.summonWeapons);

        this.rarity = Item.Rarity.UNCOMMON;

        // This damage is what gets injected into your minion via mob.updateDamage(getAttackDamage(item))
        this.attackDamage.setBaseValue(50.0F).setUpgradedValue(1.0F, 45.0F);

        // Offset the X location of the attack texture
        this.attackXOffset = 15;
        // Offset the X location of the attack texture
        this.attackYOffset = 10;


    }
}