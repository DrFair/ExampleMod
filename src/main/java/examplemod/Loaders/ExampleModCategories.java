package examplemod.Loaders;

import necesse.engine.localization.message.LocalMessage;
import necesse.inventory.item.ItemCategory;

public final class ExampleModCategories {
    private ExampleModCategories() {}

    /*
     * IMPORTANT (Creative Menu requirement)
     * ------------------------------------
     * The Creative menu tabs currently only browse a small set of hard-coded ROOT categories but this will be changed in the future.
     *
     * Placeables tab roots: tiles / objects / wiring
     * Items tab roots:      equipment / consumable / materials / misc
     * Mobs tab roots:       mobs
     *
     * So: your itemCategoryTree MUST start with one of those roots, otherwise the item/object
     * will not appear in Creative even though it is registered.
     */

    // VANILLA PLACABLE
        // ===== ROOT =====
        public static final String ROOT_TILES = "tiles";
        public static final String ROOT_OBJECTS = "objects";
        public static final String ROOT_WIRING = "wiring";

        // ===== VANILLA: TILES children =====
        public static final String TILES_FLOORS = "floors";
        public static final String TILES_LIQUIDS = "liquids";
        public static final String TILES_TERRAIN = "terrain";

        // ===== VANILLA: OBJECTS children =====
        public static final String OBJECTS_SEEDS = "seeds";
        public static final String OBJECTS_CRAFTINGSTATIONS = "craftingstations";
        public static final String OBJECTS_LIGHTING = "lighting";
        public static final String OBJECTS_FURNITURE = "furniture";
        public static final String OBJECTS_DECORATIONS = "decorations";
        public static final String OBJECTS_WALLSANDDOORS = "wallsanddoors";
        public static final String OBJECTS_FENCESANDGATES = "fencesandgates";
        public static final String OBJECTS_COLUMNS = "columns";
        public static final String OBJECTS_TRAPS = "traps";
        public static final String OBJECTS_LANDSCAPING = "landscaping";
        public static final String OBJECTS_MISC = "misc";

        // ===== VANILLA: FURNITURE children =====
        public static final String FURNITURE_MISC = "misc";
        public static final String FURNITURE_OAK = "oak";
        public static final String FURNITURE_SPRUCE = "spruce";
        public static final String FURNITURE_PINE = "pine";
        public static final String FURNITURE_MAPLE = "maple";
        public static final String FURNITURE_BIRCH = "birch";
        public static final String FURNITURE_WILLOW = "willow";
        public static final String FURNITURE_DUNGEON = "dungeon";
        public static final String FURNITURE_BONE = "bone";
        public static final String FURNITURE_DRYAD = "dryad";
        public static final String FURNITURE_BAMBOO = "bamboo";
        public static final String FURNITURE_DEADWOOD = "deadwood";

        // ===== VANILLA: DECORATIONS children =====
        public static final String DECORATIONS_PAINTINGS = "paintings";
        public static final String DECORATIONS_CARPETS = "carpets";
        public static final String DECORATIONS_POTS = "pots";
        public static final String DECORATIONS_BANNERS = "banners";

        // ===== VANILLA: LANDSCAPING children =====
        public static final String LANDSCAPING_FORESTROCKSANDORES = "forestrocksandores";
        public static final String LANDSCAPING_SNOWROCKSANDORES = "snowrocksandores";
        public static final String LANDSCAPING_PLAINSROCKSANDORES = "plainsrocksandores";
        public static final String LANDSCAPING_SWAMPROCKSANDORES = "swamprocksandores";
        public static final String LANDSCAPING_DESERTROCKSANDORES = "desertrocksandores";
        public static final String LANDSCAPING_INCURSIONROCKSANDORES = "incursionrocksandores";
        public static final String LANDSCAPING_CRYSTALS = "crystals";
        public static final String LANDSCAPING_TABLEDECORATIONS = "tabledecorations";
        public static final String LANDSCAPING_PLANTS = "plants";
        public static final String LANDSCAPING_MASONRY = "masonry";
        public static final String LANDSCAPING_MISC = "misc";

        // ===== VANILLA: WIRING children =====
        public static final String WIRING_LOGICGATES = "logicgates";


    // YOUR MOD ROOT CATEGORY
    public static final String MOD = "examplemod";
    // YOUR MOD SUB CATEGORY
    public static final String MOD_OBJECTS = "objects";

    public static void load() {

        // ITEM CATEGORIES (not Creative-visible right now, but valid categories)
        ItemCategory.createCategory("Z-EXAMPLEMOD",
                new LocalMessage("itemcategory", "examplemodrootcat"),
                MOD);

        ItemCategory.createCategory("Z-EXAMPLEMOD-OBJECTS",
                new LocalMessage("itemcategory", "examplemodobjectssubcat"),
                MOD, MOD_OBJECTS);

        // CRAFTING CATEGORIES
        ItemCategory.craftingManager.createCategory("Z-EXAMPLEMOD",
                new LocalMessage("itemcategory", "examplemodrootcat"),
                MOD);

        ItemCategory.craftingManager.createCategory("Z-EXAMPLEMOD",
                new LocalMessage("itemcategory", "examplemodobjectscat"),
                MOD,MOD_OBJECTS);
    }
}
