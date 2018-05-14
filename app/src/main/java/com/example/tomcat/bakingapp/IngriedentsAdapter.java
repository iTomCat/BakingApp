package com.example.tomcat.bakingapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.PluralsRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomcat.bakingapp.models.Ingredient;
import com.example.tomcat.bakingapp.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  Adapter for Ingriedents List
 */

public class IngriedentsAdapter extends RecyclerView.Adapter<IngriedentsAdapter.IngrViewHolder> {
    private Ingredient[] mIngriedents;
    private Context mContext;
    private Recipe mSelectedRecipe;


    public IngriedentsAdapter(Ingredient[] ingredients, Recipe selectedRecipe, Context context) {
        this.mIngriedents = ingredients;
        this.mSelectedRecipe = selectedRecipe;
        this.mContext = context;
    }


    @Override
    public IngriedentsAdapter.IngrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingriedents_list_row,
                parent, false);
        return new IngrViewHolder(view);
    }


    class IngrViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_ing_name) TextView tvName;
        @BindView(R.id.tv_quantity) TextView tvQuant;

        IngrViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onBindViewHolder(IngrViewHolder holder, int position) {

        if (position == 0){ // ------------------------------------------------------- Servings Info
            int servings = mSelectedRecipe.getmServings();
            holder.tvName.setVisibility(View.INVISIBLE);
            holder.tvName.setTextSize(mContext.getResources().getDimension(R.dimen.small_marigin));
            String servingInfo = mContext.getResources().getString(R.string.reciepe_for)
                    + " " + String.valueOf(servings) + " "
                    + getPluralMeasure ("SERW",servings, mContext);
            holder.tvQuant.setText(servingInfo);
            holder.tvQuant.setTextSize(mContext.getResources().getDimension(R.dimen.font_size));
            holder.tvQuant.setTypeface(null, Typeface.BOLD);
        }else { // --------------------------------------------------------------------- Ingriedents
            holder.tvName.setText(mIngriedents[position].getIngredient());

            String ingriedent = mIngriedents[position].getMeasure();
            double quantity = mIngriedents[position].getQuantity();
            String currentquantity = doubleFormat(quantity) + " "
                    + getPluralMeasure(ingriedent, quantity, mContext);
            holder.tvQuant.setText(currentquantity);
            holder.tvQuant.setAllCaps(true);

        }
    }

    //  ---------------------------------------------------- remove decimal when the number is total
    public static String doubleFormat(double d) {
        return (d == (long) d) ? (String.valueOf((int)d)) : (String.format("%s",d));
    }

    // ------------------------------------------------------------- singular or plural type of name
    public static String getPluralMeasure (String measure, double quantity, Context context){
        @PluralsRes int resourceId = R.plurals.measure_empty;

        if (!measure.isEmpty()){
            switch (measure) {
                case "CUP": // ----------------------------------------------------------------- cup
                    resourceId = R.plurals.measure_cup;
                    break;
                case "TBLSP": // -------------------------------------------------------- tablespoon
                    resourceId = R.plurals.measure_tablespoon;
                    break;
                case "TSP": // ------------------------------------------------------------ teaspoon
                    resourceId = R.plurals.measure_teaspoon;
                    break;
                case "K": //--------------------------------------------------------------------- Kg
                    resourceId = R.plurals.measure_kilogram;
                    break;
                case "G": //------------------------------------------------------------------- gram
                    resourceId = R.plurals.measure_gram;
                    break;
                case "OZ": //-------------------------------------------------------------------- oz
                    resourceId = R.plurals.measure_oz;
                    break;
                case "UNIT": //---------------------------------------------------------------- unit
                    resourceId = R.plurals.measure_unit;
                    break;
                case "SERW": //---------------------------------------------------------------- unit
                    resourceId = R.plurals.servings;
            }
        }
        return context.getResources().getQuantityString(resourceId, (round(quantity)));
    }

    private static int round(double d){
        return (d < 1) ? (1) : (int) (d);
    }

    @Override
    public int getItemCount() {
        return mIngriedents.length;
    }

}
