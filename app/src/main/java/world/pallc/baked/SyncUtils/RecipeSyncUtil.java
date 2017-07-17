package world.pallc.baked.SyncUtils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static world.pallc.baked.Data.RecipeContract.RecipeEntry;

/**
 * Created by Prashant Rao on 17-Jul-17.
 */

public class RecipeSyncUtil {

    private static final String TAG = "RecipeSyncUtil";
    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context) {

        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                /* URI for every row of recipe data in our recipes table*/
                Uri recipesQueryUri = RecipeEntry.CONTENT_URI;

                /* Here, we perform the query to check to see if we have any recipes data */
                Cursor cursor = context.getContentResolver().query(
                        recipesQueryUri,
                        null,
                        null,
                        null,
                        null);
                /*
                 * A Cursor object can be null for various different reasons. A few are
                 * listed below.
                 *
                 *   1) Invalid URI
                 *   2) A certain ContentProvider's query method returns null
                 *   3) A RemoteException was thrown.
                 *
                 * Bottom line, it is generally a good idea to check if a Cursor returned
                 * from a ContentResolver is null.
                 *
                 * If the Cursor was null OR if it was empty, we need to sync immediately to
                 * be able to display data to the user.
                 */
                if (null == cursor || cursor.getCount() == 0) {
                    Log.i(TAG, "Starting IntentService for syncing");
                    Intent intentToSyncImmediately = new Intent(context, RecipeSyncIntentService.class);
                    context.startService(intentToSyncImmediately);
                }

                /* Make sure to close the Cursor to avoid memory leaks! */
                cursor.close();
            }
        });

        /* Finally, once the thread is prepared, fire it off to perform our checks. */
        checkForEmpty.start();
    }
}
