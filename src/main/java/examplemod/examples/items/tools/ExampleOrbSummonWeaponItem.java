package examplemod.examples.items.tools;

import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.itemAttacker.FollowPosition;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.summonToolItem.SummonToolItem;
import necesse.inventory.lootTable.presets.SummonWeaponsLootTable;

public class ExampleOrbSummonWeaponItem extends SummonToolItem {

    public ExampleOrbSummonWeaponItem() {
        super(
                "examplesummon", // Mob stringID
                FollowPosition.PYRAMID, // Follow position
                1, // Summon space taken per mob spawned (1 slot)
                ItemRegistry.EQUIPMENT_VALUE_GOLD, // Weapon enchant cost
                SummonWeaponsLootTable.summonWeapons // Loot table category (used for incursion drop, etc.)
        );

        rarity = Item.Rarity.UNCOMMON;

        // Base damage: 15, and a tier 1 damage: 35
        attackDamage.setBaseValue(15).setUpgradedValue(1, 35);

        // Offsets of the attack item sprite relative to the player arm
        attackXOffset = 15;
        attackYOffset = 10;
    }

}