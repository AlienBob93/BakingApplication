package world.pallc.baked;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import world.pallc.baked.Data.RecipeContract;
import world.pallc.baked.NetworkUtils.JsonUtils;

import static world.pallc.baked.Data.RecipeContract.*;

/**
 * Created by Prashant Rao on 17-Jul-17.
 */

public class StepDetailsFragment extends Fragment
        implements ExoPlayer.EventListener {

    private static final String TAG = "StepDetailsFragment";

    private Context mContext;
    private long recipeId;
    private int stepNumber;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private TextView stepDescriptionTextView;

    public StepDetailsFragment() {}

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        mContext = getContext();


        Log.d(TAG, "onCreateView called");
        // Initialize the player view adn description view.
        mPlayerView = rootView.findViewById(R.id.playerView);
        stepDescriptionTextView = rootView.findViewById(R.id.step_description_textView);

        // Load a default artwork.
        /*mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.mipmap.ic_launcher_round));*/

        // Initialize the Media Session.
        initializeMediaSession(mContext);

        // get the step description and any video url for that step
        String[] stepDescription = getStepDescription(mContext, recipeId, stepNumber);

        if (stepDescription != null) {
            // Load the step Description
            initializePlayer(mContext, Uri.parse(stepDescription[0]));
            stepDescriptionTextView.setText(stepDescription[1]);
        }

        return rootView;
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession(Context context) {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Context context, Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, "RecipeStepVideo");
            /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory mediaDataSourceFactory =
                    new DefaultDataSourceFactory(context, userAgent, (TransferListener<? super DataSource>) bandwidthMeter);*/
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * function to retrieve Ingredients and Number of Steps from the DB
     */
    private String[] getStepDescription(Context context, long recipeIdToQuery, int stepNumber) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(RecipeEntry.CONTENT_URI, null,
                RecipeEntry._ID + " = " + recipeIdToQuery,
                null, null);

        if (null != cursor) {
            cursor.moveToNext();

            JSONArray stepsArray;
            int stepsIndex = cursor.getColumnIndex(RecipeEntry.RECIPE_STEPS);
            String stepsArrayStr = cursor.getString(stepsIndex);

            try {
                stepsArray = new JSONArray(stepsArrayStr);
                ArrayList<String[]> steps = JsonUtils.getRecipeSteps(context, stepsArray);
                Log.d(TAG, "element " + (stepNumber - 1) + ": " + stepsArray.get(stepNumber - 1));

                String[] stepUrlAndDescription = new String[2];
                // get the video for the recipe if it exists
                stepUrlAndDescription[0] =
                            steps.get(stepNumber - 1)[3] != null ? steps.get(stepNumber - 1)[3] :
                                    steps.get(stepNumber - 1)[4] != null ? steps.get(stepNumber - 1)[4] : "";
                Log.i(TAG, "Video URL for recipe " + recipeIdToQuery + " and step " + (stepNumber - 1) + ": " + steps.get(stepNumber - 1)[3]);
                Log.i(TAG, "Thumbnail URL for recipe " + recipeIdToQuery + " and step " + (stepNumber - 1) + ": " + steps.get(stepNumber - 1)[4]);
                // get the step description for the current step
                stepUrlAndDescription[1] = steps.get(stepNumber - 1)[2];
                return stepUrlAndDescription;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            cursor.close();
        }
        return null;
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause Called");
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
