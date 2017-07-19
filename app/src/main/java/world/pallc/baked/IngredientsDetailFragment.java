package world.pallc.baked;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.ArrayList;

import world.pallc.baked.Data.RecipeContract;
import world.pallc.baked.NetworkUtils.JsonUtils;

import static world.pallc.baked.Data.RecipeContract.*;

/**
 * Created by Prashant Rao on 18-Jul-17.
 */

public class IngredientsDetailFragment extends Fragment {

    private static final String TAG = "IngredientsFragment";
    Context mContext;
    private long recipeId;
    private RecyclerView mRecyclerView;

    public IngredientsDetailFragment() {}

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        mContext = getContext();

        mRecyclerView = rootView.findViewById(R.id.ingredients_recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        IngredientsListAdapter mAdapter = new IngredientsListAdapter(mContext);
        mAdapter.setIngredientsDetails(getIngredientsList(mContext, recipeId));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private ArrayList<String[]> getIngredientsList(Context context, long recipeIdToQuery) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(RecipeEntry.CONTENT_URI, null,
                RecipeEntry._ID + " = " + recipeIdToQuery,
                null, null);

        if (null != cursor) {
            cursor.moveToNext();

            JSONArray ingredientsArray;
            int ingredientsIndex = cursor.getColumnIndex(RecipeEntry.RECIPE_INGREDIENTS);
            String ingredientsArrayStr = cursor.getString(ingredientsIndex);

            try {
                ingredientsArray = new JSONArray(ingredientsArrayStr);
                return JsonUtils.getIngredients(context, ingredientsArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor.close();
        }

        return  null;
    }
}
