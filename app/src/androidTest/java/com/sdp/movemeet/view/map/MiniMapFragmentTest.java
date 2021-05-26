package com.sdp.movemeet.view.map;

import android.Manifest;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.activity.UploadActivityActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MiniMapFragmentTest {
    private FirebaseAuth fAuth;


    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<UploadActivityActivity> testRule = new ActivityScenarioRule<>(UploadActivityActivity.class);


    @Before
    public void setUp() throws InterruptedException {
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signInWithEmailAndPassword("test@test.com", "password");
    }

    @Test
    public void miniMapFragmentIsDisplayed() throws InterruptedException {
        testRule.getScenario().onActivity(activity -> {
            MiniMapFragment mapFragment = (MiniMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
            mapFragment.getActivity().runOnUiThread(() -> mapFragment.onMapReady(mapFragment.googleMap));
        });
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
    }

    @Test
    public void noLocationSetsUserPosition() {
        testRule.getScenario().onActivity(activity -> {
            MiniMapFragment mapFragment = (MiniMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
            assertNull(mapFragment.currentLocation);
            mapFragment.currentLocation = new Location("default user location");
            mapFragment.currentLocation.setLongitude(0);
            mapFragment.currentLocation.setLatitude(0);
            assertNull(activity.getAddressLocation());
            assertEquals(mapFragment.currentLocation.getLongitude(), mapFragment.zoomOnAddressLocation().longitude, 0);
            assertEquals(mapFragment.currentLocation.getLatitude(), mapFragment.zoomOnAddressLocation().latitude, 0);
        });
    }

    @Test
    public void onClickSetsLocation() {
        testRule.getScenario().onActivity(activity -> {
            assertEquals(0, activity.latitude, 0);
            assertEquals(0, activity.longitude, 0);
            MiniMapFragment mapFragment = (MiniMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
            mapFragment.onMapClick(new LatLng(1, 1));
            assertEquals(1, activity.latitude, 0);
            assertEquals(1, activity.longitude, 0);
        });

    }

    public boolean sleep(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

}
