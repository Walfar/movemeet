package com.sdp.movemeet.view.home;

import android.Manifest;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.map.GPSRecordingActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityLoggedTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> homeScreenTestRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    FirebaseAuth fAuth;
    FirebaseUser user;

    @Before
    public void setUp() {
        Intents.init();
        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("test@test.com", "password").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                user = fAuth.getCurrentUser();
            }
        });
    }

    @After
    public void tearDown() {
        Intents.release();
        fAuth.signOut();
    }

    @Test
    public void signInHasCorrectIntentWhenLogged() throws InterruptedException {
        while (user == null) Thread.sleep(2000);
        onView(withId(R.id.signInButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }


    @Test
    public void noAccountHasCorrectIntentWhenLogged() throws InterruptedException {
        while (user == null) Thread.sleep(2000);
        onView(withId(R.id.noAccountButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void recordButtonHasCorrectIntent() {
        onView(withId(R.id.recordRunButton)).perform(click());
        intended(hasComponent(GPSRecordingActivity.class.getName()));
    }

    public static ViewAction forceDoubleClick() {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return allOf(isClickable(), isEnabled(), isDisplayed());
            }

            @Override public String getDescription() {
                return "force click";
            }

            @Override public void perform(UiController uiController, View view) {
                view.performClick(); // perform click without checking view coordinates.
                view.performClick();
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

}
