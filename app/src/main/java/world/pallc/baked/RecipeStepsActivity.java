package world.pallc.baked;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class RecipeStepsActivity extends AppCompatActivity {

    private static final String TAG = "RecipeStepsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        long recipeID = getIntent().getLongExtra("CLICKED_RECIPE_ID", 1);
        Log.i(TAG, "got int extra " + recipeID);

        RecipeStepsMasterListFragment masterListFragment = new RecipeStepsMasterListFragment();
        masterListFragment.setRecipe(recipeID);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps_container, masterListFragment)
                .commit();
    }
}
