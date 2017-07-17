package world.pallc.baked;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Prashant Rao on 17-Jul-17.
 */

public class RecipeStepsMasterListAdapter
        extends RecyclerView.Adapter<RecipeStepsMasterListAdapter.ViewHolder> {

    private static final String TAG = "StepsMasterListAdapter";
    private Context mContext;

    final private MasterListonClickHandler mClickHandler;

    public interface MasterListonClickHandler {
        void onClick(long rowID, int stepPosition);
    }

    // string to hold the strings containing the text
    // "ingredients" and the "n step short descriptions" involved
    private String[] recipeIngredientsAndSteps;
    // variable to hold the row ID of the recipe in question
    private long recipeID;

    public RecipeStepsMasterListAdapter(Context context, MasterListonClickHandler clickHandler) {
        this.mContext = context;
        mClickHandler = clickHandler;
    }

    public void setData(Long recipeID, String[] recipeClickedOn) {
        this.recipeID = recipeID;
        this.recipeIngredientsAndSteps = recipeClickedOn;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout to a view
        int layoutIdForListItem = R.layout.view_recipe_step;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder position " + position);
        if (position == 0) {
            holder.stepIndex.setText("  ");
            holder.stepDescription.setText(recipeIngredientsAndSteps[position]);
        } else {
            holder.stepIndex.setText(String.valueOf(position));
            holder.stepDescription.setText(recipeIngredientsAndSteps[position]);
        }
    }

    @Override
    public int getItemCount() {
        return recipeIngredientsAndSteps.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        public TextView stepIndex, stepDescription;
        public ViewHolder(View itemView) {
            super(itemView);
            stepIndex = itemView.findViewById(R.id.tv_step_index);
            stepDescription = itemView.findViewById(R.id.tv_step_short_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(recipeID, clickedPosition);
        }
    }
}
