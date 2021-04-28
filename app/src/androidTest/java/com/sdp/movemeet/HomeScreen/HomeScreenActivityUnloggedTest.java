package com.sdp.movemeet.HomeScreen;

import android.Manifest;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.MainUnregister;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.R;
;
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
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void signInHasCorrectIntentWhenUnlogged()  {
        onView(withId(R.id.signInButton)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void noAccountHasCorrectIntentWhenUnlogged() {
        onView(withId(R.id.noAccountButton)).perform(click());
        intended(hasComponent(MainUnregister.class.getName()));
    }
}

