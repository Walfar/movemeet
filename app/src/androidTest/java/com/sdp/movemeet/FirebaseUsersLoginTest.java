package com.sdp.movemeet;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;


@RunWith(AndroidJUnit4.class)
public class FirebaseUsersLoginTest {

    public static final String KEYBOARD_INPUT_EMAIL = "antho2@gmail.com";
    public static final String KEYBOARD_INPUT_PASSWORD = "234567";

    @Rule
    public ActivityScenarioRule<FirebaseUsersLogin> mActivityTestRule = new ActivityScenarioRule<>(FirebaseUsersLogin.class);

    @Test
    public void firebaseUsersLoginEndToEnd() {

        // Filling the email
        onView(ViewMatchers.withId(R.id.edit_text_email_register)).perform(typeText(KEYBOARD_INPUT_EMAIL), closeSoftKeyboard());

        // Filling the password
        onView(ViewMatchers.withId(R.id.edit_text_password_register)).perform(typeText(KEYBOARD_INPUT_PASSWORD), closeSoftKeyboard());

        // Clicking on the LOGIN button
        onView(withId(R.id.text_view_details_glance)).perform(click());

        // Sleeping a bit while sending request to Firebase
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            assert (false);
        }

        // Checking the correct update of the textViews
        onView(ViewMatchers.withId(R.id.text_view_profile_name)).check(matches(withText(containsString("Anthony Test2"))));
        onView(ViewMatchers.withId(R.id.text_view_profile_email)).check(matches(withText(containsString("antho2@gmail.com"))));
        onView(ViewMatchers.withId(R.id.text_view_profile_phone)).check(matches(withText(containsString("+41798841817"))));

    }

}
