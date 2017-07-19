package world.pallc.baked;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class StepDetailsActivity extends AppCompatActivity {

    private static final String TAG = "StepDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        long recipeId = getIntent().getLongExtra("CLICKED_RECIPE_ID", 1);
        int stepNumber = getIntent().getIntExtra("CLICKED_STEP_POSITION", 1);
        Log.i(TAG, "got int extra recipeId " + recipeId);
        Log.i(TAG, "got int extra stepNumber " + stepNumber);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (stepNumber == 0) {
            IngredientsDetailFragment ingredientFragment = new IngredientsDetailFragment();
            ingredientFragment.setRecipeId(recipeId);
            fragmentManager.beginTransaction()
                    .add(R.id.step_details_container, ingredientFragment)
                    .commit();
        } else {
            StepDetailsFragment detailsFragment = new StepDetailsFragment();
            detailsFragment.setRecipeId(recipeId);
            detailsFragment.setStepNumber(stepNumber);
            fragmentManager.beginTransaction()
                    .add(R.id.step_details_container, detailsFragment)
                    .commit();
        }
    }
}
