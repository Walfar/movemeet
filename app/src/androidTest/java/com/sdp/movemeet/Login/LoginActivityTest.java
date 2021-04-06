/* package com.sdp.movemeet.Login;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


public class LoginActivityTest {

    public static final String mEmail = "email";
    public static final String mPassword = "password";
    public static final String shortPassword = "pass";

    public static final String Email = "movemeet@gmail.com";
    public static final String Password = "password";
    //mPassword = findViewById(R.id.edit_text_password);
    //progressBar = findViewById(R.id.progressBar);
    //fAuth = FirebaseAuth.getInstance();
    //mLoginBtn = findViewById(R.id.button_login);
    //mCreateBtn = findViewById(R.id.text_view_create_account);
    //mLoginBtn.setOnClickListener(new View.OnClickListener()

    @Rule
    public ActivityScenarioRule<LoginActivity> LoginTestRule = new ActivityScenarioRule<>(LoginActivity.class);

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

    @Test public void Login_TrueNonEmpty(){
        onView(withId(R.id.edit_text_email))
                .perform(typeText(Email), closeSoftKeyboard());
        onView(withId(R.id.edit_text_password))
                .perform(typeText(Password), closeSoftKeyboard());
        onView(withId(R.id.button_login)).perform(click());
        try{
            Thread.sleep(500);
            onView(withId(R.id.button_logout)).perform(click());
        }catch(Exception e){
        }
    }


} */
