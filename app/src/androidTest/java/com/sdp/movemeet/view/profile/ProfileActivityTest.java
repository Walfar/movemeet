package com.sdp.movemeet.view.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ProfileActivityTest {

    private static final String TEST_NAME = "Antho1";
    private static final String TEST_EMAIL = "antho1@gmail.com";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_PHONE = "+41798841818";
    private static final String TEST_DESCRIPTION = "Hi there!";

    private FirebaseAuth fAuth;

    @Before
    public void signOutAndSignIn() {

        // Signing out
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        if (fAuth.getCurrentUser() != null) {
            fAuth.signOut();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }

        // Logging in
        CountDownLatch latch = new CountDownLatch(1);
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signInWithEmailAndPassword(TEST_EMAIL, TEST_PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }

        ActivityScenario scenario = ActivityScenario.launch(ProfileActivity.class);

    }

    @Test
    public void verifyUserInformation() {

        // Launching directly ProfileActivity
        try (ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(ProfileActivity.class)) {

            onView(withId(R.id.text_view_activity_profile_name)).check(matches(withText(TEST_NAME)));
            onView(withId(R.id.text_view_activity_profile_email)).check(matches(withText(TEST_EMAIL)));
            onView(withId(R.id.text_view_activity_profile_phone)).check(matches(withText(TEST_PHONE)));
            onView(withId(R.id.text_view_activity_profile_description)).check(matches(withText(TEST_DESCRIPTION)));

            onView(withId(R.id.button_update_profile)).perform(click());

        }
        catch (Exception e) {
            Log.d("TAG", "Error message: " + e);
            e.printStackTrace();
        }

    }

    @After
    public void signOut() {
        fAuth.signOut();
    }

}
