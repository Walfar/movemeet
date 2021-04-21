package com.sdp.movemeet.Navigation;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> testRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Before
    public void signIn(){
        onView(ViewMatchers.withId(R.id.signInButton)).perform(click());
        onView(withId(R.id.edit_text_email)).perform(replaceText("antho2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password)).perform(replaceText("234567"), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
    }

    @Test
    public void mainActivityToProfileActivity() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_profile));
        logout();
    }

    @Test
    public void mainActivityToActivityUpload() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_add_activity));
        logout();
    }

    @Test
    public void mainActivityToStartActivity() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_start_activity));
        logout();
    }

    @Test
    public void mainActivityGotoHome() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        logout();
    }

    @Test
    public void mainActivityGotoChat() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_chat));
        logout();
    }

    @Test
    public void mainActivity_logout() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
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
}