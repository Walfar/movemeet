package com.sdp.movemeet.view.profile;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.home.RegisterActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    public static final String TEST_FULL_NAME = "Yolo Test";
    public static final String TEST_EMAIL = "yolotest@gmail.com";
    public static final String TEST_PASSWORD = "123456";
    public static final String TEST_PHONE = "0798841817";

    private FirebaseAuth fAuth;
    private FirebaseUser user;

    @Rule
    // TODO: why is this ActivityScenarioRule not launching RegisterActivity, but MainActivity sometimes?
    public ActivityScenarioRule<RegisterActivity> testRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Before
    public void createAccount() {
        onView(withId(R.id.edit_text_full_name)).perform(replaceText(TEST_FULL_NAME), closeSoftKeyboard());
        onView(withId(R.id.edit_text_email)).perform(replaceText(TEST_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password)).perform(replaceText(TEST_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.edit_text_phone)).perform(replaceText(TEST_PHONE), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }
    }


    @Test
    public void deleteAccount() {

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

        // Launching directly ProfileActivity (even if the "rule" is set to RegisterActivity)
        try (ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(ProfileActivity.class)) {

            clickDeleteAccountButton();

        }
        catch (Exception e) {
            Log.d("TAG", "deleteAccount Exception: " + e);
            e.printStackTrace();
        }

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            assert (false);
        }

        // TODO: check if the userId still exists or not (to have some kind of assertion here!)
        user = fAuth.getCurrentUser();
    }


    public void clickDeleteAccountButton() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }
        onView(withId(R.id.button_delete_account)).perform(forceClick());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            assert (false);
        }
    }


    public static ViewAction forceClick() {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return allOf(isClickable(), isEnabled(), isDisplayed());
            }

            @Override public String getDescription() {
                return "force click";
            }

            @Override public void perform(UiController uiController, View view) {
                view.performClick(); // perform click without checking view coordinates.
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

}

