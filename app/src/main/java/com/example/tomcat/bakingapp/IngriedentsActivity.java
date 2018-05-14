package com.example.tomcat.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.tomcat.bakingapp.models.Ingredient;
import com.example.tomcat.bakingapp.models.Recipe;
import com.example.tomcat.bakingapp.utilitis.Tools;
import com.example.tomcat.bakingapp.widget.IngredientsWidget;

/**
 * Ingriedents List
 */

public class IngriedentsActivity extends AppCompatActivity{
    public Ingredient[] mIngriedents;
    IngriedentsAdapter adapter;
    private  Recipe selectedRecipe;
    String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingriedents_activity);

        selectedRecipe = getIntent().getParcelableExtra(MainActivity.SELECTED_RECIPE);
        assert selectedRecipe != null;
        title = getResources().getString(R.string.ingriedents_title)
                + " " + selectedRecipe.getName();
        Tools.setFontToActionBar(this, title);

        getIngriedentsFromBundle();
        initView();

        setDataToWidget();
    }

    private void initView() {
        RecyclerView.LayoutManager layoutManager;
        RecyclerView recyclerView = findViewById(R.id.ingriedents_recycler_view);
        adapter = new IngriedentsAdapter(mIngriedents, selectedRecipe, this);

        layoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    // ********************************************************************************************* Update Widget
    private void setDataToWidget(){
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        final int[] appWidgetIds = appWidgetManager.getAppWidgetIds
                (new ComponentName(this, IngredientsWidget.class));

        if (appWidgetIds.length > 0 ) {
            showToast();

            IngredientsWidget.updateIngriedentsWidgets(this, appWidgetManager, appWidgetIds,
                    mIngriedents, title);
        }
    }

    private void showToast(){
        Toast.makeText(this, getResources().getString(R.string.recipe_was_added),
                Toast.LENGTH_LONG).show();
    }

    // ********************************************************************************************* Steps - Convert Parcelable to Array
    private void getIngriedentsFromBundle(){
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        Bundle ingriedentsBundle = extras.getBundle(MainActivity.SELECTED_INGRIEDENTS);

        if (ingriedentsBundle != null){
            Parcelable[] stepsArray = ingriedentsBundle.getParcelableArray(MainActivity.BUNDLE_INGRIEDENTS);

            assert stepsArray != null;
            mIngriedents = new Ingredient[stepsArray.length];

            for (int i = 0; i < stepsArray.length; i++){
                mIngriedents[i] = (Ingredient) stepsArray[i];
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
