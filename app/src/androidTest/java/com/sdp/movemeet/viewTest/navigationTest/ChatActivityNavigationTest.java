/*package com.sdp.movemeet.viewTest.navigationTest;

import android.view.Gravity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.home.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class ChatActivityNavigationTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> testRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void signIn(){
        onView(withId(R.id.edit_text_email)).perform(replaceText("antho2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password)).perform(replaceText("234567"), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            assert (false);
        }
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        try{
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_chat));

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        try{
            Thread.sleep(2500);
        }catch(Exception e){}
    }

    @Test
    public void ChatActivityToProfileActivity() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_profile));
        onView(withId(R.id.button_update_profile)).check(matches(isDisplayed()));
        logout();
    }

    @Test
    public void ChatActivityToActivityUpload() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_add_activity));
        onView(withId(R.id.buttonConfirmUpload)).check(matches(isDisplayed()));
        logout();
    }

    @Test
    public void ChatActivityToStartActivity() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_start_activity));
        onView(withId(R.id.activityChatDescription)).check(matches(isDisplayed()));
        logout();
    }

    @Test
    public void ChatActivityGotoHome() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.textView), withText("MOVEMEƎT")));
        logout();
    }

    @Test
    public void ChatActivityGotoChat() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_chat));
        onView(withId(R.id.addMessageImageView)).check(matches(isDisplayed()));
        logout();
    }

    @Test
    public void ChatActivity_logout() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }

    public void logout() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
    }
} */
