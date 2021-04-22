package com.sdp.movemeet.utility;

import android.location.Location;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.sdp.movemeet.map.MainMapFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LocationFetcherTest {

    @Rule
    public FragmentTestRule<?, MainMapFragment> fragmentTestRule =
            FragmentTestRule.create(MainMapFragment.class);

    @Test
    public void fetchingWithoutPermissionsDoesntSetLocation() {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        //If the location fetcher is called without permissions, the current location should always be null
        LocationFetcher.fetchLastLocation(mapFragment.getFusedLocationProviderClient(), mapFragment.getSupportMapFragment(), mapFragment);
        assertNull(LocationFetcher.currentLocation);
    }

    @Test
    public void defaultLocationIsCenter() {
        Location defaultLocation = LocationFetcher.defaultLocation();
        assertEquals(0, defaultLocation.getLongitude(), 0);
        assertEquals(0, defaultLocation.getLatitude(), 0);
    }
}
