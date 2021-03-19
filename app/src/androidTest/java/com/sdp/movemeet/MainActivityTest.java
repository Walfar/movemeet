package com.sdp.movemeet;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class MainActivityTest {

    public static final String KEYBOARD_INPUT = "TEST_INPUT_00192qa19";


    @Rule
    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivity_sendMessage() {
        Intents.init();

        onView(ViewMatchers.withId(R.id.mainEditName))
                .perform(replaceText(KEYBOARD_INPUT), closeSoftKeyboard());
        onView(withId(R.id.mainGoButton)).perform(click());


        intended(hasExtra(MainActivity.EXTRA_MESSAGE, KEYBOARD_INPUT));

        Intents.release();


    }

//    @Test
//    public void mainActivity_logout() {
//        Intents.init();
//
//        onView(withId(R.id.button_logout)).perform(click());
//
//        Intents.release();
//    }

    @Test
    public void mainActivityToProfileActivity() {
        Intents.init();

        onView(withId(R.id.button_user_profile)).perform(click());

        Intents.release();
    }

}