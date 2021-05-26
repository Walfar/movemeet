package com.sdp.movemeet.view.home;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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


public class LoginActivityTest {

    public static final String falseEmail = "email";
    public static final String falsePassword = "password";
    public static final String shortPassword = "pass";

    public static final String trueEmail = "movemeet@gmail.com";
    public static final String truePassword = "password";

    @Rule
    public ActivityScenarioRule<LoginActivity> LoginTestRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void signOutBeforeTest() {
        FirebaseAuth fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        if (fAuth.getCurrentUser() != null) {
            fAuth.signOut();
        }
    }

    @Test public void Login_Empty(){
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_FalseNonEmpty() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(falseEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(falsePassword), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_EmptyMail() {
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_EmptyPassword() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(falseEmail), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @Test public void Login_ShortPassword() {
        onView(withId(R.id.edit_text_email))
                .perform(typeText(falseEmail), closeSoftKeyboard());
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
                .perform(typeText(trueEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(truePassword), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
    }

    @After
    public void signOut() {
        FirebaseAuth fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        if (fAuth.getCurrentUser() != null) {
            fAuth.signOut();
        }
    }
}