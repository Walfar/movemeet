package com.sdp.movemeet.HomeScreen;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.HomeUnregister;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.R;
;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> homeScreenTestRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    public FirebaseAuth fAuth;

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
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) fAuth.signOut();
        sleep(2000);
        onView(withId(R.id.signInButton)).perform(click());
        sleep(2000);
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void signInHasCorrectIntentWhenLogged()  {
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) fAuth.signOut();
        sleep(2000);
        fAuth.signInWithEmailAndPassword("test@test.com", "password");
        sleep(2000);
        onView(withId(R.id.signInButton)).perform(click());
        sleep(2000);
        intended(hasComponent(MainActivity.class.getName()));
        fAuth.signOut();
        sleep(2000);
    }


    @Test
    public void mainActivity_noAccount() {
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) fAuth.signOut();

        ActivityScenario scenario = ActivityScenario.launch(HomeScreenActivity.class);

        assert(sleep(1000));
        onView(withId(R.id.noAccountButton)).perform(click());

        intended(hasComponent(HomeUnregister.class.getName()));
    }

    public boolean sleep(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }
}

