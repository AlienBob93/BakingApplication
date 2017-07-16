package world.pallc.baked.NetworkUtils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Prashant Rao on 15-Jul-17.
 */

public class JsonUtils {

    public static ArrayList<String[]> getRecipesFromJson(Context context, String recipeListJsonStr)
        throws JSONException {

        final String OWM_MESSAGE_CODE = "cod";

        final String RECIPE_ID = "id";
        final String RECIPE_NAME = "name";
        final String RECIPE_INGREDIENTS = "ingredients";
        final String RECIPE_STEPS = "steps";
        final String RECIPE_SERVINGS = "servings";
        final String RECIPE_IMAGE = "image";

        final String INGREDIENT_QUANTITY = "quantity";
        final String INGREDIENT_MEASUREMENT_UNIT = "measure";
        final String INGREDIENT_NAME = "ingredient";

        final String STEP_ID = "id";
        final String STEP_SHORT_DESCRIPTION = "shortDescription";
        final String STEP_DESCRIPTION = "description";
        final String STEP_VIDEO_URL = "videoURL";
        final String STEP_THUMBNAIL_URL = "thumbnailURL";

        ArrayList<String[]> parsedRecipeList = new ArrayList<>();

        // put the recipes in a JSONArray
        JSONArray recipeArray = new JSONArray(recipeListJsonStr);

        // TODO parse the steps for making the recipes
        for (int i = 0; i < recipeArray.length(); i++) {
            int recipeId;
            String recipeName;
            String recipeImage;
            int recipeServings;

            JSONObject recipe = recipeArray.getJSONObject(i);

            recipeId = recipe.getInt(RECIPE_ID);
            recipeName = recipe.getString(RECIPE_NAME);
            recipeImage = recipe.getString(RECIPE_IMAGE);
            recipeServings = recipe.getInt(RECIPE_SERVINGS);

            String[] recipeStr =
                    new String[] {Integer.toString(recipeId), recipeName, recipeImage, Integer.toString(recipeServings)};
            parsedRecipeList.add(i, recipeStr);
        }

        return parsedRecipeList;
    }
}
