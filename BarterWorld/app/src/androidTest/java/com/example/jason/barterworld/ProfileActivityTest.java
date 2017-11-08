package com.example.jason.barterworld;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by User on 10/3/2017.
 */
public class ProfileActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>( MainActivity.class);

    private MainActivity mainActivity=null;


    @Before
    public void setUp() throws Exception {
        mainActivity=mActivityRule.getActivity();
    }

    @Test
    public  void testProfileActivity(){
        try {
            RecyclerView recyclerView = (RecyclerView)mainActivity.findViewById(R.id.post_recycler_list);
            assertNotNull(recyclerView);

            onView(withId(R.id.post_recycler_list)).check(matches(isDisplayed())); Thread.sleep(2000);

            onView(withId(R.id.activity_main)).perform(open());
            // onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_barter_map));
            Thread.sleep(2000);
            onView(withText("Profile")).perform(click());

            Thread.sleep(5000);//wait for internet connection which is too slow in some situation



//txt_msg_send

onView(withId(R.id.btn_edit_info)).perform(click()); Thread.sleep(2000);


            onView(withId(R.id.txt_post_name)).perform(typeText("This is the test case for Manage Profile View"));

            // ChatAct1.finish();
            Thread.sleep(5000);
            Espresso.pressBack();

        }catch (Exception e){

        }
    }



    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }

}