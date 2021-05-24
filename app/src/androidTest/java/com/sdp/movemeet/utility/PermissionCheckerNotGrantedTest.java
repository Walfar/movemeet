package com.sdp.movemeet.utility;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.view.home.HomeScreenActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PermissionCheckerNotGrantedTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> activityRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Test
    public void locationPermissionsShouldNotBeGranted() {
        activityRule.getScenario().onActivity(activity -> {
            assertThat(PermissionChecker.isLocationPermissionGranted(activity), is(false));
        });
    }

    @Test
    public void readPermissionsShouldNotBeGranted() {
        activityRule.getScenario().onActivity(activity -> {
            assertThat(PermissionChecker.isStorageReadPermissionGranted(activity), is(false));
        });
    }

    @Test
    public void writePermissionsShouldNotBeGranted() {
        activityRule.getScenario().onActivity(activity -> {
            assertThat(PermissionChecker.isStorageWritePermissionGranted(activity), is(false));
        });
    }

}
