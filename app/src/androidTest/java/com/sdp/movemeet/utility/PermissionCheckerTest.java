package com.sdp.movemeet.utility;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.sdp.movemeet.view.home.HomeScreenActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PermissionCheckerTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> activityRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Rule
    public GrantPermissionRule readPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule writePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule locationRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void locationPermissionsShouldBeGranted() {
        activityRule.getScenario().onActivity(activity -> {
           assertThat(PermissionChecker.isLocationPermissionGranted(activity), is(true));
        });
    }

    @Test
    public void readPermissionsShouldBeGranted() {
        activityRule.getScenario().onActivity(activity -> {
            assertThat(PermissionChecker.isStorageReadPermissionGranted(activity), is(true));
        });
    }

    @Test
    public void writePermissionsShouldBeGranted() {
        activityRule.getScenario().onActivity(activity -> {
            assertThat(PermissionChecker.isStorageWritePermissionGranted(activity), is(true));
        });
    }

}
