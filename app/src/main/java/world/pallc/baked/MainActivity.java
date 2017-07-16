package world.pallc.baked;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import world.pallc.baked.NetworkUtils.FetchRecipesAsyncTask;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView mRecyclerView;
    private static RecipeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        // specify an adapter (see also next example)
        loadRecipes();
    }

    private void loadRecipes() {
        new FetchRecipesAsyncTask(this).execute();
    }

    public static void setRecipeAdapter(ArrayList<String[]> recipes) {
        mAdapter = new RecipeAdapter(recipes);
        mRecyclerView.setAdapter(mAdapter);
    }
}
