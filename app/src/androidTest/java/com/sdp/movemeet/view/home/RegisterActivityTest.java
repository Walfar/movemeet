package com.sdp.movemeet.view.home;

import android.view.View;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    public static final String password = "password";
    public static final String shortPassword = "pass";

    public static final String email = "movemeet@gmail.com";

    @Rule
    //public ActivityScenarioRule<RegisterActivity> RegisterTestRule = new ActivityScenarioRule<>(RegisterActivity.class);
    public ActivityTestRule<RegisterActivity> RegisterTestRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void Empty_Register() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        onView(withId(R.id.button_register)).perform(click());

        onView(withId(R.id.edit_text_email)).check(matches(withError(RegisterTestRule.getActivity().getString(R.string.register_email_required_message))));
    }

    @Test
    public void goToLogin() {
        onView(withId(R.id.text_view_login_here)).perform(click());
        try {
            Thread.sleep(400);
        } catch (Exception e) {
        }
        // Ensuring that we precisely land on LoginActivity by checking the presence of the Login
        // button
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }

    @Test
    public void Empty_Password() {
        onView(withId(R.id.edit_text_email))
                .perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());

        onView(withId(R.id.edit_text_password)).check(matches(withError(RegisterTestRule.getActivity().getString(R.string.register_password_required_message))));
    }

    @Test
    public void Short_Password() {
        onView(withId(R.id.edit_text_email))
                .perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(replaceText(shortPassword), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());

        onView(withId(R.id.edit_text_password)).check(matches(withError(RegisterTestRule.getActivity().getString(R.string.register_short_password_message))));
    }

    @Test
    public void Right_AlreadyRegister() {
        onView(withId(R.id.edit_text_email))
                .perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(replaceText(password), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Right_NewRegister() {
        onView(withId(R.id.edit_text_email))
                .perform(replaceText(email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(replaceText(password), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }


    @Test
    public void AccountAlreadyExists() {
        onView(withId(R.id.edit_text_full_name))
                .perform(replaceText("Antho"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_email))
                .perform(replaceText("antho2@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(replaceText("234567"), closeSoftKeyboard());
        onView(withId(R.id.edit_text_phone))
                .perform(replaceText("0244341964"), closeSoftKeyboard());

        onView(withId(R.id.button_register)).perform(click());


        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        onView(withId(R.id.button_register)).check(matches(isDisplayed()));

    }

    private static Matcher<View> withError(final String expected) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return editText.getError().toString().equals(expected);
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }


    @Test
    public void logOut() {
        FirebaseAuth fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signOut();
    }
}