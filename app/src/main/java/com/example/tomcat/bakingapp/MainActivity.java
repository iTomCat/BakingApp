package com.example.tomcat.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tomcat.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.tomcat.bakingapp.json.BakingService;
import com.example.tomcat.bakingapp.models.Ingredient;
import com.example.tomcat.bakingapp.models.Recipe;
import com.example.tomcat.bakingapp.models.Steps;
import com.example.tomcat.bakingapp.utilitis.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainAdapter.ReciepeAdapterOnClickHandler {
    @BindView(R.id.no_internet_conn) ImageView noInternetConn;
    public static Typeface robotoFont;
    public static Typeface pacificoFont;
    private ArrayList<Recipe> mRecipes = null;

    @Override
    protected void onStart() {
        super.onStart();
        if (mRecipes == null) getJsonData();
    }

    public MainAdapter.ReciepeAdapterOnClickHandler mClickHandler = this;
    public static final String RECIPES = "reciepes";
    public static final String SELECTED_RECIPE = "selected_reciepe";
    public static final String SELECTED_INGRIEDENTS = "selected_ingriedents";
    public static final String BUNDLE_INGRIEDENTS = "bundle_ingriedents";
    public static final String SELECTED_PREPARATION_STEPS = "selected_steps";
    public static final String BUNDLE_PREPARATION_STEPS = "bundle_steps";
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        robotoFont = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        pacificoFont = Typeface.createFromAsset(this.getAssets(), "fonts/Pacifico-Regular.ttf");

        if (savedInstanceState == null) {
            getJsonData();
        } else {
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES);
            if (mRecipes != null) initView();
        }
        Tools.setFontToActionBar(this);
    }

    // ********************************************************************************************* OnClick List
    @Override
    public void onClick(int position) {
       startReciepeActivity(position);
    }

    // --------------------------------------------------------------- Start Detail Reciepe Activity
    private void startReciepeActivity(int position){
        Recipe selectedReciepe = mRecipes.get(position);

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(SELECTED_RECIPE, selectedReciepe);
        intent.putExtra(SELECTED_INGRIEDENTS, putIngrToBundle(selectedReciepe.getIngredients()));
        intent.putExtra(SELECTED_PREPARATION_STEPS, putStepsToBundle(selectedReciepe.getSteps()));
        startActivity(intent);
    }

    // ********************************************************************************************* Adding Steps and Ingriedents to Bundle
    Bundle putStepsToBundle(List<Steps> steps){
            Steps[] stepsFromSelRecipe = steps.toArray(new Steps[steps.size()]);
            Bundle bundle = new Bundle();
            bundle.putParcelableArray(BUNDLE_PREPARATION_STEPS, stepsFromSelRecipe);
            return bundle;
    }
    Bundle putIngrToBundle(List<Ingredient> ingredients){
        Ingredient[] stepsFromSelRecipe = ingredients.toArray(new Ingredient[ingredients.size()]);
        Bundle bundle = new Bundle();
        bundle.putParcelableArray(BUNDLE_INGRIEDENTS, stepsFromSelRecipe);
        return bundle;
    }

    // ********************************************************************************************* Get JSON Data
    void getJsonData() {
        if (mIdlingResource != null) mIdlingResource.setIdleState(false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BakingService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BakingService bakingClient = retrofit.create(BakingService.class);

        Call<List<Recipe>> call = bakingClient.getRecipes();

        call.enqueue(new Callback<List<Recipe>>(){
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,
                                   @NonNull Response<List<Recipe>> response){

                mRecipes = (ArrayList<Recipe>) response.body();
                if (mRecipes != null) initView();
                noInternetConn.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.e(MainActivity.this.getClass().getSimpleName(), t.getMessage());
                if (t instanceof IOException) {
                    setContentView(R.layout.activity_main);
                    ButterKnife.bind(MainActivity.this);
                    noInternetConn.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // ********************************************************************************************* init View
    private void initView(){
        RecyclerView.LayoutManager layoutManager;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean portraitMode =  getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_PORTRAIT;

        if (!isTablet && portraitMode){ // --------------------------------------------------------- Phone vertically
            layoutManager = new LinearLayoutManager
                    (MainActivity.this, LinearLayoutManager.VERTICAL, false);
        }else{ // ---------------------------------------------------------------------------------- Phone horizontally and Tablet
            layoutManager = new GridLayoutManager
                    (MainActivity.this, 2);
        }

        MainAdapter adapter = new MainAdapter(mRecipes, mClickHandler);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView recyclerView = findViewById(R.id.card_recycler_view_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        @VisibleForTesting
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIdlingResource != null) mIdlingResource.setIdleState(true);
            }
        }, 1000);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPES, mRecipes);
    }


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if (mIdlingResource == null){
            mIdlingResource = new SimpleIdlingResource();}
        return mIdlingResource;
    }
}
