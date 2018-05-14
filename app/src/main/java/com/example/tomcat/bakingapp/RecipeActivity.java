package com.example.tomcat.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.tomcat.bakingapp.models.Recipe;
import com.example.tomcat.bakingapp.models.Steps;
import com.example.tomcat.bakingapp.utilitis.Tools;
import com.google.android.exoplayer2.C;


/**
 * Recipe Activity
 */

public class RecipeActivity extends AppCompatActivity
        implements StepsAdapter.StepsAdapterOnClickHandler, VideoFragment.VideoFragOnClickHandler {
    Recipe selectedRecipe;
    Steps[] mSteps;

    private StepsFragment stepsFragment = null;
    private VideoFragment videoFragment;
    private final static String SAVED_CURRENT_FRAGM = "current_fragment";
    private final static String SAVED_VODEO_FRAGM = "video_fragment";
    private final static String CURRENT_STEP = "current_pos";
    public Fragment CURRENT_FRAGMENT;
    public static int currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentStep = savedInstanceState.getInt(CURRENT_STEP);
            if(!Tools.tableteMode(this)) {
                CURRENT_FRAGMENT = getSupportFragmentManager()
                        .getFragment(savedInstanceState, SAVED_CURRENT_FRAGM);

                videoFragment = (VideoFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, SAVED_VODEO_FRAGM);
            }
        }else{
            currentStep = 0;
        }

        selectedRecipe = getIntent().getParcelableExtra(MainActivity.SELECTED_RECIPE);
        if (selectedRecipe == null) closeOnError();
        getStepsFromBundle();

        setContentView(R.layout.activity_reciepe);
        Tools.setFontToActionBar(this, selectedRecipe.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragments();
    }

    // ********************************************************************************************* Reading data by Fragments
    public Steps[] getSteps(){
        return mSteps;
    }

    // ********************************************************************************************* Steps - Convert Parcelable to Array
    private void getStepsFromBundle(){
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        Bundle stepsBundle = extras.getBundle(MainActivity.SELECTED_PREPARATION_STEPS);

        if (stepsBundle != null){
            Parcelable[] stepsArray = stepsBundle.getParcelableArray(MainActivity.BUNDLE_PREPARATION_STEPS);

            assert stepsArray != null;
            mSteps = new Steps[stepsArray.length];

            for (int i = 0; i < stepsArray.length; i++){
                mSteps[i] = (Steps) stepsArray[i];
            }
        }
    }

    private Bundle getIngriedentsFromBundle(){
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        return extras.getBundle(MainActivity.SELECTED_INGRIEDENTS);
    }

    // ********************************************************************************************* Close on error
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    // ********************************************************************************************* Setting Fragments
    private void setFragments(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (stepsFragment == null) {
            stepsFragment = new StepsFragment();
            if (CURRENT_FRAGMENT == null) CURRENT_FRAGMENT = stepsFragment;
        }

        if(videoFragment == null) {
            videoFragment = new VideoFragment();
        }

        if (Tools.tableteMode(this)) { // ------------------------------------------------- for TWO PANE

            // Fragment Steps
            if(!checkIsFragmentInContainer(R.id.master_list_fragment)) { // ----- Container is Empty
                fragmentManager.beginTransaction()
                        .add(R.id.master_list_fragment, stepsFragment)
                        .commit();
            }else { // ------------------------------------------------------- Fragment is displayed
                fragmentManager.beginTransaction()
                        .replace(R.id.master_list_fragment, stepsFragment)
                        .commit();
            }

            // Fragment Video
            if(!checkIsFragmentInContainer(R.id.video_container)) { // ---------- Container is Empty
                fragmentManager.beginTransaction()
                        .add(R.id.video_container, videoFragment)
                        .commit();
            }else { // ------------------------------------------------------- Fragment is displayed
                fragmentManager.beginTransaction()
                        .replace(R.id.video_container, videoFragment)
                        .commit();
            }

        }else{ // ---------------------------------------------------------------------------------- for ONE PANE

            if ((CURRENT_FRAGMENT == stepsFragment)
                    && (checkIsFragmentInContainer(R.id.video_container))) {
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager()
                                .findFragmentById(R.id.video_container))
                        .commit();
            }

            if(!checkIsFragmentInContainer(R.id.master_list_fragment)){ // ------ Container is Empty
                fragmentManager.beginTransaction()
                        .add(R.id.master_list_fragment, CURRENT_FRAGMENT)
                        .commit();
            }else{  // ------------------------------------------------------- Fragment is displayed
                fragmentManager.beginTransaction()
                        .replace(R.id.master_list_fragment, CURRENT_FRAGMENT)
                        .commit();
            }
        }
    }

    private boolean checkIsFragmentInContainer(int id){
        boolean fragInContainer;
        Fragment fragmentInContainer = getSupportFragmentManager()
                .findFragmentById(id);
        fragInContainer = fragmentInContainer != null;
        return fragInContainer;
    }

    // ********************************************************************************************* Click on Step List
    @Override
    public void onClick(int position) {
        if (position == 0){ // --------------------------------------------------------------------- for Ingriedents
            Intent intent = new Intent(this, IngriedentsActivity.class);
            assert  getIngriedentsFromBundle() != null;
            intent.putExtra(MainActivity.SELECTED_INGRIEDENTS, getIngriedentsFromBundle());
            intent.putExtra(MainActivity.SELECTED_RECIPE, selectedRecipe);
            intent.putExtra(Intent.EXTRA_TEXT, selectedRecipe.getName());
            startActivity(intent);
        }else{ // ---------------------------------------------------------------------------------- for Steps
            currentStep = position - 1;
            if (Tools.tableteMode(this)){
                videoFragment.playStep(currentStep);
            }else{
                CURRENT_FRAGMENT = videoFragment;
                setFragments();
            }
        }
    }

    // ********************************************************************************************* Click Next/Prev Step
    @Override
    public void onNextStepCallBack(int position) {
        if((stepsFragment!= null) && Tools.tableteMode(this))stepsFragment.highlightStep();
    }

    // ********************************************************************************************* Navigate Back
    @Override
    public void onBackPressed(){
        navigateBack();
    }

    @Override
    public boolean onSupportNavigateUp(){
        navigateBack();
        return true;
    }

    public void navigateBack(){
        if((CURRENT_FRAGMENT == videoFragment) && !Tools.twoPaneScreen(this)){
            CURRENT_FRAGMENT = stepsFragment;
            setFragments();
        }else {
            finish();
        }
        VideoFragment.mResumePosition = C.POSITION_UNSET;
    }

    // ********************************************************************************************* Save Instance State
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, SAVED_CURRENT_FRAGM, CURRENT_FRAGMENT);
        if (CURRENT_FRAGMENT == videoFragment){
            getSupportFragmentManager().putFragment(outState, SAVED_VODEO_FRAGM, videoFragment);
        }
        outState.putInt(CURRENT_STEP, currentStep);
    }
}

