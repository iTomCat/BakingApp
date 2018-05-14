package com.example.tomcat.bakingapp.utilitis;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;

import com.example.tomcat.bakingapp.MainActivity;
import com.example.tomcat.bakingapp.R;


/**
 * useful tools methods :)
 */

public class Tools {

    // ********************************************************************************************* checking if it is Tablet Mode with Two Pan
    public static boolean twoPaneScreen(Context context){
        boolean mode;
        boolean landscapeMode =  context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        boolean tabletMode = context.getResources().getBoolean(R.bool.isTablet);
        mode = landscapeMode && tabletMode;
        return mode;
    }

    public static boolean landscapeMode(Context context){
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean tableteMode(Context context){
        return context. getResources().getBoolean(R.bool.isTablet);
    }

    // ********************************************************************************************* set nice font to ActionBar
    public static void setFontToActionBar(AppCompatActivity activity){
        SpannableString spannable = new SpannableString(activity.getResources().getString(R.string.app_name));
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(spannable);

        spannable.setSpan (new CustomTypefaceSpan("", MainActivity.pacificoFont),
                0, spannable.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        actionBar.setTitle(spannable);
    }

    public static void setFontToActionBar(AppCompatActivity activity, String reciepeName){
        SpannableString spannable = new SpannableString(reciepeName);
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(spannable);

        spannable.setSpan (new CustomTypefaceSpan("", MainActivity.pacificoFont),
                0, spannable.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        actionBar.setTitle(spannable);
    }

}
