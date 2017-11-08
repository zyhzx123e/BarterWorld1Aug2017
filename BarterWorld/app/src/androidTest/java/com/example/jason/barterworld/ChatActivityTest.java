package com.example.jason.barterworld;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
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
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by User on 10/3/2017.
 */
public class ChatActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>( MainActivity.class);
    public ActivityTestRule<OutgoingQuotesActivity> cActivityRule = new ActivityTestRule<OutgoingQuotesActivity>( OutgoingQuotesActivity.class);
    public ActivityTestRule<IncomingQuotesActivity> cActivityRule1 = new ActivityTestRule<IncomingQuotesActivity>( IncomingQuotesActivity.class);
    public ActivityTestRule<ChatActivity> chatActivityRule = new ActivityTestRule<ChatActivity>( ChatActivity.class);



    private  MainActivity mainActivity = null;
    private  OutgoingQuotesActivity outgoingQuotesActivity =null;
    private  IncomingQuotesActivity incomingQuotesActivity =null;
    private  ChatActivity chatActivity=null;

    Instrumentation.ActivityMonitor chatView = getInstrumentation().addMonitor(OutgoingQuotesActivity.class.getName(),null,false);
    Instrumentation.ActivityMonitor chatView1 = getInstrumentation().addMonitor(IncomingQuotesActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mainActivity = mActivityRule.getActivity();
        outgoingQuotesActivity = cActivityRule.getActivity();
        incomingQuotesActivity = cActivityRule1.getActivity();
        chatActivity=chatActivityRule.getActivity();
    }

    @Test
    public  void testChatSetup(){

        try {
            RecyclerView recyclerView = (RecyclerView)mainActivity.findViewById(R.id.post_recycler_list);
            assertNotNull(recyclerView);

            onView(withId(R.id.post_recycler_list)).check(matches(isDisplayed())); Thread.sleep(2000);

            onView(withId(R.id.activity_main)).perform(open());
            // onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_barter_map));
            Thread.sleep(2000);
            onView(withText("My Interested Barters")).perform(click());
            // onView(withId(R.id.nav_barter_map)).perform(click());
            // onData(allOf(withId(R.id.nav_barter_map), withParent(withId(R.id.nav_view)))).perform(click()); Thread.sleep(2000);
             Activity ChatAct = getInstrumentation().waitForMonitorWithTimeout(chatView,5000);
              assertNotNull(ChatAct);
            Thread.sleep(5000);//wait for internet connection which is too slow in some situation

            //incoming_quote_recycler_list
           // onView(withId(R.id.outgoing_quote_recycler_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));Thread.sleep(5000);
//txt_msg_send
            //onView(withId(R.id.txt_msg_send)).perform(typeText("This is the test case for Outgoing Chat View"));

            Thread.sleep(5000);

            Espresso.pressBack();
//outgoing_quote_recycler_list
            onView(withId(R.id.activity_main)).perform(open()); Thread.sleep(2000);
            onView(withText("Quotes To Me")).perform(click());
            Activity ChatAct1 = getInstrumentation().waitForMonitorWithTimeout(chatView1,5000);
            assertNotNull(ChatAct1);
            Thread.sleep(5000);//wait for internet connection which is too slow in some situation

            onView(withId(R.id.incoming_quote_recycler_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));Thread.sleep(5000);

//txt_msg_send

            onView(withId(R.id.txt_msg_send)).perform(typeText("This is the test case for  Chat View"));

            // ChatAct1.finish();
            Thread.sleep(5000);
            Espresso.pressBack();

        }catch (Exception e){

        }

    }


    @After
    public void tearDown() throws Exception {
        mainActivity=null;
        outgoingQuotesActivity=null;
        incomingQuotesActivity=null;
        chatActivity=null;
    }

}