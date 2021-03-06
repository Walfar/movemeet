package com.sdp.movemeet.view.home;

import android.Manifest;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.main.MainActivity;

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
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
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
}
