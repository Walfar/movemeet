package com.sdp.movemeet.map;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4ClassRunner.class)
public class GPSRecordingActivityWithoutPermissionsTest {
    @Rule
    public ActivityScenarioRule<GPSRecordingActivity> testRule = new ActivityScenarioRule<>(GPSRecordingActivity.class);

    @Test
    public void cantUpdateLocationWithNoPermissions() {
        testRule.getScenario().onActivity(activity -> {
            ((GPSRecordingActivity) activity).startLocationUpdates();
            assertEquals(activity.updatingLocation, false);
        });

    }
}
