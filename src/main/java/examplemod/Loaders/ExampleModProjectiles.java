package examplemod.Loaders;

import examplemod.examples.projectiles.ExampleArrowProjectile;
import examplemod.examples.projectiles.ExampleProjectile;
import necesse.engine.registries.ProjectileRegistry;

public class ExampleModProjectiles {
    public static void load(){
        // Register our projectile
        ProjectileRegistry.registerProjectile("exampleprojectile", ExampleProjectile.class, "exampleprojectile", "exampleprojectile_shadow");

        // Register our arrow projectile
        ProjectileRegistry.registerProjectile("examplearrowprojectile", ExampleArrowProjectile.class, "examplearrowprojectile","arrow_shadow");
    }
}
