package com.example.tomcat.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Ingredient Model Class
 */

public class Ingredient implements Parcelable {

    // --------------------------------------------------------------------------------------------- Quantity
    @SerializedName("quantity")
    private double quantity;

    // --------------------------------------------------------------------------------------------- Measure
    @SerializedName("measure")
    private String measure;

    // --------------------------------------------------------------------------------------------- Ingredient
    @SerializedName("ingredient")
    private String ingredient;


    private Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    // --------------------------------------------------------------------------------------------- GETTERS
    public double getQuantity()
    {
        return quantity;
    }

    public String getMeasure()
    {
        return measure;
    }

    public String getIngredient()
    {
        return ingredient;
    }
}
