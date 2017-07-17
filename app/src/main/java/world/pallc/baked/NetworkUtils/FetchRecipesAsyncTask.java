package world.pallc.baked.NetworkUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import static world.pallc.baked.Data.RecipeContract.RecipeEntry;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class FetchRecipesAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "FetchRecipesAsyncTask";
    private Context mContext;

    public FetchRecipesAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
        return null;
    }
}

