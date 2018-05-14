package com.example.tomcat.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Recipe Data
 */

public class Recipe implements Parcelable{

    // --------------------------------------------------------------------------------------------- id
    @SerializedName("id")
    private int id;

    // --------------------------------------------------------------------------------------------- name
    @SerializedName("name")
    private String name;

    // --------------------------------------------------------------------------------------------- ingredients
    @SerializedName("ingredients")
    private List<Ingredient> ingredients;


    // --------------------------------------------------------------------------------------------- steps
    @SerializedName("steps")
    private List<Steps> steps;

    //---------------------------------------------------------------------------------------------- servings
    @SerializedName("servings")
    private int serving;

    //---------------------------------------------------------------------------------------------- image
    @SerializedName("image")
    private String image;

    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        serving = in.readInt();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(serving);
        parcel.writeString(image);
    }


    // --------------------------------------------------------------------------------------------- GETTERS
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public List<Steps> getSteps()
    {
        return steps;
    }

    public int getmServings()
    {
        return serving;
    }

    public String getImage()
    {
        return image;
    }
}
