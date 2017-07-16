package world.pallc.baked.NetworkUtils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class NetworkUtils {

    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    // generate appropriate URL to query recipes
    public static URL buildUrlForJson() {
        Uri builtRecipeUri = Uri.parse(RECIPE_URL).buildUpon().build();
        URL urlRecipe = null;

        try {
            urlRecipe = new URL(builtRecipeUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return  urlRecipe;
    }

    // connect to the web API and fetch the returned data
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
