package examplemod.examples;

import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.entity.mobs.friendly.critters.RabbitMob;
import net.bytebuddy.asm.Advice;

/**
 * Intercepts a constructor
 * Check out ExampleMethodPatch class for some documentation
 */
@ModConstructorPatch(target = RabbitMob.class, arguments = {}) // No arguments
public class ExampleConstructorPatch {

    /*
        Other than printing a debug message, this is currently set up to change
        the speed of all rabbits to 60 (double of normal).
     */

    @Advice.OnMethodExit
    static void onExit(@Advice.This RabbitMob rabbitMob) {
        rabbitMob.setSpeed(60);
        // Debug message to know it's working
        System.out.println("Exited RabbitMob constructor: " + rabbitMob.getStringID());
    }

}
