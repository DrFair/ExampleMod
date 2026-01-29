package examplemod.Loaders;

import necesse.engine.localization.message.LocalMessage;
import necesse.inventory.item.ItemCategory;

public final class ExampleModCategories {
    private ExampleModCategories() {}

    /*
     * Necesse has TWO category trees:
     *
     * 1) Item categories (ItemCategory master tree)
     *    * Used for Creative inventory browsing / item lists / general item grouping.
     *    * This is what setItemCategory(...) uses on items/objects.
     *
     * 2) Crafting categories (ItemCategory.craftingManager tree)
     *    * Used by crafting station recipe lists and their filter/grouping UI.
     *    * This is what setCraftingCategory(...) uses on items/objects (and also what Recipe.setCraftingCategory uses).
     *
     * Important rule:
     *   If you want to use a category path, you MUST create that exact path in the corresponding manager
     *   BEFORE any items/objects/recipes try to reference it.
     *
     *
     * -------------------------------------------------------------------------
     * Ways to APPLY categories:
     *
     * A) Apply on the object/item itself (recommended baseline)
     *      Object registration (GameObject):
     *        new ExampleObject()
     *          .setItemCategory(ROOT, PLACEABLES, DECOR)        // Creative / item browsing
     *          .setCraftingCategory(ROOT, PLACEABLES, DECOR);   // Crafting menu default
     *
     *      Item registration (Item):
     *        new ExampleItem()
     *          .setItemCategory(ROOT, MATERIALS)
     *          .setCraftingCategory(ROOT, MATERIALS);
     *
     *    What this affects:
     *        Creative inventory category placement (itemCategoryTree)
     *        Default crafting category used by stations/recipes if not overridden (craftingCategoryTree)
     *
     * B) Apply on the recipe (vanilla does this often)
     *      Recipe override:
     *        new Recipe(...)
     *          .setCraftingCategory(ROOT, PLACEABLES, DECOR);
     *
     *    What this affects:
     *        Where the RECIPE appears in the crafting UI.
     *        Useful when a station collapses category depth (e.g. Workstation depth=1).
     *        You can force “Placeables/Decor” grouping even when the station normally trims deeper paths.
     *
     * Rule of thumb:
     *     Put the “true” category on the item/object (so creative inventory and general lists are correct).
     *     Use Recipe.setCraftingCategory(...) when you want a specific crafting UI grouping.
     */

    // Category paths (so you can reuse them consistently)
    public static final String ROOT = "examplemod";

    public static final String INCURSION = "incursion";
    public static final String MATERIALS = "materials";
    public static final String CONSUMABLES = "consumables";
    public static final String TOOLS = "tools";
    public static final String WEAPONS = "weapons";
    public static final String ARMOR = "armor";
    public static final String PLACEABLES = "placeables";
    public static final String FURNITURE = "furniture";
    public static final String LIGHTING = "lighting";
    public static final String DECOR = "decor";

    public static void load() {

        // ===== Normal item category tree (Creative inventory / item browsing) =====
        // "Z-..." usually pushes it toward the bottom; change if you want it earlier.
        // The category "path" is built from these strings, e.g.:
        //   ROOT -> PLACEABLES -> DECOR
        // becomes:
        //   examplemod.placeables.decor
        ItemCategory.createCategory("Z-EX-0", new LocalMessage("itemcategory", "examplemod"), ROOT);

        ItemCategory.createCategory("Z-EX-1", new LocalMessage("itemcategory", "examplemod_materials"), ROOT, MATERIALS);

        ItemCategory.createCategory("Z-EX-2", new LocalMessage("itemcategory", "examplemod_consumables"), ROOT, CONSUMABLES);
        ItemCategory.createCategory("Z-EX-3", new LocalMessage("itemcategory", "examplemod_tools"), ROOT, TOOLS);
        ItemCategory.createCategory("Z-EX-4", new LocalMessage("itemcategory", "examplemod_weapons"), ROOT, WEAPONS);
        ItemCategory.createCategory("Z-EX-5", new LocalMessage("itemcategory", "examplemod_armor"), ROOT, ARMOR);
        ItemCategory.createCategory("Z-EX-6", new LocalMessage("itemcategory", "examplemod_incursion"), ROOT, INCURSION);
        ItemCategory.createCategory("Z-EX-7", new LocalMessage("itemcategory", "examplemod_placeables"), ROOT, PLACEABLES);

        // Subcategories under Placeables (Creative)
        ItemCategory.createCategory("Z-EX-7A", new LocalMessage("itemcategory", "examplemod_furniture"), ROOT, PLACEABLES, FURNITURE);
        ItemCategory.createCategory("Z-EX-7B", new LocalMessage("itemcategory", "examplemod_lighting"),  ROOT, PLACEABLES, LIGHTING);
        ItemCategory.createCategory("Z-EX-7C", new LocalMessage("itemcategory", "examplemod_decor"),     ROOT, PLACEABLES, DECOR);

        // ===== Crafting filter tree (Crafting station recipe list/filter UI) =====
        // If you want the same category paths to work in crafting menus,
        // you must mirror the same tree here.
        ItemCategory.craftingManager.createCategory("Z-EX-0", new LocalMessage("itemcategory", "examplemod"), ROOT);
        ItemCategory.craftingManager.createCategory("Z-EX-1", new LocalMessage("itemcategory", "examplemod_materials"), ROOT, MATERIALS);
        ItemCategory.craftingManager.createCategory("Z-EX-2", new LocalMessage("itemcategory", "examplemod_consumables"), ROOT, CONSUMABLES);
        ItemCategory.craftingManager.createCategory("Z-EX-3", new LocalMessage("itemcategory", "examplemod_tools"), ROOT, TOOLS);
        ItemCategory.craftingManager.createCategory("Z-EX-4", new LocalMessage("itemcategory", "examplemod_weapons"), ROOT, WEAPONS);
        ItemCategory.craftingManager.createCategory("Z-EX-5", new LocalMessage("itemcategory", "examplemod_armor"), ROOT, ARMOR);
        ItemCategory.craftingManager.createCategory("Z-EX-6", new LocalMessage("itemcategory", "examplemod_incursion"), ROOT, INCURSION);
        ItemCategory.craftingManager.createCategory("Z-EX-7", new LocalMessage("itemcategory", "examplemod_placeables"), ROOT, PLACEABLES);

        // Subcategories under Placeables (Crafting)
        ItemCategory.craftingManager.createCategory("Z-EX-7A", new LocalMessage("itemcategory", "examplemod_furniture"), ROOT, PLACEABLES, FURNITURE);
        ItemCategory.craftingManager.createCategory("Z-EX-7B", new LocalMessage("itemcategory", "examplemod_lighting"),  ROOT, PLACEABLES, LIGHTING);
        ItemCategory.craftingManager.createCategory("Z-EX-7C", new LocalMessage("itemcategory", "examplemod_decor"),     ROOT, PLACEABLES, DECOR);
    }
}
