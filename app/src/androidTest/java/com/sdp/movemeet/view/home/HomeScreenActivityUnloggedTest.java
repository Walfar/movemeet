package com.sdp.movemeet.view.home;

import android.Manifest;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.sdp.movemeet.R;
import com.sdp.movemeet.view.main.MainUnregisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityUnloggedTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> homeScreenTestRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Rule
    public GrantPermissionRule runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void signInHasCorrectIntentWhenUnlogged() {
        onView(withId(R.id.signInButton)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void noAccountHasCorrectIntentWhenUnlogged() {
        onView(withId(R.id.noAccountButton)).perform(click());
        intended(hasComponent(MainUnregisterActivity.class.getName()));
    }
}

