package world.pallc.baked;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import world.pallc.baked.CustomAdapters.RecipeStepsMasterListAdapter;
import world.pallc.baked.NetworkUtils.JsonUtils;

import static world.pallc.baked.Data.RecipeContract.*;

/**
 * Created by Prashant Rao on 17-Jul-17.
 */

public class RecipeStepsMasterListFragment extends Fragment
        implements RecipeStepsMasterListAdapter.MasterListonClickHandler {

    private static final String TAG = "StepsMasterListFragment";
    private long recipeID;
    private Context context;

    public RecipeStepsMasterListFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps_list, container, false);
        context = getContext();

        RecyclerView mRecyclerView = rootView.findViewById(R.id.steps_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        RecipeStepsMasterListAdapter masterListAdapter =
                new RecipeStepsMasterListAdapter(context, this);

        // set the RecyclerView data
        masterListAdapter.setData(recipeID, createStepsList(context, recipeID));
        mRecyclerView.setAdapter(masterListAdapter);

        return rootView;
    }

    /**
     * function to retrieve Ingredients and Number of Steps from the DB
     */
    private String[] createStepsList(Context context, long recipeIdToQuery) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(RecipeEntry.CONTENT_URI, null,
                RecipeEntry._ID + " = " + recipeIdToQuery,
                null, null);
        if (null != cursor) {
            cursor.moveToNext();
            int stepsIndex = cursor.getColumnIndex(RecipeEntry.RECIPE_STEPS);
            String stepsArrayStr = cursor.getString(stepsIndex);

            JSONArray stepsArray;
            try {
                stepsArray = new JSONArray(stepsArrayStr);
                ArrayList<String[]> steps = JsonUtils.getRecipeSteps(context, stepsArray);

                String[] adapterData = new String[steps.size() + 1];
                adapterData[0] = "Ingredients";
                for (int i = 0; i < steps.size(); i++) {
                    // get the short step description for the current step
                    adapterData[i + 1] = steps.get(i)[1];
                    Log.i(TAG, "adapterData[" + (i + 1) + "] = " + adapterData[i + 1]);
                }
                return adapterData;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cursor.close();
        }
        return null;
    }

    /**
     * function to receive the recipe to populate the steps list with
     */
    public void setRecipe(long recipeID) {
        this.recipeID = recipeID;
    }

    @Override
    public void onClick(long rowID, int stepPosition) {
        Intent startStepDetailIntent = new Intent(context, StepDetailsActivity.class);
        startStepDetailIntent.putExtra("CLICKED_RECIPE_ID", rowID);
        startStepDetailIntent.putExtra("CLICKED_STEP_POSITION", stepPosition);
        startActivity(startStepDetailIntent);
    }
}
