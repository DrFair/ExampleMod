package examplemod.examples.items.ammo;

import necesse.engine.registries.ProjectileRegistry;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.entity.projectile.Projectile;
import necesse.inventory.item.arrowItem.ArrowItem;

public class ExampleArrowItem extends ArrowItem {

    public ExampleArrowItem() {
        super(5000); // stack size like vanilla arrows

        // These fields are on ArrowItem (public), and BowProjectileToolItem uses them via modDamage/modVelocity.
        this.damage = 8;          // adds +8 damage to the bow’s base damage
        this.armorPen = 2;        // adds armor pen
        this.critChance = 0.05f;  // +5% base crit
        this.speedMod = 1.10f;    // 10% faster arrow velocity
    }

    @Override
    public Projectile getProjectile(float x, float y, float targetX, float targetY,
                                    float velocity, int range, GameDamage damage, int knockback,
                                    ItemAttackerMob owner) {

        // Same exact pattern as StoneArrowItem / IronArrowItem, etc.
        return ProjectileRegistry.getProjectile(
                "examplearrowprojectile",           // your projectile stringID
                owner.getLevel(),
                x, y, targetX, targetY,
                velocity, range,
                damage, knockback,
                (Mob) owner
        );
    }
}
