package examplemod.examples;

import necesse.engine.localization.Localization;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.entity.projectile.Projectile;
import necesse.gfx.GameResources;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.projectileToolItem.magicProjectileToolItem.MagicProjectileToolItem;
import necesse.level.maps.Level;

// Extends MagicProjectileToolItem
public class ExampleProjectileWeapon extends MagicProjectileToolItem {

    // This weapon will shoot out some projectiles.
    // Different classes for specific projectile weapon are already in place that you can use:
    // GunProjectileToolItem, BowProjectileToolItem, BoomerangToolItem, etc.

    public ExampleProjectileWeapon() {
        super(400, null);
        rarity = Rarity.RARE;
        attackAnimTime.setBaseValue(300);
        attackDamage.setBaseValue(20) // Base sword damage
                .setUpgradedValue(1, 110); // Upgraded tier 1 damage
        velocity.setBaseValue(100); // Velocity of projectiles
        knockback.setBaseValue(50); // Knockback of projectiles
        attackRange.setBaseValue(1500); // Range of the projectile

        // Offsets of the attack item sprite relative to the player arm
        attackXOffset = 12;
        attackYOffset = 22;
    }

    @Override
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "examplestafftip"));
        return tooltips;
    }

    @Override
    public void showAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, int animAttack, int seed, GNDItemMap mapContent) {
        if (level.isClient()) {
            // Play magic bolt sound effect with 70% volume, and a random pitch between 100 and 110%
            SoundManager.playSound(GameResources.magicbolt1, SoundEffect.effect(attackerMob)
                    .volume(0.7f)
                    .pitch(GameRandom.globalRandom.getFloatBetween(1.0f, 1.1f)));
        }
    }

    @Override
    public InventoryItem onAttack(Level level, int x, int y, ItemAttackerMob attackerMob, int attackHeight, InventoryItem item, ItemAttackSlot slot, int animAttack, int seed, GNDItemMap mapContent) {
        // This method is ran on the attacking client and on the server.
        // This means we need to tell other clients that a projectile is being added.
        // Every projectile weapon is set to include an integer seed used to make sure that the attacking client
        // and the server gives the projectiles added the same uniqueID.

        // Example we use our example projectile
        Projectile projectile = new ExampleProjectile(
                level, attackerMob, // Level and owner
                attackerMob.x, attackerMob.y, // Start position of projectile
                x, y, // Target position of projectile
                getProjectileVelocity(item, attackerMob), // Will add player buffs, enchantments etc
                getAttackRange(item), // Will add player buffs, enchantments etc
                getAttackDamage(item), // Will add player buffs, enchantments etc
                getKnockback(item, attackerMob) // Will add player buffs, enchantments etc
        );
        // Sync the uniqueID using the given seed
        GameRandom random = new GameRandom(seed);
        projectile.resetUniqueID(random);

        // We can move the projectile 40 units out
        projectile.moveDist(40);


        // Add the projectile without sending it to the local client that is running this as well
        attackerMob.addAndSendAttackerProjectile(projectile);

        // Finally, consume the mana cost
        consumeMana(attackerMob, item);

        // Should return the item after it's been used.
        // Example: if it consumes the item, you can use item.setAmount(item.getAmount() - 1)
        return item;
    }

}
