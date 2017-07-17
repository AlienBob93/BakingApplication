package world.pallc.baked;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StepDetailsActivity extends AppCompatActivity {

    private static final String TAG = "StepDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        long recipeId = getIntent().getLongExtra("CLICKED_RECIPE_ID", 1);
        int stepNumber = getIntent().getIntExtra("CLICKED_STEP_POSITION", 1);
        Log.i(TAG, "got int extra " + recipeId);
        Log.i(TAG, "got int extra " + stepNumber);

        StepDetailsFragment detailsFragment = new StepDetailsFragment();


        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.step_details_container, detailsFragment)
                .commit();
    }
}
