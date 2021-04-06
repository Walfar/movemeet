package com.sdp.movemeet.Register;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.RegisterActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.sql.DriverManager.println;

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
}