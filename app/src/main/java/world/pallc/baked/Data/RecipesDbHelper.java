package world.pallc.baked.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;
import static world.pallc.baked.Data.RecipeContract.*;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class RecipesDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "recipesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // constructor
    public RecipesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + RecipeEntry.RECIPES_LIST_TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                RecipeEntry.RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeEntry.RECIPE_NAME + " TEXT NOT NULL, " +
                RecipeEntry.RECIPE_INGREDIENTS + " TEXT NOT NULL, " +
                RecipeEntry.RECIPE_STEPS + " TEXT NOT NULL, " +
                RecipeEntry.RECIPE_SERVINGS + " INTEGER, " +
                RecipeEntry.RECIPE_IMAGE + " TEXT);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("ALTER TABLE IF EXISTS " + RecipeEntry.RECIPES_LIST_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
