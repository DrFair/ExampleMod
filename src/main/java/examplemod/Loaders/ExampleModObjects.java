package examplemod.Loaders;

import examplemod.examples.objects.ExampleBaseRockObject;
import examplemod.examples.objects.ExampleObject;
import examplemod.examples.objects.ExampleOreRockObject;
import examplemod.examples.objects.ExampleWallWindowDoorObject;
import necesse.engine.registries.ObjectRegistry;

//NOTE item and crafting categories subject to change
public class ExampleModObjects {

    public static void load(){
        // Register our objects

        ObjectRegistry.registerObject("exampleobject", new ExampleObject()
                .setItemCategory(ExampleModCategories.ROOT_OBJECTS,ExampleModCategories.OBJECTS_COLUMNS)
                .setCraftingCategory(ExampleModCategories.ROOT_OBJECTS,ExampleModCategories.OBJECTS_COLUMNS), 2, true);

        //this wont currently display in creative due to how creative is coded but this is subject to change
        ObjectRegistry.registerObject("exampleobject2", new ExampleObject()
                .setItemCategory(ExampleModCategories.MOD,ExampleModCategories.MOD_OBJECTS)
                .setCraftingCategory(ExampleModCategories.MOD,ExampleModCategories.MOD_OBJECTS), 2, true);
        // Register a rock object
        ExampleBaseRockObject exampleBaseRock = new ExampleBaseRockObject();
        ObjectRegistry.registerObject("examplebaserock", exampleBaseRock, -1.0F, true);

        // Register an ore rock object that overlays onto our incursion rock
        ObjectRegistry.registerObject("exampleorerock", new ExampleOreRockObject(exampleBaseRock), -1.0F, true);

        // Register a wall object, window object and door object
        ExampleWallWindowDoorObject.registerWallsDoorsWindows();
    }
}
