package com.sdp.movemeet;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FirebaseUsersGreetingActivityTest {

    public static final String RECEIVED_INPUT = "TEST_INPUT_09820183";

    @Rule
    public ActivityScenarioRule<FirebaseUsersMainActivity> testRule = new ActivityScenarioRule<>(FirebaseUsersMainActivity.class);

    @Test
    public void greetingActivity_getsCorrectData() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, FirebaseUsersGreetingActivity.class);
        intent.putExtra(FirebaseUsersMainActivity.EXTRA_MESSAGE, RECEIVED_INPUT);


        try (ActivityScenario<FirebaseUsersGreetingActivity> scenario = ActivityScenario.launch(intent)) {
            // Assert stuff on the activity
            String expected = context.getResources().getString(R.string.greeting_message, RECEIVED_INPUT);
            onView(withId(R.id.greetingMessage)).check(matches(withText(expected)));
        }
    }


    @Test
    public void greetingAction_endToEnd() {
        onView(withId(R.id.mainEditName))
                .perform(replaceText(RECEIVED_INPUT), closeSoftKeyboard());
        onView(withId(R.id.mainGoButton)).perform(click());

        // Necessary to retrieve context to be able to retrieve resource strings
        Context targetContext = ApplicationProvider.getApplicationContext();
        String expected = targetContext.getResources().getString(R.string.greeting_message, RECEIVED_INPUT);

        onView(withId(R.id.greetingMessage)).check(matches(withText(expected)));
    }

}
