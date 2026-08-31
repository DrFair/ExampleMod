package examplemod.examples.items.tools;

import necesse.entity.mobs.itemAttacker.FollowPosition;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.summonToolItem.SummonToolItem;
import necesse.inventory.lootTable.presets.SummonWeaponsLootTable;

public class ExampleOrbSummonWeapon extends SummonToolItem {

    public ExampleOrbSummonWeapon() {
        super(
                "examplesummon", // Mob stringID
                FollowPosition.PYRAMID, // Follow position
                1, // Summon space taken per mob spawned (1 slot)
                400, // Weapon enchant cost
                SummonWeaponsLootTable.summonWeapons // Loot table category (used for incursion drop, etc.)
        );

        rarity = Item.Rarity.UNCOMMON;

        // Base damage: 50, and a tier 1 damage: 55
        attackDamage.setBaseValue(50).setUpgradedValue(1, 55);

        // The offset in pixels of where the player holds the attack texture
        attackXOffset = 15;
        attackYOffset = 10;
    }

}