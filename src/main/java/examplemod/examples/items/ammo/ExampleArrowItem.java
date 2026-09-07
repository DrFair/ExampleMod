package examplemod.examples.items.ammo;

import necesse.engine.registries.ProjectileRegistry;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.entity.projectile.Projectile;
import necesse.inventory.item.arrowItem.ArrowItem;

public class ExampleArrowItem extends ArrowItem {

    public ExampleArrowItem() {
        super(5000); // Stack size like vanilla arrows

        // Here we set the stats that is added to the bow when using this arrow
        // In our case, the arrow actually hits friendlies and heals instead (see ExampleArrowProjectile)
        // It heals based on the damage, so these stats still does something

        damage = 8; // Adds +8 damage to the bows base damage
        armorPen = 2; // Adds +2 armor pen
        critChance = 0.05f; // +5% crit chance
        speedMod = 1.10f; // 10% faster arrow velocity
    }

    @Override
    public Projectile getProjectile(float x, float y, float targetX, float targetY,
                                    float velocity, int range, GameDamage damage, int knockback,
                                    ItemAttackerMob owner) {
        return ProjectileRegistry.getProjectile(
                "examplearrowprojectile", // Projectile stringID that the arrow shoots
                owner.getLevel(),
                x, y, targetX, targetY,
                velocity, range,
                damage, knockback,
                owner
        );
    }
}
