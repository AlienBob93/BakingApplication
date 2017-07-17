package world.pallc.baked.Data;

import org.json.JSONArray;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class RecipesClass {
    // class to hold recipe details
    // class isn't parcelable since the class isn't passed between activities

    private int recipeId;
    private String recipeName;
    private String recipeImage;
    private int recipeServings;
    private JSONArray recipeIngredients;
    private JSONArray recipeSteps;

    public RecipesClass(int id, String name, String imageURL,
                        int num_servings, JSONArray ingredients, JSONArray steps) {
        this.recipeId = id;
        this.recipeName = name;
        this.recipeImage = imageURL == null ? "PLACEHOLDER IMAGE URL" : imageURL;
        this.recipeServings = num_servings;
        this.recipeIngredients = ingredients;
        this.recipeSteps = steps;
    }

    // getter methods
    public int getRecipeID() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public String getRecipeImage() {
        return recipeImage;
    }
    public int getRecipeServings() {
        return recipeServings;
    }
    public JSONArray getRecipeIngredients() {
        return recipeIngredients;
    }
    public JSONArray getRecipeSteps() {
        return recipeSteps;
    }
}
