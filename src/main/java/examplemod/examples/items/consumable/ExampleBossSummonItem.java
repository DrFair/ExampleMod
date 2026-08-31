package examplemod.examples.items.consumable;

import examplemod.examples.maps.biomes.ExampleBiome;
import necesse.engine.localization.Localization;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketChatMessage;
import necesse.engine.registries.MobRegistry;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.engine.util.LevelIdentifier;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.placeableItem.consumableItem.ConsumableItem;
import necesse.level.maps.IncursionLevel;
import necesse.level.maps.Level;

import java.awt.geom.Line2D;

/**
 * A consumable item that summons our boss mob.
 */
public class ExampleBossSummonItem extends ConsumableItem {

    public ExampleBossSummonItem() {
        // Stack size 1, is "single use" consumable behaviour
        super(1, true);

        // Cooldown (ms) before you can use it again
        itemCooldownTime.setBaseValue(2000);

        // Where it appears in the creative menu
        setItemCategory("consumable", "bossitems");

        // If the player dies, drop this like a material (depending on death penalty rules)
        dropsAsMatDeathPenalty = true;

        // Search keywords (helps with the in-game search)
        keyWords.add("boss");

        // Item rarity / color
        rarity = Item.Rarity.LEGENDARY;

        // How big the item sprite is when dropped in the world
        worldDrawSize = 32;

        // How long it takes the incinerator to destroy this item
        incinerationTimeMillis = 30_000;
    }

    /**
     * Checks if the item is allowed to be used here.
     */
    public String canPlace(Level level, int x, int y, PlayerMob player,
                           Line2D playerPositionLine, InventoryItem item, GNDItemMap mapContent) {
        int tileX;
        int tileY;

        // Don't allow boss summoning inside an incursion (special dungeon-like levels)
        if (level instanceof IncursionLevel) {
            return "inincursion";
        }

        // Only allow use in caves (not surface)
        if (!level.isCave) {
            return "notcave";
        }

        // Figure out which tile we should check.
        // If we have a player, use the player's tile.
        // If not (rare cases), convert the clicked pixel coords into tile coords.
        if (player == null) {
            tileX = GameMath.getTileCoordinate(x);
            tileY = GameMath.getTileCoordinate(y);
        } else {
            tileX = player.getTileX();
            tileY = player.getTileY();
        }

        // Only allow in *cave identifier* AND only if the biome at that tile is our ExampleBiome.
        // This prevents using the item in other cave biomes.
        if (!level.getIdentifier().equals(LevelIdentifier.CAVE_IDENTIFIER)
                || !(level.getBiome(tileX, tileY) instanceof ExampleBiome))
            return "notexamplebiome";

        // Allowed
        return null;
    }

    /**
     * Runs when the player tries to use the item but canPlace(...) returned an error.
     * This is where we can send a nicer message to the player.
     */
    public InventoryItem onAttemptPlace(Level level, int x, int y, PlayerMob player,
                                        InventoryItem item, GNDItemMap mapContent, String error) {

        // Only do chat messages on the server, and only if we have a real server client player
        if (level.isServer() && player != null && player.isServerClient() && error.equals("inincursion")) {
            player.getServerClient().sendChatMessage(new LocalMessage("misc", "cannotsummoninincursion"));
        }

        // Let vanilla handle the rest (cooldowns, failure behavior, etc.)
        return super.onAttemptPlace(level, x, y, player, item, mapContent, error);
    }

    /**
     * Runs when the item is successfully used.
     * This is where we actually spawn the boss.
     */
    public InventoryItem onPlace(Level level, int x, int y, PlayerMob player,
                                 int seed, InventoryItem item, GNDItemMap mapContent) {

        // Only spawn mobs on the server (clients are just visuals)
        if (level.isServer()) {
            // Simple debug log
            System.out.println("Example Boss Mob has been summoned at " + level.getIdentifier() + ".");

            // Pick a random direction (angle 0-359 degrees)
            int angle = GameRandom.globalRandom.nextInt(360);

            // Turn that angle into a unit direction vector (nx, ny)
            float nx = GameMath.cos(angle);
            float ny = GameMath.sin(angle);

            // How far away from the player the boss should appear (in pixels)
            float distance = 16 * 32; // 16 tiles

            // Create the boss mob instance
            Mob mob = MobRegistry.getMob("exampleboss", level);

            // Spawn it near the player, at a random offset
            level.entityManager.addMob(
                    mob,
                    (player.getX() + (int) (nx * distance)),
                    (player.getY() + (int) (ny * distance))
            );

            // Tell nearby clients (chat message) that the boss was summoned
            level.getServer().network.sendToClientsWithEntity(
                    new PacketChatMessage(new LocalMessage("misc", "bosssummon", "name", mob.getLocalization())),
                    mob
            );
        }

        // If this item is single-use, consume 1 from the stack
        if (isSingleUse(player)) {
            item.setAmount(item.getAmount() - 1);
        }

        return item;
    }

    /**
     * Extra tooltip line shown on the item.
     */
    public ListGameTooltips getTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "examplebosssummontip"));
        return tooltips;
    }

    /**
     * The "type name" shown in the UI (e.g. Relic).
     */
    public String getTranslatedTypeName() {
        return Localization.translate("item", "relic");
    }
}