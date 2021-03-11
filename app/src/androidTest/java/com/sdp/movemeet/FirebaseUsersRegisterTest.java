package com.sdp.movemeet;

import androidx.test.espresso.intent.Intents;
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


@RunWith(AndroidJUnit4.class)
public class FirebaseUsersRegisterTest {

    public static final String KEYBOARD_INPUT_EMAIL = "email";
    public static final String KEYBOARD_INPUT_PASSWORD = "password";
    public static final String KEYBOARD_INPUT_SHORT_PASSWORD = "pass";

    @Rule
    public ActivityScenarioRule<FirebaseUsersRegister> mActivityRule = new ActivityScenarioRule<>(FirebaseUsersRegister.class);

    @Test
    public void Login_NonEmpty() {
        onView(ViewMatchers.withId(R.id.edit_text_email_register)).perform(typeText(KEYBOARD_INPUT_EMAIL), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.edit_text_password_register)).perform(typeText(KEYBOARD_INPUT_PASSWORD), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.button_register)).perform(click());

    }

    @Test
    public void Login_EmptyMail() {
        onView(ViewMatchers.withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Login_EmptyPassword() {
        onView(ViewMatchers.withId(R.id.edit_text_email_register)).perform(typeText(KEYBOARD_INPUT_EMAIL), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Login_Password() {
        onView(ViewMatchers.withId(R.id.edit_text_email_register)).perform(typeText(KEYBOARD_INPUT_EMAIL), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.edit_text_password_register)).perform(typeText(KEYBOARD_INPUT_SHORT_PASSWORD), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Register() {
        Intents.init();

        onView(ViewMatchers.withId(R.id.text_view_login_here)).perform(click());

        Intents.release();
    }
}