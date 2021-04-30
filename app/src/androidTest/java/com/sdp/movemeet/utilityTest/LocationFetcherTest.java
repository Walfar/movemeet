package com.sdp.movemeet.utilityTest;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.utility.LocationFetcher;
import com.sdp.movemeet.view.map.MainMapFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Calendar;

import static com.sdp.movemeet.viewTest.mapTest.GPSRecordingActivityTest.FAKE_ACCURACY;
import static com.sdp.movemeet.viewTest.mapTest.GPSRecordingActivityTest.FAKE_LATITUDE;
import static com.sdp.movemeet.viewTest.mapTest.GPSRecordingActivityTest.FAKE_LONGITUDE;
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

    private Location currentLocation;

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


        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                //Whenever we get an update from the fusedLocationProviderClient, we update the current location
                currentLocation = locationResult.getLastLocation();
            }
        };
        locationFetcher = new LocationFetcher(fragmentTestRule.getFragment().getSupportMapFragment(), locationCallback);

        when(fusedLocationProviderClient.getLastLocation()).thenReturn(mockLocationTask);
        when(fusedLocationProviderClient
                .requestLocationUpdates(any(LocationRequest.class), any(LocationCallback.class), any(Looper.class)))
                .thenAnswer((Answer<Task<Void>>) invocation -> {
                    LocationCallback listener = (LocationCallback) invocation.getArguments()[1];
                    LocationResult mockRes = LocationResult.create(Arrays.asList(fakeLocation, fakeLocation, fakeLocation));
                    listener.onLocationResult(mockRes);
                    return mockTask;
                });

        locationFetcher.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    @Test
    public void fetcherCorrectlyUpdatesLocation() throws InterruptedException {
        locationFetcher.startLocationUpdates();
        Thread.sleep(2000);
        locationFetcher.stopLocationUpdates();
        assertEquals(fakeLocation, currentLocation);
    }


    @Test
    public void defaultLocationIsCorrect() {
        Location defaultLocation = new Location("default location");
        defaultLocation.setLongitude(0);
        defaultLocation.setLatitude(0);
        assertEquals(locationFetcher.getDefaultLocation().getLatitude(), defaultLocation.getLatitude(), 0);
        assertEquals(locationFetcher.getDefaultLocation().getLongitude(), defaultLocation.getLongitude(), 0);
    }
}
