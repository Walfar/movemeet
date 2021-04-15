package com.sdp.movemeet.HomeScreen;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.util.regex.Pattern.matches;

@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityTest {

    /*@Rule
    public ActivityScenarioRule<HomeScreenActivity> HomeScreenTestRule = new ActivityScenarioRule<>(HomeScreenActivity.class);*/

    public FirebaseAuth fAuth;

    @Test
    public void mainActivity_signIn() {
        CountDownLatch latch = new CountDownLatch(1);

        fAuth = FirebaseAuth.getInstance();

        fAuth.signInWithEmailAndPassword("movemeet@gmail.com", "password")
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        latch.countDown();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assert(false);
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert(false);
        }

        ActivityScenario scenario = ActivityScenario.launch(HomeScreenActivity.class);

        assert(sleep(1000));

        onView(withId(R.id.signInButton)).perform(click());

        // Assert that the correct intent is fired here
    }

    @Test
    public void mainActivity_noAccount() {
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) fAuth.signOut();

        ActivityScenario scenario = ActivityScenario.launch(HomeScreenActivity.class);

        assert(sleep(1000));
        onView(withId(R.id.noAccountButton)).perform(click());

        // Assert that the correct Intent is fired here
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

