package com.example.jason.barterworld;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.internal.NavigationSubMenu;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by User on 10/3/2017.
 */
public class MapActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>( MainActivity.class);


    private  MainActivity mainActivity = null;
    Instrumentation.ActivityMonitor mapView = getInstrumentation().addMonitor(MapActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mainActivity = mActivityRule.getActivity();
    }

    @Test
    public  void testMapSetup(){

        try {
            RecyclerView recyclerView = (RecyclerView)mainActivity.findViewById(R.id.post_recycler_list);
            assertNotNull(recyclerView);

            onView(withId(R.id.post_recycler_list)).check(matches(isDisplayed())); Thread.sleep(2000);

            onView(withId(R.id.activity_main)).perform(open());
           // onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_barter_map));
            Thread.sleep(2000);
            onView(withText("Barter Map=>Post Barter")).perform(click());
           // onView(withId(R.id.nav_barter_map)).perform(click());
         // onData(allOf(withId(R.id.nav_barter_map), withParent(withId(R.id.nav_view)))).perform(click()); Thread.sleep(2000);
            Activity MapAct = getInstrumentation().waitForMonitorWithTimeout(mapView,5000);
           assertNotNull(MapAct);
             Thread.sleep(10000);//wait for internet connection which is too slow in some situation

        }catch (Exception e){

        }

    }


    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }

}