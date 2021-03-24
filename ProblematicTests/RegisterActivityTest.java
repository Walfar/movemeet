package com.sdp.movemeet;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class RegisterActivityTest {

    public static final String mEmail = "email";
    public static final String mPassword = "password";
    public static final String shortPassword = "pass";

    @Rule
    public ActivityScenarioRule<RegisterActivity> testRule = new ActivityScenarioRule<>(RegisterActivity.class);

// Commented 3rd
//    @Test
//    public void Login_NonEmpty() {
//        onView(withId(R.id.edit_text_email))
//                .perform(replaceText(mEmail), closeSoftKeyboard());
//        onView(withId(R.id.edit_text_password))
//                .perform(replaceText(mPassword), closeSoftKeyboard());
//        onView(withId(R.id.button_register)).perform(click());
//
//    }

    @Test
    public void Login_EmptyMail() {

        onView(withId(R.id.button_register)).perform(click());
    }

// Commented 4th
//    @Test
//    public void Login_EmptyPassword() {
//        onView(withId(R.id.edit_text_email))
//                .perform(replaceText(mEmail), closeSoftKeyboard());
//        onView(withId(R.id.button_register)).perform(click());
//    }

// Commented 2nd
//    @Test
//    public void Login_Password() {
//        onView(withId(R.id.edit_text_email))
//                .perform(replaceText(mEmail), closeSoftKeyboard());
//        onView(withId(R.id.edit_text_password))
//                .perform(replaceText(shortPassword), closeSoftKeyboard());
//        onView(withId(R.id.button_register)).perform(click());
//    }

// Commented first
//    @Test
//    public void Register() {
//        //Intents.init();
//        onView(withId(R.id.text_view_login_here)).perform(click());
//        //Intents.release();
//    }

}