package com.example.tomcat.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Prepare Steps Model Class
 */

public class Steps implements Parcelable {

    // --------------------------------------------------------------------------------------------- short description
    @SerializedName("shortDescription")
    private String shortDescription;

    // --------------------------------------------------------------------------------------------- description
    @SerializedName("description")
    private String recipeDescription;

    // --------------------------------------------------------------------------------------------- video URL
    @SerializedName("videoURL")
    private String videoURL;

    // --------------------------------------------------------------------------------------------- thumbnail URL
    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    private Steps(Parcel in) {
        shortDescription = in.readString();
        recipeDescription = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortDescription);
        parcel.writeString(recipeDescription);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    // --------------------------------------------------------------------------------------------- GETTERS
    public String getShortDescription()
    {
        return shortDescription;
    }

    public String getRecipeDescription()
    {
        return recipeDescription;
    }

    public String getVideoURL()
    {
        return videoURL;
    }

    public String getThumbnailURL()
    {
        return thumbnailURL;
    }
}
