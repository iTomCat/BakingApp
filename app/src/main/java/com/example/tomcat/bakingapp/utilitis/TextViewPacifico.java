package com.example.tomcat.bakingapp.utilitis;

import android.content.Context;
import android.util.AttributeSet;

import com.example.tomcat.bakingapp.MainActivity;

/**
 * TextView with Pacifico
 */

public class TextViewPacifico extends android.support.v7.widget.AppCompatTextView {

    public TextViewPacifico(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewPacifico(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewPacifico(Context context) {
        super(context);
        init();
    }

    public void init() {
        //setTextSize(28);
        setTypeface(MainActivity.pacificoFont);
        //setTextColor(MainActivity.mainColor);
    }
}
