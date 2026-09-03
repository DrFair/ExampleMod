package examplemod.Loaders;

import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;

/**
 * Here is where we will register our recipes into the game.
 * There is potentially quite a few of them so this will allow us to maintain cleaner code
*/
public class ExampleModRecipes {

    //Put your recipe registrations in here
    public static void registerRecipes(){

        // Example item recipe, crafted in inventory for 2 iron bars
        Recipes.registerModRecipe(new Recipe(
                "exampleitem",
                1,
                RecipeTechRegistry.NONE,
                new Ingredient[] {
                        new Ingredient("examplebar", 2)
                }
        ).showAfter("woodboat")); // Show recipe after wood boat recipe


        // FORGE RECIPES
        Recipes.registerModRecipe(new Recipe(
                "examplebar",
                1,
                RecipeTechRegistry.FORGE,
                new Ingredient[] {
                        new Ingredient("exampleore",2)
                })
        );

        // IRON ANVIL RECIPES
        Recipes.registerModRecipe(new Recipe(
                "examplemeleesword",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[] {
                        new Ingredient("exampleitem", 4),
                        new Ingredient("examplebar", 5)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplemagicstaff",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[] {
                        new Ingredient("exampleitem", 5),
                        new Ingredient("examplebar", 4)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplesummonorb",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[] {
                        new Ingredient("exampleitem", 3),
                        new Ingredient("examplebar", 2)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplerangedbow",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[] {
                        new Ingredient("examplelog", 8),
                        new Ingredient("examplebar", 2),
                        new Ingredient("exampleitem", 2)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplehelmet",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[] {
                        new Ingredient("examplebar", 8),
                        new Ingredient("exampleitem", 2)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplechestplate",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[] {
                        new Ingredient("examplebar", 14),
                        new Ingredient("exampleitem", 4)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "exampleboots",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[] {
                        new Ingredient("examplebar", 10),
                        new Ingredient("exampleitem", 3)
                }
        ));

        // WORKSTATION RECIPES
        Recipes.registerModRecipe(new Recipe(
                "examplewall",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[] {
                        new Ingredient("examplestone", 7)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "exampledoor",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[] {
                        new Ingredient("examplestone", 7)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplearrow",
                25, // 25 arrows per craft
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[] {
                        new Ingredient("examplelog", 1),
                        new Ingredient("exampleitem", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplepressureplate",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[] {
                        new Ingredient("examplestone", 6),
                        new Ingredient("examplebar", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "exampleworkstation",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[] {
                        new Ingredient("examplelog", 20),
                        new Ingredient("examplebar", 8)
                }
        ));



        // COOKING POT RECIPES
        Recipes.registerModRecipe(new Recipe(
                "examplefood",
                1,
                RecipeTechRegistry.COOKING_POT,
                new Ingredient[] {
                        new Ingredient("bread", 1),
                        new Ingredient("strawberry", 2),
                        new Ingredient("sugar", 1)
                }
        ));

        // ALCHEMY RECIPES
        Recipes.registerModRecipe(new Recipe(
                "examplepotion",
                1,
                RecipeTechRegistry.ALCHEMY,
                new Ingredient[] {
                        new Ingredient("speedpotion", 1),
                }
        ));

        // LANDSCAPING RECIPES
        Recipes.registerModRecipe(new Recipe(
                "examplebaserock",
                1,
                RecipeTechRegistry.LANDSCAPING,
                new Ingredient[] {
                        new Ingredient("examplestone", 5),
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "exampleorerock",
                1,
                RecipeTechRegistry.LANDSCAPING,
                new Ingredient[] {
                        new Ingredient("examplestone", 5),
                        new Ingredient("exampleore", 5),
                }
        ));

        // EXAMPLE TECH RECIPES
        Recipes.registerModRecipe(new Recipe(
                "exampleconfigobject",
                1,
                ExampleModTech.EXAMPLE_TECH,
                new Ingredient[] {
                        new Ingredient("examplestone", 4),
                        new Ingredient("exampleitem", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplejobobject",
                1,
                ExampleModTech.EXAMPLE_TECH,
                new Ingredient[] {
                        new Ingredient("examplestone", 4),
                        new Ingredient("exampleitem", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "exampleeventtriggerobject",
                1,
                ExampleModTech.EXAMPLE_TECH,
                new Ingredient[] {
                        new Ingredient("examplestone", 4),
                        new Ingredient("exampleitem", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "exampleobject",
                1,
                ExampleModTech.EXAMPLE_TECH,
                new Ingredient[] {
                        new Ingredient("examplestone", 7),
                        new Ingredient("exampleitem", 3)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "examplebosssummonitem",
                1,
                ExampleModTech.EXAMPLE_TECH,
                new Ingredient[] {
                        new Ingredient("examplestone", 10),
                        new Ingredient("examplelog", 10),
                        new Ingredient("exampleitem", 5)
                }
        ));

    }
}
