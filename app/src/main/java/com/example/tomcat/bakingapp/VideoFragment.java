package com.example.tomcat.bakingapp;


import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tomcat.bakingapp.utilitis.Tools;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment with Video and descriptopn
 */

public class VideoFragment extends Fragment implements View.OnClickListener {
    private VideoFragment.VideoFragOnClickHandler mCallback;
    RecipeActivity recipeActivity;
    private View rootView;
    @BindView(R.id.button_prev_step) Button buttonPrev;
    @BindView(R.id.button_next_step) Button buttonNext;
    @BindView(R.id.my_exo_prev) ImageButton buttonExoPrev;
    @BindView(R.id.my_exo_next) ImageButton buttonExoNext;
    @BindView(R.id.recipe_desc) TextView recipeDesc;
    @BindView(R.id.exo_shutter) View exoShutter;
    @BindView(R.id.step_thumbnail_view) ImageView tumbView;
    private final static int PLAY_PREV = 0;
    private final static int PLAY_NEXT = 1;
    private Unbinder unbinder;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    int numberOfSteps;
    boolean playWhenReady = true;

    private static final String STATE_RESUME_POSITION = "resumePosition";
    private static final String STATE_PAUSE_PLAY = "pause_play";
    public static long mResumePosition = C.POSITION_UNSET;

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.video_fragment, container, false);
        mCallback = (VideoFragment.VideoFragOnClickHandler) getContext();

        initView(rootView);

        recipeActivity = (RecipeActivity) getActivity();
        numberOfSteps = recipeActivity.getSteps().length-1;

        playStep(RecipeActivity.currentStep);

        if (savedInstanceState != null) {
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            playWhenReady = savedInstanceState.getBoolean(STATE_PAUSE_PLAY);
        }

        return rootView;
    }

    // ********************************************************************************************* Init View
    private void initView(View view){
        unbinder = ButterKnife.bind(this, view);

        buttonPrev.setTypeface(MainActivity.robotoFont);
        buttonPrev.setOnClickListener(this);
        buttonPrev.setTag(PLAY_PREV);

        buttonNext.setTypeface(MainActivity.robotoFont);
        buttonNext.setOnClickListener(this);
        buttonNext.setTag(PLAY_NEXT);

        buttonExoPrev.setOnClickListener(this);
        buttonExoPrev.setTag(PLAY_PREV);

        buttonExoNext.setOnClickListener(this);
        buttonExoNext.setTag(PLAY_NEXT);

        mPlayerView = view.findViewById(R.id.playerView);
    }


    // ********************************************************************************************* Uri Getters
    private Uri getVideoUrl(int step){
        String videoURL = recipeActivity.getSteps()[step].getVideoURL();
        int uriLenght = videoURL.length();
        boolean acceptedFormats = false;
        if(!videoURL.isEmpty()) {
            String ext = videoURL.substring(uriLenght - 4, uriLenght);
            acceptedFormats = (ext.equals(".mp4") || ext.equals(".M4A") || ext.equals(".FMP4")
                    || ext.equals(".MP3") || ext.equals(".WAV") || ext.equals(".FLV"));
        }

        if ((!videoURL.isEmpty()) && acceptedFormats) {
            return Uri.parse(videoURL).buildUpon()
                    .build();
        } else {
            return null;
        }
    }

    private Uri getThumbnailUrl(int step) {
        String thumbnailUrl = recipeActivity.getSteps()[step].getThumbnailURL();
        int uriLenght = thumbnailUrl.length();
        boolean acceptedFormats = false;
        if(!thumbnailUrl.isEmpty()) {
            String ext = thumbnailUrl.substring(uriLenght - 4, uriLenght);
           acceptedFormats = (ext.equals(".jpg") || ext.equals(".bmp") || ext.equals(".png"));
        }

        if ((!thumbnailUrl.isEmpty()) && acceptedFormats) { // ----------- Thumbnail URL from server
            return Uri.parse(thumbnailUrl).buildUpon()
                    .build();
        }else { // ------------------------------------------------ No Thumbnail URI to loacal Image
            return Uri.parse("android.resource://com.example.tomcat.bakingapp/"
                     + R.drawable.still);
        }
    }

    // ********************************************************************************************* Play Next / Prev Step
    public void playStep(int step) {
        if (mExoPlayer != null) releasePlayer();
        boolean uriHasVideo;
        Uri videoUri = getVideoUrl(step);
        if(videoUri != null){ // ------------------------------------------------------------- Video
            uriHasVideo = true;
            initializePlayer(videoUri, uriHasVideo);
        }else { // -------------------------------------------------- Image from JSON or local image
            Uri thumbUri = getThumbnailUrl(step);
            uriHasVideo = false;
            showThumbnail(thumbUri, uriHasVideo);
        }

        if (step == 0){
            buttonPrev.setAlpha(0.3f);
            buttonPrev.setEnabled(false);
            buttonExoPrev.setAlpha(0.3f);
            buttonExoPrev.setEnabled(false);
        }else{
            buttonPrev.setAlpha(1.0f);
            buttonPrev.setEnabled(true);
            buttonExoPrev.setAlpha(1.0f);
            buttonExoPrev.setEnabled(true);
        }

        if (step == numberOfSteps){
            buttonNext.setAlpha(0.3f);
            buttonNext.setEnabled(false);
            buttonExoNext.setAlpha(0.3f);
            buttonExoNext.setEnabled(false);

        }else{
            buttonNext.setAlpha(1.0f);
            buttonNext.setEnabled(true);
            buttonExoNext.setAlpha(1.0f);
            buttonExoNext.setEnabled(true);
        }

        // ----------------------------------------------------------------------- Set Reciepe Descr
        String descr = recipeActivity.getSteps()[RecipeActivity.currentStep].getRecipeDescription();
        recipeDesc.setText(descr);

        mCallback.onNextStepCallBack(step);
    }

    private void showThumbnail(Uri thumbUri, boolean uriHasVideo){
        final ImageView thumbView = rootView.findViewById(R.id.step_thumbnail_view);
        showVideoOrThumbnail(uriHasVideo);
        Picasso.with(getContext()).load(thumbUri)
                .into(thumbView);
    }


    private void initializePlayer(Uri uri, boolean uriHasVideo) {
        showVideoOrThumbnail(uriHasVideo);

        if(!Tools.twoPaneScreen(getContext()) && Tools.landscapeMode(getContext())){
            fullScreenPlayer();
        }

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(recipeActivity, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);
            mPlayerView.setBackgroundColor(Color.WHITE);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(recipeActivity,
                    VideoFragment.class.getSimpleName());

            // -------------------------------------------------------------------------- show Video
                mPlayerView.setDefaultArtwork(null);
                MediaSource mediaSource = new ExtractorMediaSource
                        (uri, new DefaultDataSourceFactory(recipeActivity, userAgent),
                                new DefaultExtractorsFactory(), null, null);

                if (mResumePosition != C.POSITION_UNSET) {
                    mExoPlayer.seekTo(mResumePosition);
                }

                mExoPlayer.prepare(mediaSource);
                mPlayerView.setUseController(true);
                mExoPlayer.setPlayWhenReady(true);
                mResumePosition = mExoPlayer.getCurrentPosition();
                mPlayerView.hideController();
        }
    }

    private void showVideoOrThumbnail(boolean videoOrTumbail){
        int constraintTo;

        final com.google.android.exoplayer2.ui.SimpleExoPlayerView
                playerView = rootView.findViewById(R.id.playerView);
        if (videoOrTumbail) {
            playerView.setVisibility(View.VISIBLE);
            tumbView.setVisibility(View.GONE);
            constraintTo = R.id.playerView;
        }else {
            playerView.setVisibility(View.GONE);
            tumbView.setVisibility(View.VISIBLE);
            constraintTo = R.id.step_thumbnail_view;
        }

        if (!Tools.landscapeMode(getContext()) || Tools.twoPaneScreen(getContext())) {
            ConstraintSet constraintSet = new ConstraintSet();
            ConstraintLayout aa = rootView.findViewById(R.id.step_layout);
            int marigin = (int) getContext().getResources().getDimension(R.dimen.steps_margin);
            constraintSet.clone(aa);
            constraintSet.connect(R.id.button_next_step, ConstraintSet.TOP,
                    constraintTo, ConstraintSet.BOTTOM, marigin);
            constraintSet.connect(R.id.button_prev_step, ConstraintSet.TOP,
                    constraintTo, ConstraintSet.BOTTOM, marigin);
            constraintSet.applyTo(aa);
        }
    }

    // ********************************************************************************************* Show Player in Full Screen
    private void fullScreenPlayer() {
        exoShutter.setBackgroundColor(Color.BLACK);
        buttonExoNext.setVisibility(View.GONE);
        buttonExoPrev.setVisibility(View.GONE);

        Dialog mFullScreenDialog = new Dialog(getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                    this.dismiss();
                    recipeActivity.navigateBack();
                    super.onBackPressed();
            }
        };
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenDialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            if (mResumePosition != C.POSITION_UNSET) mResumePosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
        }

        if (mExoPlayer != null) mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null){
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(mResumePosition);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) releasePlayer();
    }

    // ******************************************************************************************** Release ExoPlayer
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    // ********************************************************************************************* Buttons Next/Prex Step
    @Override
    public void onClick(View view) {
        int buttonTag = (int) view.getTag();

        switch (buttonTag) {
            case PLAY_NEXT:
                RecipeActivity.currentStep++;
                if (RecipeActivity.currentStep > numberOfSteps) {
                    RecipeActivity.currentStep = numberOfSteps;
                }

                break;
            case PLAY_PREV:
                    RecipeActivity.currentStep--;
                if (RecipeActivity.currentStep < 0) {
                    RecipeActivity.currentStep = 0;
                }
        }

        mResumePosition = 0;
        playStep(RecipeActivity.currentStep);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PAUSE_PLAY, playWhenReady);
        super.onSaveInstanceState(outState);
    }
    public interface VideoFragOnClickHandler {
        void onNextStepCallBack(int position);
    }
}
