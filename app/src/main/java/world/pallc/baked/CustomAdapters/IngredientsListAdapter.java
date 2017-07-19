package world.pallc.baked.CustomAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import world.pallc.baked.R;

/**
 * Created by Prashant Rao on 18-Jul-17.
 */

public class IngredientsListAdapter
        extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {

    private static final String TAG = "IngredientsListAdapter";
    private Context mContext;
    private ArrayList<String[]> ingredientsDetails;

    public IngredientsListAdapter(Context context) {
        this.mContext = context;
    }

    public void setIngredientsDetails(ArrayList<String[]> ingredientsDetails) {
        this.ingredientsDetails = ingredientsDetails;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout to a view
        int layoutIdForListItem = R.layout.view_ingredients_detail;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ingredientQuantity.setText(ingredientsDetails.get(position)[0]);
        holder.ingredientUnits.setText(ingredientsDetails.get(position)[1]);
        holder.ingredientName.setText(ingredientsDetails.get(position)[2]);
    }

    @Override
    public int getItemCount() {
        return ingredientsDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ingredientQuantity, ingredientUnits, ingredientName;
        public ViewHolder(View itemView) {
            super(itemView);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            ingredientUnits = itemView.findViewById(R.id.ingredient_units);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
        }
    }
}
