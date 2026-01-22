package examplemod.examples;

import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;

/*
here is where we will register our recipes into the game.
 there is potentially quite a few of them so this will allow us to maintain cleaner code
*/
public class ExampleRecipes {

    //Put your recipe registrations in here
    public static void registerRecipes(){

        // Example Bar item smelted in the forge
        Recipes.registerModRecipe(new Recipe(
                "examplebar",
                1,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("exampleore",2)
                })
        );


        // Example item recipe, crafted in inventory for 2 iron bars
        Recipes.registerModRecipe(new Recipe(
                "exampleitem",
                1,
                RecipeTechRegistry.NONE,
                new Ingredient[]{
                        new Ingredient("examplebar", 2)
                }
        ).showAfter("woodboat")); // Show recipe after wood boat recipe


        // Example sword recipe, crafted in iron anvil using 4 example items and 5 copper bars
        Recipes.registerModRecipe(new Recipe(
                "examplesword",
                1,
                RecipeTechRegistry.IRON_ANVIL,
                new Ingredient[]{
                        new Ingredient("exampleitem", 4),
                        new Ingredient("examplebar", 5)
                }
        ));

        // Example staff recipe, crafted in workstation using 4 example items and 10 gold bars
        Recipes.registerModRecipe(new Recipe(
                "examplestaff",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("exampleitem", 4),
                        new Ingredient("examplebar", 10)
                }
        ).showAfter("exampleitem")); // Show the recipe after example item recipe

        // Example food item recipe
        Recipes.registerModRecipe(new Recipe(
                "examplefood",
                1,
                RecipeTechRegistry.COOKING_POT,
                new Ingredient[]{
                        new Ingredient("bread", 1),
                        new Ingredient("strawberry", 2),
                        new Ingredient("sugar", 1)
                }
        ));

        // Example potion item recipe
        Recipes.registerModRecipe(new Recipe(
                "examplepotion",
                1,
                RecipeTechRegistry.ALCHEMY,
                new Ingredient[]{
                        new Ingredient("speedpotion", 1),
                }
        ));
    }
}
