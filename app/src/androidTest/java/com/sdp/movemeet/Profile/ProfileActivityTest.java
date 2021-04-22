package com.sdp.movemeet.Profile;

import android.util.Log;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.ProfileActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.RegisterActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public static final String TEST_DESCRIPTION = "My yolo description";

    @Rule
    public ActivityScenarioRule<RegisterActivity> testRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Before
    public void createAccount(){
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

        //editProfileActivity_getsCorrectData();

        // Trying to directly launch ProfileActivity (even if the "rule" is set to RegisterActivity)
        try (ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(ProfileActivity.class)) {
            clickDeleteAccountButton();
        }
        catch (Exception e) {
            Log.d("TAG", "deleteAccount Exception: " + e);
            e.printStackTrace();
        }
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


    /*@Test
    public void profileActivityToEditProfileActivity() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.edit_text_email),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("antho2@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.edit_text_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("234567"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.edit_text_password), withText("234567"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.button_login), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton2.perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            assert (false);
        }

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav_edit_profile)).perform(click());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.button_update_profile)).perform(click());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.button_edit_profile_save_profile_data)).perform(click());

        logout();
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
    } */


}
