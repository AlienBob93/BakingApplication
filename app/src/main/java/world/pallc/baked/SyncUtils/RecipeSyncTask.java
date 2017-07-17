package world.pallc.baked.SyncUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.net.URL;

import world.pallc.baked.NetworkUtils.JsonUtils;
import world.pallc.baked.NetworkUtils.NetworkUtils;

import static world.pallc.baked.Data.RecipeContract.RecipeEntry;

/**
 * Created by Prashant Rao on 17-Jul-17.
 */

public class RecipeSyncTask {

    private static final String TAG = "RecipeSyncTask";

    synchronized public static void syncRecipe(Context mContext) {
        try{
            URL recipesRequestUrl = NetworkUtils.buildUrlForJson();

            String JsonResponse = NetworkUtils.getResponseFromHttpUrl(recipesRequestUrl);

            ContentValues[] recipeValues = JsonUtils.getRecipesFromJson(mContext, JsonResponse);

            if (recipeValues != null && recipeValues.length != 0) {
                ContentResolver contentResolver = mContext.getContentResolver();

                contentResolver.delete(RecipeEntry.CONTENT_URI, null, null);
                Log.i(TAG, "deleted old data");

                contentResolver.bulkInsert(RecipeEntry.CONTENT_URI, recipeValues);
                Log.i(TAG, "inserted new rows");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
