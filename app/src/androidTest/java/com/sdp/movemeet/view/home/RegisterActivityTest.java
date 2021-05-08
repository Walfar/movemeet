package com.sdp.movemeet.view.home;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    public static final String mEmail = "email";
    public static final String mPassword = "password";
    public static final String shortPassword = "pass";

    public static final String Email = "movemeet@gmail.com";
    public static final String Password = "password";

    @Rule
    public ActivityScenarioRule<RegisterActivity> RegisterTestRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void Empty_Register(){
        try{
            Thread.sleep(5000);
        } catch (Exception e){}

        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void goToLogin(){
        onView(withId(R.id.text_view_login_here)).perform(click());
    }

    @Test
    public void Empty_Password(){
        onView(withId(R.id.edit_text_email))
                .perform(typeText(Email), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Short_Password(){
        onView(withId(R.id.edit_text_email))
                .perform(typeText(Email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(shortPassword), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Right_AlreadyRegister(){
        onView(withId(R.id.edit_text_email))
                .perform(typeText(Email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(mPassword), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void Right_NewRegister(){
        onView(withId(R.id.edit_text_email))
                .perform(typeText(Email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(mPassword), closeSoftKeyboard());
        onView(withId(R.id.button_register)).perform(click());
    }

    @Test
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}