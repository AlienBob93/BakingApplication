package world.pallc.baked.NetworkUtils;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import world.pallc.baked.Data.RecipeContract;
import world.pallc.baked.Data.RecipesClass;

import static world.pallc.baked.Data.RecipeContract.*;

/**
 * Created by Prashant Rao on 15-Jul-17.
 */

public class JsonUtils {

    public static ContentValues[] getRecipesFromJson(Context context, String recipeListJsonStr)
        throws JSONException {

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";

        // put the recipes in a JSONArray
        JSONArray recipeArray = new JSONArray(recipeListJsonStr);
        ContentValues[] parsedRecipeList = new ContentValues[recipeArray.length()];

        for (int i = 0; i < recipeArray.length(); i++) {
            int recipeId;
            String recipeName;
            String recipeImage;
            int recipeServings;
            JSONArray recipeIngredients;
            JSONArray recipeSteps;

            JSONObject recipe = recipeArray.getJSONObject(i);

            recipeId = recipe.getInt(RECIPE_ID);
            recipeName = recipe.getString(RECIPE_NAME);
            recipeImage = recipe.isNull(RECIPE_NAME) ? null : recipe.getString(RECIPE_IMAGE);
            recipeServings = recipe.isNull(RECIPE_SERVINGS) ? -1 : recipe.getInt(RECIPE_SERVINGS);

            recipeIngredients = recipe.getJSONArray(RECIPE_INGREDIENTS);
            recipeSteps = recipe.getJSONArray(RECIPE_STEPS);

            ContentValues recipeStr = new ContentValues();
            recipeStr.put(RecipeEntry.RECIPE_ID, recipeId);
            recipeStr.put(RecipeEntry.RECIPE_NAME, recipeName);
            recipeStr.put(RecipeEntry.RECIPE_IMAGE, recipeImage);
            recipeStr.put(RecipeEntry.RECIPE_SERVINGS, recipeServings);
            recipeStr.put(RecipeEntry.RECIPE_INGREDIENTS, recipeIngredients.toString());
            recipeStr.put(RecipeEntry.RECIPE_STEPS, recipeSteps.toString());

            parsedRecipeList[i] = recipeStr;
        }

        return parsedRecipeList;
    }

    public static ArrayList<String[]> getRecipeSteps(Context context, JSONArray stepsArray)
            throws JSONException {

        final String STEP_ID = "id";
        final String STEP_SHORT_DESCRIPTION = "shortDescription";
        final String STEP_DESCRIPTION = "description";
        final String STEP_VIDEO_URL = "videoURL";
        final String STEP_THUMBNAIL_URL = "thumbnailURL";

        ArrayList<String[]> parsedSteps = new ArrayList<>();

        for (int i = 0; i < stepsArray.length(); i++) {
            int stepId;
            String shortDescription;
            String description;
            String videoUrl;
            String thumbnailUrl;

            JSONObject step = stepsArray.getJSONObject(i);

            // get the values while handling missing variables
            stepId = step.isNull(STEP_ID) ? -1 : step.getInt(STEP_ID);
            shortDescription = step.isNull(STEP_SHORT_DESCRIPTION) ? "Step " + i : step.getString(STEP_SHORT_DESCRIPTION);
            description = step.isNull(STEP_DESCRIPTION) ? "..." : step.getString(STEP_DESCRIPTION);
            videoUrl = step.isNull(STEP_VIDEO_URL) ? null : step.getString(STEP_VIDEO_URL);
            thumbnailUrl = step.isNull(STEP_THUMBNAIL_URL) ? null : step.getString(STEP_THUMBNAIL_URL);

            String[] stepsStr =
                    new String[] {Integer.toString(stepId), shortDescription, description, videoUrl, thumbnailUrl};
            parsedSteps.add(i, stepsStr);
        }

        return parsedSteps;
    }

    public static ArrayList<String[]> getIngredients(Context context, JSONArray ingredientsArray)
            throws JSONException {

        final String INGREDIENT_QUANTITY = "quantity";
        final String INGREDIENT_MEASUREMENT_UNIT = "measure";
        final String INGREDIENT_NAME = "ingredient";

        ArrayList<String[]> parsedIngredients = new ArrayList<>();

        for (int i = 0; i < ingredientsArray.length(); i++) {
            int quantity;
            String measurementUnits;
            String ingredientName;

            JSONObject ingredient = ingredientsArray.getJSONObject(i);

            // get the values while handling missing variables
            quantity = ingredient.getInt(INGREDIENT_QUANTITY);
            measurementUnits = ingredient.getString(INGREDIENT_MEASUREMENT_UNIT);
            ingredientName = ingredient.getString(INGREDIENT_NAME);

            String[] ingredientsStr = new String[] {Integer.toString(quantity), measurementUnits, ingredientName};
            parsedIngredients.add(i, ingredientsStr);
        }

        return parsedIngredients;
    }
}
