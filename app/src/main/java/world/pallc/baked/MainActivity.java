package world.pallc.baked;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import world.pallc.baked.NetworkUtils.FetchRecipesAsyncTask;

import static world.pallc.baked.Data.RecipeContract.RecipeEntry;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mPosition = RecyclerView.NO_POSITION;

    private static final int ID_RECIPES_LOADER = 616;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(ID_RECIPES_LOADER, null, this);

        // get network data
        new FetchRecipesAsyncTask(this).execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_RECIPES_LOADER:
                Log.i(TAG, "onCreateLoader");
                Uri recipesQueryUri = RecipeEntry.CONTENT_URI;

                return new CursorLoader(this,
                        recipesQueryUri,
                        null,
                        null,
                        null,
                        null);
            default:
                throw  new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "onLoadFinished");
        // call the adapter's swapCursor method and pass in the new Cursor
        mAdapter.swapCursor(data);
        /*if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG, "onLoaderReset");
        mAdapter.swapCursor(null);
    }
}
