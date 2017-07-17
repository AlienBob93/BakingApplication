package world.pallc.baked;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import world.pallc.baked.Data.RecipeContract;
import world.pallc.baked.Data.RecipesClass;

/**
 * Created by Prashant Rao on 16-Jul-17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final String TAG = "RecipeAdapter";
    private Context mContext;
    private Cursor mCursor;

    public RecipeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout to a view
        int layoutIdForListItem = R.layout.cardview_recipe;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int nameIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_NAME);

        // move to the correct position in the cursor
        mCursor.moveToPosition(position);

        // determine values of the wanted data
        String name = mCursor.getString(nameIndex);

        // set the values
        holder.mRecipeName.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // check if this cursor is the same as the previous cursor (mCursor)
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mRecipeName;
        public ViewHolder(View v) {
            super(v);
            mRecipeName = v.findViewById(R.id.recipe_info_text);
        }
    }
}
