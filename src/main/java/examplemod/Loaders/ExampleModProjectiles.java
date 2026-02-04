package examplemod.Loaders;

import examplemod.examples.ExampleProjectile;
import necesse.engine.registries.ProjectileRegistry;

public class ExampleModProjectiles {
    public static void load(){
        // Register our projectile
        ProjectileRegistry.registerProjectile("exampleprojectile", ExampleProjectile.class, "exampleprojectile", "exampleprojectile_shadow");
    }
}
