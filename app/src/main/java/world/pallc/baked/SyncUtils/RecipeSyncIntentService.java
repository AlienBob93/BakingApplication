package world.pallc.baked.SyncUtils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Prashant Rao on 17-Jul-17.
 */

public class RecipeSyncIntentService extends IntentService {

    public RecipeSyncIntentService() {
        super("RecipeSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RecipeSyncTask.syncRecipe(this);
    }
}
