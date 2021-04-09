package com.sdp.movemeet.Profile;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityToUserProfileActivity {

    @Rule
    public ActivityTestRule<HomeScreenActivity> mActivityTestRule = new ActivityTestRule<>(HomeScreenActivity.class);

    @Test
    public void homeScreenActivityToUserProfileActivity() {
        /*onView(withId(R.id.signInButton)).perform(click());
        onView(withId(R.id.edit_text_email)).perform(replaceText("antho2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password)).perform(replaceText("234567"), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
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

        // Start the screen of your activity.
        onView(withId(R.id.nav_edit_profile)).perform(click());

        try{
            Thread.sleep(2000);
        }catch(Exception e){}*/

        /*ViewInteraction textView = onView(
                allOf(withId(R.id.text_view_activity_profile_name), withText("Anthony Guinchard"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Anthony Guinchard")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.text_view_activity_profile_email), withText("antho2@gmail.com"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView2.check(matches(withText("antho2@gmail.com")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.text_view_activity_profile_phone), withText("00412356788"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("00412356788")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.text_view_activity_profile_description), withText("Hi there! I love Judo!"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView4.check(matches(withText("Hi there! I love Judo!")));
        logout(); */

    }

    public void logout() {

        try {
            Thread.sleep(5000);
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

        onView(withId(R.id.nav_logout)).perform(forceClick());
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

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
