package com.sdp.movemeet.HomeScreen;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.util.regex.Pattern.matches;

@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> HomeScreenTestRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Test
    public void mainActivity_signIn() {

        onView(withId(R.id.signInButton)).perform(click());
    }

    @Test
    public void mainActivity_noAccount() {
        onView(withId(R.id.noAccountButton)).perform(click());
    }
}

