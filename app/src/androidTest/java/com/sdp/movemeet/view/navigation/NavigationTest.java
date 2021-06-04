package com.sdp.movemeet.view.navigation;

import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.profile.ProfileActivity;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


public class NavigationTest {

    private FirebaseDatabase mDatabase;
    private FirebaseAuth fAuth;


    @Before
    public void signIn() {
        Navigation.profileField = false;
        MainActivity.enableNav = true;
        UploadActivityActivity.enableNav = true;
        ActivityDescriptionActivity.enableNav = true;
        ChatActivity.enableNav = true;
        ProfileActivity.enableNav = true;

        CountDownLatch latch = new CountDownLatch(1);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signInWithEmailAndPassword("movemeet@gmail.com", "password").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                latch.countDown();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assert (false);
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert (false);
        }

        ActivityScenario scenario = ActivityScenario.launch(UploadActivityActivity.class);
        Intents.init();
    }

    @Test
    public void NavigationTest() {

        //ActivityScenario scenario = ActivityScenario.launch(MainActivity.class);
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        onView(withId(R.id.nav_home)).perform(click());


        try {
            Thread.sleep(1500);
        } catch (Exception e) {
        }
        Intents.intended(allOf(hasComponent(MainActivity.class.getName())));

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        onView(withId(R.id.nav_add_activity)).perform(click());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        intended(hasComponent(UploadActivityActivity.class.getName()));

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        onView(withId(R.id.nav_view)).perform(swipeUp());
        onView(withId(R.id.nav_edit_profile)).perform(click());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        intended(hasComponent(ProfileActivity.class.getName()));
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        onView(withId(R.id.nav_view)).perform(swipeUp());
        onView(withId(R.id.nav_logout)).perform(click());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        intended(hasComponent(LoginActivity.class.getName()));
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        Intents.release();

    }

    @Test
    public void MainDialogBox() {

        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        onView(withId(R.id.nav_home)).perform(click());

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        Espresso.pressBack();

        onView(withText("Confirm exit ?")).check(matches(isDisplayed()));
        onView(withText("No")).perform(click());
        Intents.intended(allOf(hasComponent(MainActivity.class.getName())));


        Espresso.pressBack();

        onView(withText("Confirm exit ?")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());

        Intents.release();

    }
}

