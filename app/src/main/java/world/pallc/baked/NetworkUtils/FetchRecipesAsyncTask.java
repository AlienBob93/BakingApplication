package world.pallc.baked.NetworkUtils;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;

import world.pallc.baked.MainActivity;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class FetchRecipesAsyncTask extends AsyncTask<Void, Void, ArrayList<String[]>> {

    private Context mContext;

    public FetchRecipesAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<String[]> doInBackground(Void... voids) {

        // TODO request from URL only if local DB is empty
        // query the URL and parse the returned JSON object
        URL requestRecipesUrl = NetworkUtils.buildUrlForJson();

        try {
            String JsonResponse = NetworkUtils.getResponseFromHttpUrl(requestRecipesUrl);

            return JsonUtils.getRecipesFromJson(mContext, JsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<String[]> strings) {
        if (strings != null) {
            MainActivity.setRecipeAdapter(strings);
        }
    }
}
