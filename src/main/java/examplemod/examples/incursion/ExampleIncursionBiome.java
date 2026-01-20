package examplemod.examples.incursion;

import necesse.engine.network.server.Server;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.util.TicketSystemList;
import necesse.engine.world.WorldEntity;
import necesse.entity.objectEntity.FallenAltarObjectEntity;
import necesse.inventory.item.Item;
import necesse.inventory.lootTable.LootTable;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.level.maps.IncursionLevel;
import necesse.level.maps.incursion.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * An example incursion biome.
 * This class defines how the incursion behaves: rewards, available objectives,
 * altar appearance, and which level class is used.
 */
public class ExampleIncursionBiome extends IncursionBiome {

    public ExampleIncursionBiome() {
        super("examplebossmob"); // The boss mob string ID for this incursion
    }

    // Items required to be obtained when completing an extraction objective in this incursion
    @Override
    public Collection<Item> getExtractionItems(IncursionData data) {
        return Collections.singleton(ItemRegistry.getItem("exampleore"));
    }

    /**
     * Loot dropped from mobs during hunt-type incursion objectives.
     * This example returns a custom item to demonstrate adding new drops.
     */
    @Override
    public LootTable getHuntDrop(IncursionData incursionData) {
        return new LootTable(
                new ChanceLootItem(0.66F, "examplehuntincursionitem")
        );
    }

    // Defines which incursion types are available and their relative chances
    @Override
    public TicketSystemList<Supplier<IncursionData>> getAvailableIncursions(int tabletTier, IncursionData incursionData) {
        TicketSystemList<Supplier<IncursionData>> system = new TicketSystemList<>();

        // Base ticket weights for each incursion type
        int huntTickets = 100;
        int extractionTickets = 100;

        // Apply modifiers from the previous incursion, if present
        if (incursionData != null) {
            huntTickets = (int) (huntTickets * incursionData.nextIncursionModifiers.getModifier(IncursionDataModifiers.MODIFIER_HUNT_DROPS));
            extractionTickets = (int) (extractionTickets * incursionData.nextIncursionModifiers.getModifier(IncursionDataModifiers.MODIFIER_EXTRACTION_DROPS));
        }

        // Register hunt and extraction incursions with their calculated weights
        system.addObject(huntTickets, () -> new BiomeHuntIncursionData(1.0F, this, tabletTier));
        system.addObject(extractionTickets, () -> new BiomeExtractionIncursionData(1.0F, this, tabletTier));

        return system;
    }

    // Creates a new incursion level instance when players enter through the fallen altar
    @Override
    public IncursionLevel getNewIncursionLevel(FallenAltarObjectEntity altar, LevelIdentifier identifier,
                                               BiomeMissionIncursionData incursion, Server server,
                                               WorldEntity world, AltarData altarData) {
        return new ExampleIncursionLevel(identifier, incursion, world, altarData);
    }

    /**
     * Colors used for the glowing gateway lights on the fallen altar.
     * IncursionBiome requires this method; expected to return list of 6 colors.
     */
    @Override
    public ArrayList<Color> getFallenAltarGatewayColorsForBiome() {
        ArrayList<Color> colors = new ArrayList<>();
        // Repeat colors to satisfy the altar rendering requirements
        colors.add(new Color(181, 80, 120));
        colors.add(new Color(215, 42, 52));
        colors.add(new Color(181, 92, 59));
        colors.add(new Color(181, 80, 120));
        colors.add(new Color(215, 42, 52));
        colors.add(new Color(181, 92, 59));
        return colors;
    }
}
