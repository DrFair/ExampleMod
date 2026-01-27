package examplemod.Loaders;

import examplemod.examples.objects.ExampleBaseRockObject;
import examplemod.examples.objects.ExampleObject;
import examplemod.examples.objects.ExampleOreRockObject;
import examplemod.examples.objects.ExampleWallWindowDoorObject;
import necesse.engine.registries.ObjectRegistry;

public class ExampleModObjects {

    public static void load(){
        // Register our objects
        ObjectRegistry.registerObject("exampleobject", new ExampleObject(), 2, true);

        // Register a rock object
        ExampleBaseRockObject exampleBaseRock = new ExampleBaseRockObject();
        ObjectRegistry.registerObject("examplebaserock", exampleBaseRock, -1.0F, true);

        // Register an ore rock object that overlays onto our incursion rock
        ObjectRegistry.registerObject("exampleorerock", new ExampleOreRockObject(exampleBaseRock), -1.0F, true);

        // Register a wall object, window object and door object
        ExampleWallWindowDoorObject.registerWallsDoorsWindows();
    }
}
