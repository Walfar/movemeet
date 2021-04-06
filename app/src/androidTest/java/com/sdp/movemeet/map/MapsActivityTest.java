package com.sdp.movemeet.map;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {

    @Rule
    public ActivityScenarioRule<MapsActivity> testRule = new ActivityScenarioRule<>(MapsActivity.class);


    @Test
    public void mapsActivity_mapIsLaunched() {
        onView(withId(R.id.activity_maps)).check(matches(isDisplayed()));
    }
}
