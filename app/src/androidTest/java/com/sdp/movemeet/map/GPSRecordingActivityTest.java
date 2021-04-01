package com.sdp.movemeet.map;

import android.Manifest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class GPSRecordingActivityTest {

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<GPSRecordingActivity> testRule = new ActivityScenarioRule<>(GPSRecordingActivity.class);

    @Test
    public void mapGetsReady() {

        GrantPermissionRule locationPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

        ActivityScenario scenario = ActivityScenario.launch(GPSRecordingActivity.class);

        assert(sleep(2000));

        onView(withId(R.id.gmap_recording)).check(matches(isDisplayed()));

        assert(sleep(2000));

        /*scenario.onActivity(activity -> {
            assert(((GPSRecordingActivity) activity).getGoogleMap() != null);
        });*/

        /*onView(withId(R.id.recordButton)).check(matches(withText("Start")));
        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Stop")));

        assert(sleep(15000));

        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Start")));

        scenario.onActivity(activity -> {
            assert(!((GPSRecordingActivity) activity).getPath().isEmpty());
        });*/

    }

    public boolean sleep(int millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

}
