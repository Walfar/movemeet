package com.sdp.movemeet.map;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class GPSRecordingActivityTest {

    ActivityScenario scenario;

    private static final double FAKE_LATITUDE = 30.00;
    private static final double FAKE_LONGITUDE = 5.00;
    private static final float FAKE_ACCURACY = 3.0f;
    private Location fakeLocation;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule CoarseLocationPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);


    @Rule
    public ActivityScenarioRule<GPSRecordingActivity> testRule = new ActivityScenarioRule<>(GPSRecordingActivity.class);

    private UiDevice device;

    private Task<Void> mockTask;
    private Task<Location> mockLocationTask;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());

        scenario = testRule.getScenario();

        fakeLocation = new Location(LocationManager.PASSIVE_PROVIDER);
        fakeLocation.setLatitude(FAKE_LATITUDE);
        fakeLocation.setLongitude(FAKE_LONGITUDE);
        fakeLocation.setAccuracy(FAKE_ACCURACY);
        fakeLocation.setElapsedRealtimeNanos(Calendar.getInstance().getTimeInMillis());
        fakeLocation.setTime(Calendar.getInstance().getTimeInMillis());

        mockTask = mock(Task.class);
        mockLocationTask = mock(Task.class);

        device.wait(Until.hasObject(By.desc(GPSRecordingActivity.MAP_READY_DESC)), 60_000);

        // Map is now ready, so location updates have started. We can turn them off, switch out
        // the callback, and turn them back on.

        /*scenario.onActivity(activity -> {
            when(mockLocationTask.getResult()).thenReturn(fakeLocation);
        });

        scenario.onActivity(activity -> {*/
            /*((GPSRecordingActivity) activity).stopLocationUpdates();

            ((GPSRecordingActivity) activity).fusedLocationClient = mock(FusedLocationProviderClient.class);
            FusedLocationProviderClient flpc = ((GPSRecordingActivity) activity).fusedLocationClient;

            when(flpc.getLastLocation()).thenReturn(mockLocationTask);*/
            /*when(flpc
                    .requestLocationUpdates(any(LocationRequest.class), any(LocationCallback.class), any(Looper.class)))
                    .thenAnswer((Answer<Task<Void>>) invocation -> {
                        LocationCallback listener = (LocationCallback) invocation.getArguments()[1];
                        LocationResult mockRes = LocationResult.create(Arrays.asList(fakeLocation, fakeLocation, fakeLocation));
                        listener.onLocationResult(mockRes);
                        return mockTask;
                    });
        });*/

        scenario.onActivity(activity -> {
            ((GPSRecordingActivity) activity).stopLocationUpdates();

            ((GPSRecordingActivity) activity).locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    ((GPSRecordingActivity) activity).updatePath(fakeLocation);
                }
            };

            ((GPSRecordingActivity) activity).startLocationUpdates();
        });
    }

    @Test
    public void checkPathGetsRecorded() {

        Boolean success = device.wait(Until.hasObject(By.desc(GPSRecordingActivity.MAP_READY_DESC)), 60_000);
        assertNotNull("Map was not readied in time", success);

        onView(withId(R.id.gmap_recording)).check(matches(isDisplayed()));

        onView(withId(R.id.recordButton)).check(matches(withText("Start")));
        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Stop")));

        assert(sleep(15000));

        scenario.onActivity(activity -> {
            assertFalse("Path was empty", ((GPSRecordingActivity) activity).path.isEmpty());
        });

        scenario.onActivity(activity -> {
            ((GPSRecordingActivity) activity).path = new ArrayList();
        });

        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Start")));

        assert(sleep(10000));

        scenario.onActivity(activity -> {
            assertEquals("Activity kept recording but should have stopped", true, ((GPSRecordingActivity) activity).path.isEmpty());
        });

    }

    @After
    public void teardown() {
        scenario.onActivity(activity -> {
            ((GPSRecordingActivity) activity).fusedLocationClient.setMockMode(false);
        });
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
