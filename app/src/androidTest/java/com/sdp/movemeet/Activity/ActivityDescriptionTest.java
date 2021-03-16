package com.sdp.movemeet.Activity;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.movemeet.R;
import com.sdp.movemeet.RegisterActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ActivityDescriptionTest {

    @Rule
    public ActivityScenarioRule<ActivityDescriptionActivity> testRule = new ActivityScenarioRule<>(ActivityDescriptionActivity.class);

    @Test
    public void create() {
        Intents.init();

        Intents.release();
    }
}
