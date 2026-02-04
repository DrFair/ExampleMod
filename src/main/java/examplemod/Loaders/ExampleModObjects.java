package examplemod.Loaders;

import examplemod.examples.objects.*;
import necesse.engine.registries.ObjectRegistry;

//NOTE item and crafting categories subject to change
public class ExampleModObjects {

    public static void load(){
        // Register our objects

        ObjectRegistry.registerObject("exampleobject", new ExampleObject()
                .setItemCategory(ExampleModCategories.ROOT_OBJECTS,ExampleModCategories.OBJECTS_COLUMNS)
                .setCraftingCategory(ExampleModCategories.ROOT_OBJECTS,ExampleModCategories.OBJECTS_COLUMNS), 2, true);


        // Register a rock object
        ExampleBaseRockObject exampleBaseRock = new ExampleBaseRockObject();
        ObjectRegistry.registerObject("examplebaserock", exampleBaseRock, -1.0F, true);

        // Register an ore rock object that overlays onto our incursion rock
        ObjectRegistry.registerObject("exampleorerock", new ExampleOreRockObject(exampleBaseRock), -1.0F, true);

        // Register a wall object, window object and door object
        ExampleWallWindowDoorObject.registerWallsDoorsWindows();

        // Register a tree object
        ObjectRegistry.registerObject("exampletree",new ExampleTreeObject(),0.0F,false,false,false);

        // Register a sapling object
        ObjectRegistry.registerObject("examplesapling", new ExampleTreeSaplingObject(),10,true);

        // Register a furnature object this won't currently display in creative due to how creative is coded but this is subject to change
        ObjectRegistry.registerObject("examplechair", new ExampleWoodChairObject()
                .setItemCategory(ExampleModCategories.MOD,ExampleModCategories.MOD_OBJECTS,ExampleModCategories.EXAMPLEWOOD)
                .setCraftingCategory(ExampleModCategories.MOD,ExampleModCategories.MOD_OBJECTS,ExampleModCategories.EXAMPLEWOOD),50,true);

        // Register a grass object
        ObjectRegistry.registerObject("examplegrass",new ExampleGrassObject(),1,true);

        // Register an object which uses a level event
        ObjectRegistry.registerObject("exampleleveleventobject", new ExampleLevelEventObject(),1,true);

    }
}
