package com.sdp.movemeet.Security;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class ActivityDescriptionSecurityTest {

    @Rule
    public ActivityScenarioRule<ActivityDescriptionActivity> testRule = new ActivityScenarioRule<>(ActivityDescriptionActivity.class);

    @Test
    public void redirectionTest() {
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }
}