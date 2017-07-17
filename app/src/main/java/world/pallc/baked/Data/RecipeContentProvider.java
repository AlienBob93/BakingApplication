package world.pallc.baked.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static world.pallc.baked.Data.RecipeContract.CONTENT_AUTHORITY;
import static world.pallc.baked.Data.RecipeContract.PATH_RECIPES_LIST;
import static world.pallc.baked.Data.RecipeContract.RecipeEntry;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class RecipeContentProvider extends ContentProvider {

    private static final String TAG = "RecipeContentProvider";
    private static final int RECIPE = 300;

    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Member variable for a RecipesDbHelper that's initialized in the onCreate() method
    private RecipesDbHelper recipesDbHelper;

    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // add a code for each type of URI you want
        matcher.addURI(CONTENT_AUTHORITY, PATH_RECIPES_LIST, RECIPE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        // initialize RecipesDbHelper on startup
        Context context = getContext();
        recipesDbHelper = new RecipesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = recipesDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the recipes directory and write a default case
        switch (match) {
            // Query for the recipes directory
            case RECIPE:
                retCursor =  db.query(RecipeEntry.RECIPES_LIST_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = recipesDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case RECIPE:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeEntry.RECIPES_LIST_TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                Log.i(TAG, rowsInserted + " rows inserted into DB.");
                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // Get access to the movie database (to write new data to)
        final SQLiteDatabase database = recipesDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case RECIPE:
                // Insert new values into the database
                // Inserting values into favorites movies table
                long id = database.insert(RecipeEntry.RECIPES_LIST_TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize the directory
        final SQLiteDatabase db = recipesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted movies
        int tasksDeleted; // starts as 0

        switch (match) {
            case RECIPE:
                // delete all rows
                tasksDeleted = db.delete(RecipeEntry.RECIPES_LIST_TABLE_NAME, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // All rows were deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of recipes deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}