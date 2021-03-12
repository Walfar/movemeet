package com.sdp.movemeet;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdp.movemeet.Backend.BackendActivityManagerDemoTest.KEYBOARD_INPUT;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivity_sendMessage() {
        Intents.init();

        onView(ViewMatchers.withId(R.id.mainEditName))
                .perform(typeText(KEYBOARD_INPUT), closeSoftKeyboard());
        onView(withId(R.id.mainGoButton)).perform(click());
        Intents.release();
    }
}
