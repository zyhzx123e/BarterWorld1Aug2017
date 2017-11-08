package com.example.jason.barterworld;

/**
 * Created by User on 10/2/2017.
 */


import android.content.Context;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TestDataFlow {



    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>( MainActivity.class);



    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.jason.barterworld", appContext.getPackageName());
    }


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void activityLaunch() {

       // onView(withId(R.id.post_recycler_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

      try {
          onView(withId(R.id.post_recycler_list)).check(matches(isDisplayed()));
//nav_interested_barters

          Thread.sleep(5000);//wait for internet connection which is too slow in some situation
         // onView(withRecyclerView(R.id.post_recycler_list).atPositionOnView(0, R.id.img_post)).perform(click());
          onView(withId(R.id.post_recycler_list)).perform(

                  // First position the recycler view. Necessary to allow the layout
                  // manager perform the scroll operation
                  RecyclerViewActions.scrollToPosition(0),

                  // Click the item to trigger navigation to detail view
                  RecyclerViewActions.actionOnItemAtPosition(0, click())
          );
         // onView(withId(R.id.post_recycler_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.img_post)));
          Thread.sleep(5000);//wait for internet connection which is too slow in some situation

      }catch (Exception e){

      }
    }





}
