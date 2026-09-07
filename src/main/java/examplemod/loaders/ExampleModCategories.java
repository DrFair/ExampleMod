package examplemod.loaders;

import necesse.engine.localization.message.LocalMessage;
import necesse.inventory.item.ItemCategory;

public class ExampleModCategories {

    public static void load() {
        // Here we register our example item categories
        // The first parameter is the sorting string. It allows us to define exactly where we
        // want our category to be displayed. Works like this:
        // The string is divided up between the hyphens (-). Then each section is alphabetically compared to the
        // other categories corresponding section. This makes it so it's always possible to insert a category in
        // between two existing categories no matter what sorting string they have.

        // You can see the existing base game categories here:
        /// {@link necesse.inventory.item.ItemCategory}

        // ITEM CATEGORIES
        ItemCategory.createCategory(
                "BA-A-A", // We want it to be sorted just after the consumable root category
                new LocalMessage("itemcategory", "examplemod"),
                "examplemod"
        );

        ItemCategory.createCategory(
                "BA-A-A-SUB",
                new LocalMessage("itemcategory", "examplemodsub"),
                "examplemod", "sub"
        );

        // If we want the category to appear in the placeables creative menu, we can do so by
        // adding the root category to the list like this:
//        CreativeMenuForm.placeablesTabMasterCategories.add("examplemod");

        // CRAFTING CATEGORIES
        // These categories are used in workstations. They define the order in which the categories are shown
        ItemCategory.craftingManager.createCategory(
                "BA-A-A",
                new LocalMessage("itemcategory", "examplemod"),
                "examplemod"
        );

        ItemCategory.craftingManager.createCategory(
                "BA-A-A-SUB",
                new LocalMessage("itemcategory", "examplemodsub"),
                "examplemod", "sub"
        );
    }
}
