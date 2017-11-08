package com.example.jason.barterworld;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

/**
 * Created by User on 10/3/2017.
 */
public class SearchTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>( MainActivity.class);


private  MainActivity mainActivity = null;

    Instrumentation.ActivityMonitor singleBarterView = getInstrumentation().addMonitor(BarterSingleActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
    mainActivity = mActivityRule.getActivity();

    }

    @Test
    public  void testSearch(){
        RecyclerView  recyclerView = (RecyclerView)mainActivity.findViewById(R.id.post_recycler_list);
        assertNotNull(recyclerView);

        try {
            onView(withId(R.id.post_recycler_list)).check(matches(isDisplayed()));
//nav_interested_barters

            Thread.sleep(4000);//wait for internet connection which is too slow in some situation
//btn_goods
//btn_services
            onView(withId(R.id.btn_goods)).perform(click());
            Thread.sleep(4000);
            onView(withId(R.id.btn_services)).perform(click());
            Thread.sleep(4000);
           final EditText titleInput = (EditText) mainActivity.findViewById(R.id.txt_barter);
            getInstrumentation().runOnMainSync(new Runnable() {
                public void run() {
                    titleInput.setText("wax");
                }
            });
            Thread.sleep(2000);
            onView(withId(R.id.btn_barter_search)).perform(click());Thread.sleep(4000);

            //btn_barter_search
            // onView(withRecyclerView(R.id.post_recycler_list).atPositionOnView(0, R.id.img_post)).perform(click());


            onView(withId(R.id.txt_barter)).perform(typeText("This is the test case for Barter Searching"));

            //  BarterSingleAct.finish();
            // onView(withId(R.id.post_recycler_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.img_post)));
            Thread.sleep(5000);//wait for internet connection which is too slow in some situation

        }catch (Exception e){

        }

    }

    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }

    @Test
    public void onStart() throws Exception {

    }

}