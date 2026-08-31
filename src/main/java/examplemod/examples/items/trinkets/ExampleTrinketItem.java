package examplemod.examples.items.trinkets;

import necesse.engine.localization.Localization;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.trinketItem.SimpleTrinketItem;
import necesse.inventory.lootTable.presets.TrinketsLootTable;

// Extends SimpleTrinketItem
public class ExampleTrinketItem extends SimpleTrinketItem {

    public ExampleTrinketItem() {
        super(
                Rarity.UNCOMMON, // Rarity
                "exampletrinketbuff", // The buffs stringID that it gives
                400, // Enchant cost
                TrinketsLootTable.trinkets // Loot table category
        );
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);

        // Add our custom tooltip
        tooltips.add(Localization.translate("itemtooltip", "exampletrinkettip"));

        return tooltips;
    }
}