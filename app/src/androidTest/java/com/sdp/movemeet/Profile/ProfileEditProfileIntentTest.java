package com.sdp.movemeet.Profile;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.EditProfileActivity;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.ProfileActivity;
import com.sdp.movemeet.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class ProfileEditProfileIntentTest {

    public static final String TEST_FULL_NAME = "Your Name";
    public static final String TEST_EMAIL = "Your Email Address";
    public static final String TEST_PHONE = "Phone Number";
    public static final String TEST_DESCRIPTION = "Description";

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> testRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

//    @Before
//    public void signIn(){
//        onView(ViewMatchers.withId(R.id.signInButton)).perform(click());
//        onView(withId(R.id.edit_text_email)).perform(replaceText("antho2@gmail.com"), closeSoftKeyboard());
//        onView(withId(R.id.edit_text_password)).perform(replaceText("234567"), closeSoftKeyboard());
//        onView(withId(R.id.button_login)).perform(click());
//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            assert (false);
//        }
//        // Open Drawer to click on navigation.
//        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
//        try{
//            Thread.sleep(500);
//        }catch(Exception e){}
//
//        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_profile));
//
//        try{
//            Thread.sleep(500);
//        }catch(Exception e){}
//
//    }


    @Test
    public void editProfileActivity_getsCorrectData() {
//        Context context = ApplicationProvider.getApplicationContext();
//        Intent intent = new Intent(context, EditProfileActivity.class);
//        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_FULL_NAME, TEST_FULL_NAME);
//        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_EMAIL, TEST_EMAIL);
//        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_PHONE, TEST_PHONE);
//        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_DESCRIPTION, TEST_DESCRIPTION);
//
//        try (ActivityScenario<EditProfileActivity> scenario = ActivityScenario.launch(intent)) {
//            onView(withId(R.id.edit_text_edit_profile_full_name)).check(matches(withText(TEST_FULL_NAME)));
//            onView(withId(R.id.edit_text_edit_profile_email)).check(matches(withText(TEST_EMAIL)));
//            onView(withId(R.id.edit_text_edit_profile_phone)).check(matches(withText(TEST_PHONE)));
//            onView(withId(R.id.edit_text_edit_profile_description)).check(matches(withText(TEST_DESCRIPTION)));
//        }
//
//        logout();
//    }
//
//    public void logout() {
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            assert (false);
//        }
//
//        // Open Drawer to click on navigation.
//        onView(withId(R.id.drawer_layout))
//                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
//                .perform(DrawerActions.open()); // Open Drawer
//
//        try{
//            Thread.sleep(500);
//        }catch(Exception e){}
//
//        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
    }

}