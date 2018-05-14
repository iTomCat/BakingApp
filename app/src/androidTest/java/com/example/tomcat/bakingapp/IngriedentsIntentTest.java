package com.example.tomcat.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Ingriedents Intent Test
 */

@RunWith(AndroidJUnit4.class)
public class IngriedentsIntentTest {
    private static final String RECIEPE_NAME = "Brownies";

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(
            MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickRecycleViewItem_OpenReciepeActivity() {
        onView(withId(R.id.card_recycler_view_main))
                .perform(actionOnItemAtPosition(1, click()));
    }

    @Test
    public void clickIngriedents_SendsName() {

        clickRecycleViewItem_OpenReciepeActivity();
        onView(withId(R.id.steps_card))
                .perform(actionOnItemAtPosition(0, click()));
        intended(allOf(
                hasExtra(Intent.EXTRA_TEXT, RECIEPE_NAME)));
    }
}
