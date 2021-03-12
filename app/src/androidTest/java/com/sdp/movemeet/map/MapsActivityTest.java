package com.sdp.movemeet.map;

import android.location.Location;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {

    @Rule
    public ActivityScenarioRule<MapsActivity> testRule = new ActivityScenarioRule<>(MapsActivity.class);

    @Test
    public void mapsActivity_onRequestPermissionResult() {
        Location location = new Location("flp");
        location.setLatitude(37.7);
        location.setLongitude(-122.08);
        location.setAccuracy(3.0f);
    }
}
