package com.sdp.movemeet.Navigation;

import android.view.Gravity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UploadActivityNavigationTest {
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
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_add_activity));

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        try{
            Thread.sleep(500);
        }catch(Exception e){}
    }

    @Test
    public void UploadActivityToProfileActivity() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_edit_profile));
        logout();
    }

    @Test
    public void UploadActivityToActivityUpload() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_add_activity));
        logout();
    }

    @Test
    public void UploadActivityToStartActivity() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_start_activity));
        logout();
    }

    @Test
    public void UploadActivityGotoHome() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        logout();
    }

    @Test
    public void UploadActivityGotoChat() {
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_chat));
        logout();
    }

    @Test
    public void UploadActivity_logout() {
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
