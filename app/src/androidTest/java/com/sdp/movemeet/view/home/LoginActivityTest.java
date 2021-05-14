package com.sdp.movemeet.view.home;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    public static final String mEmail = "email";
    public static final String mPassword = "password";
    public static final String shortPassword = "pass";

    public static final String Email = "movemeet@gmail.com";
    public static final String Password = "password";

    @Rule
    public ActivityScenarioRule<LoginActivity> LoginTestRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void unableNav() {
        MainActivity.enableNav = false;
    }


    @Test public void Login_Empty(){
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_FalseNonEmpty() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(mPassword), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_EmptyMail() {
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_EmptyPassword() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_ShortPassword() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(shortPassword), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Register() {
        onView(withId(R.id.text_view_create_account)).perform(click());
    }

    @Test
    public void Login_TrueNonEmpty(){
        onView(withId(R.id.edit_text_email))
                .perform(typeText(Email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(Password), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @After
    public void signOut() {
        MainActivity.enableNav = true;
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            fAuth.signOut();
        }
    }
}