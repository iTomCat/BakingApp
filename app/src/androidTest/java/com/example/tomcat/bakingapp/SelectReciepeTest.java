package com.example.tomcat.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 * test that checks whether the appropriate activity with the recipe steps is triggered

 */

@RunWith(AndroidJUnit4.class)
public class SelectReciepeTest {
    private static final String RECIEPE_NAME = "Nutella Pie";
    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }


    @Test
    public void clickRecycleViewItem_OpenReciepeActivity() {
        onView(withId(R.id.card_recycler_view_main))
                .perform(actionOnItemAtPosition(0, click()));

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(ViewAssertions.matches(withText(RECIEPE_NAME)));
    }

    @After
    public void unregisterIdlingResource(){
        if (mIdlingResource != null) IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}

