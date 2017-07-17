package world.pallc.baked.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "world.pallc.baked";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Define the possible paths for accessing data in this contract
    public static final String PATH_RECIPES_LIST = "recipes_list";

    public static final class RecipeEntry implements BaseColumns {
        // table name
        public static final String RECIPES_LIST_TABLE_NAME = "recipes_list";

        // columns
        public static final String _ID = "_id";
        public static final String RECIPE_ID = "recipe_id";
        public static final String RECIPE_NAME = "recipe_name";
        public static final String RECIPE_INGREDIENTS = "recipe_ingredients";
        public static final String RECIPE_STEPS = "recipe_steps";
        public static final String RECIPE_SERVINGS = "recipe_servings";
        public static final String RECIPE_IMAGE = "recipe_image";

                /*
        /* create content uri
         * content URI = base content URI + path
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_RECIPES_LIST).build();
    }
}
