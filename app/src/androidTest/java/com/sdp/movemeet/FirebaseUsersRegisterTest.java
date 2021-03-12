package com.sdp.movemeet;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class FirebaseUsersRegisterTest {

    public static final String mEmail = "email";
    public static final String mPassword = "password";
    public static final String shortPassword = "pass";

    @Rule
    public ActivityScenarioRule<FirebaseUsersRegister> testRule = new ActivityScenarioRule<>(FirebaseUsersRegister.class);

    @Test
    public void Login_NonEmpty() {
        onView(withId(R.id.edit_text_email_register))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_register))
                .perform(typeText(mPassword), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());

    }

    @Test
    public void Login_EmptyMail() {

        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Login_EmptyPassword() {
        onView(withId(R.id.edit_text_email_register))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Login_Password() {
        onView(withId(R.id.edit_text_email_register))
                .perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password_register))
                .perform(typeText(shortPassword), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Register() {
        Intents.init();

        onView(withId(R.id.text_view_login_here)).perform(click());

        Intents.release();
    }
}
