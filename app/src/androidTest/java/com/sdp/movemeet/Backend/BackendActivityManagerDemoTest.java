/* package com.sdp.movemeet.Backend;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

public class BackendActivityManagerDemoTest {

    public static final String KEYBOARD_INPUT = "ENDTOENDDEBUGTEST";

    @Rule
    public ActivityScenarioRule<BackendActivityManagerDemo> testRule = new ActivityScenarioRule<>(BackendActivityManagerDemo.class);

    @Test
    public void endToEnd() {

        onView(ViewMatchers.withId(R.id.editHostUpload))
                .perform(typeText(KEYBOARD_INPUT), closeSoftKeyboard());
        onView(withId(R.id.uploadButton)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(ViewMatchers.withId(R.id.searchResult)).check(matches(withText(containsString("Success"))));

        onView(ViewMatchers.withId(R.id.editHostSearch))
                .perform(typeText(KEYBOARD_INPUT), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(ViewMatchers.withId(R.id.searchResult)).check(matches(withText(containsString(KEYBOARD_INPUT))));

        onView(ViewMatchers.withId(R.id.editHostDelete))
                .perform(typeText(KEYBOARD_INPUT), closeSoftKeyboard());
        onView(withId(R.id.deleteButton)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(ViewMatchers.withId(R.id.searchResult)).check(matches(withText(containsString("Success"))));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(withId(R.id.searchButton)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(ViewMatchers.withId(R.id.searchResult)).check(matches(withText(containsString("No result"))));
    }
} */
