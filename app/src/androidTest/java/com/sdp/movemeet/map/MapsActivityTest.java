package com.sdp.movemeet.map;

import android.Manifest;
import android.location.Location;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.R;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MapsActivityTest {

    private static final double FAKE_GPS_LATITUDE = 123.76;
    private static final double FAKE_GPS_LONGITUDE = 37.92;
    private static final float FAKE_GPS_ACCURACY = 3.0f;
    public FusedLocationProviderClient mLocationClient;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<MapsActivity> testRule = new ActivityScenarioRule<>(MapsActivity.class);

    @Test
    public void mapsActivity_testLocation(){
        Location newLocation = new Location("flp");
        newLocation.setLatitude(FAKE_GPS_LATITUDE);
        newLocation.setLongitude(FAKE_GPS_LONGITUDE);
        newLocation.setAccuracy(FAKE_GPS_ACCURACY);
        //when(mLocationClient.getLastLocation(any())).thenReturn(newLocation);
    }


    @Test
    public void mapsActivity_isDisplayed() {
        onView(withId(R.id.google_map)).check(matches((isDisplayed())));
    }


    @Test
    public void mapsActivity_checkThatMarkersAreDisplayedAndClickable() throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject userMarker = device.findObject(new UiSelector().descriptionContains("I am here !"));

        UiObject marker1 = device.findObject(new UiSelector().descriptionContains("soccer"));
        UiObject marker2 = device.findObject(new UiSelector().descriptionContains("tennis"));

        assertNotNull(userMarker);
        assertNotNull(marker1);
        assertNotNull(marker2);

        //assertTrue(marker1.isFocusable());
        //assertTrue(marker2.isFocusable());
        //assertTrue(userMarker.isFocusable());

        //marker1.click();
        //marker2.click();
        //userMarker.click();
    }
}
