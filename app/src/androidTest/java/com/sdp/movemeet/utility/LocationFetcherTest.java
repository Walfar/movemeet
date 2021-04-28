package com.sdp.movemeet.utility;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import androidx.annotation.VisibleForTesting;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.R;
import com.sdp.movemeet.map.GPSRecordingActivity;
import com.sdp.movemeet.map.MainMapFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Calendar;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.sdp.movemeet.map.GPSRecordingActivityTest.FAKE_ACCURACY;
import static com.sdp.movemeet.map.GPSRecordingActivityTest.FAKE_LATITUDE;
import static com.sdp.movemeet.map.GPSRecordingActivityTest.FAKE_LONGITUDE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LocationFetcherTest {

    private Location fakeLocation;
    private Task<Void> mockTask;
    private Task<Location> mockLocationTask;

    private LocationFetcher locationFetcher;

    @Rule
    public FragmentTestRule<?, MainMapFragment> fragmentTestRule =
            FragmentTestRule.create(MainMapFragment.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setup() {

        fakeLocation = new Location(LocationManager.PASSIVE_PROVIDER);
        fakeLocation.setLatitude(FAKE_LATITUDE);
        fakeLocation.setLongitude(FAKE_LONGITUDE);
        fakeLocation.setAccuracy(FAKE_ACCURACY);
        fakeLocation.setTime(Calendar.getInstance().getTimeInMillis());

        mockTask = mock(Task.class);
        mockLocationTask = mock(Task.class);

        FusedLocationProviderClient fusedLocationProviderClient = mock(FusedLocationProviderClient.class);
        locationFetcher = new LocationFetcher(fragmentTestRule.getFragment().getSupportMapFragment(), fragmentTestRule.getFragment());
        locationFetcher.fusedLocationProviderClient = fusedLocationProviderClient;

        when(fusedLocationProviderClient.getLastLocation()).thenReturn(mockLocationTask);
        when(fusedLocationProviderClient
                .requestLocationUpdates(any(LocationRequest.class), any(LocationCallback.class), any(Looper.class)))
                .thenAnswer((Answer<Task<Void>>) invocation -> {
                    LocationCallback listener = (LocationCallback) invocation.getArguments()[1];
                    LocationResult mockRes = LocationResult.create(Arrays.asList(fakeLocation, fakeLocation, fakeLocation));
                    listener.onLocationResult(mockRes);
                    return mockTask;
                });
    }

    @Test
    public void fetcherCorrectlyUpdatesLocation() throws InterruptedException {
        locationFetcher.startLocationUpdates();
        Thread.sleep(1000);
        locationFetcher.stopLocationUpdates();
        assertEquals(mockLocationTask.getResult(), locationFetcher.getCurrentLocation());
    }

    @Test
    public void currentLocationReturnsDefaultWhenNull() {
        locationFetcher.currentLocation = null;
        assertEquals(locationFetcher.getDefaultLocation(), locationFetcher.getCurrentLocation());
    }
}
