package com.sdp.movemeet.view.map;

import android.Manifest;
import android.location.Location;
import android.util.Log;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivityUnregister;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.sdp.movemeet.models.Sport.Badminton;
import static com.sdp.movemeet.models.Sport.Boxing;
import static com.sdp.movemeet.models.Sport.Climbing;
import static com.sdp.movemeet.models.Sport.Dancing;
import static com.sdp.movemeet.models.Sport.Golf;
import static com.sdp.movemeet.models.Sport.Gym;
import static com.sdp.movemeet.models.Sport.Hockey;
import static com.sdp.movemeet.models.Sport.Parkour;
import static com.sdp.movemeet.models.Sport.Pingpong;
import static com.sdp.movemeet.models.Sport.Rugby;
import static com.sdp.movemeet.models.Sport.Running;
import static com.sdp.movemeet.models.Sport.Soccer;
import static com.sdp.movemeet.models.Sport.Swimming;
import static com.sdp.movemeet.models.Sport.Tennis;
import static com.sdp.movemeet.models.Sport.Trekking;
import static com.sdp.movemeet.models.Sport.Tricking;
import static com.sdp.movemeet.models.Sport.VolleyBall;
import static com.sdp.movemeet.models.Sport.Windsurfing;
import static com.sdp.movemeet.models.Sport.Yoga;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class MainMapFragmentTest {

    private final UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
    private FirebaseAuth fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
    private final String TAG = "Main Map Fragment Test";

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public FragmentTestRule<?, MainMapFragment> fragmentTestRule =
            FragmentTestRule.create(MainMapFragment.class);

    @Before
    public void setUp() {
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signInWithEmailAndPassword("test@test.com", "password");
        Intents.init();
    }

    @Test
    public void mainMapFragmentIsDisplayedAndMapIsReadyWhenNotNull() throws InterruptedException {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        mapFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.onMapReady(mapFragment.googleMap);
            }
        });
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
    }

    @Test
    public void markerOnMapForUser() throws UiObjectNotFoundException, InterruptedException {
        waitFor(5000);
        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("I am here !"));
        assertNotNull(marker);
    }

    @Test
    public void displayingUserMarkerSetsPositionMarker() {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        mapFragment.currentLocation = new Location("current location");
        mapFragment.currentLocation.setLatitude(0);
        mapFragment.currentLocation.setLongitude(0);

        mapFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                assertNull(mapFragment.positionMarker);
                mapFragment.displayUserMarker();
                if (mapFragment.googleMap != null) {
                    assertNotNull(mapFragment.positionMarker);
                    assertEquals(mapFragment.onMarkerClick(mapFragment.positionMarker), true);
                }
            }
        });
    }

    @Test
    public void cameraZoomsOnlyOnFirstCall() {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        mapFragment.currentLocation = new Location("current location");
        mapFragment.currentLocation.setLatitude(0);
        mapFragment.currentLocation.setLongitude(0);
        mapFragment.getActivity().runOnUiThread(() -> {
            mapFragment.first_callback = true;
            mapFragment.displayMarkerOnMapReadyAndZoomInFirstCallback();
            assertEquals(false, mapFragment.first_callback);
        });
    }


    @Test
    public void clickingOnActivityMakesCorrectIntentWhenLogged() {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        Activity act = new Activity("activity id", "organizer id", "my title", 4, new ArrayList<>(), 0, 0,
                "desc", "activities/test", new Date(), 1, Soccer, "Dubai UAE", new Date());
        ActivitiesUpdater.activities.add(act);
        mapFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.displayNearbyMarkers();
                if (mapFragment.googleMap != null) {
                    mapFragment.onMarkerClick(mapFragment.activitiesMarkerMap.get(act));
                }
            }
        });
        assertNotNull(fAuth.getCurrentUser());
        if (mapFragment.googleMap != null)
            intended(hasComponent(ActivityDescriptionActivity.class.getName()));
    }

    @Test
    public void clickingOnActivityMakesCorrectIntentWhenUnlogged() {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        mapFragment.user = null;
        Activity act = new Activity("activity id", "organizer id", "my title", 4, new ArrayList<>(), 0, 0,
                "desc", "activities/test", new Date(), 1, Soccer, "Dubai UAE", new Date());
        ActivitiesUpdater.activities.add(act);
        mapFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.displayNearbyMarkers();
                if (mapFragment.googleMap != null) {
                    mapFragment.onMarkerClick(mapFragment.activitiesMarkerMap.get(act));
                }
            }
        });
        if (mapFragment.googleMap != null)
            intended(hasComponent(ActivityDescriptionActivityUnregister.class.getName()));
    }

    @Test
    public void displayingActivitiesMarkersSetsDefaultLocationWhenNull() {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        assertNull(mapFragment.currentLocation);
        //list of activities should be empty
        mapFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.displayNearbyMarkers();
                assertEquals(mapFragment.currentLocation.getLatitude(), 0, 0);
                assertEquals(mapFragment.currentLocation.getLongitude(), 0, 0);
            }
        });
    }

    @Test
    public void testChooseIcons() throws InterruptedException {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();

        assertEquals(R.drawable.icon_boxing, setSportIcon(Boxing, mapFragment), 0);
        assertEquals(R.drawable.icon_windsurfing, setSportIcon(Windsurfing, mapFragment), 0);
        assertEquals(R.drawable.icon_dancing, setSportIcon(Dancing, mapFragment), 0);
        assertEquals(R.drawable.icon_yoga, setSportIcon(Yoga, mapFragment), 0);
        assertEquals(R.drawable.icon_climbing, setSportIcon(Climbing, mapFragment), 0);
        assertEquals(R.drawable.icon_golf, setSportIcon(Golf, mapFragment), 0);
        assertEquals(R.drawable.icon_gym, setSportIcon(Gym, mapFragment), 0);
        assertEquals(R.drawable.icon_soccer, setSportIcon(Soccer, mapFragment), 0);
        assertEquals(R.drawable.icon_tennis, setSportIcon(Tennis, mapFragment), 0);
        assertEquals(R.drawable.icon_volleyball, setSportIcon(VolleyBall, mapFragment), 0);
        assertEquals(R.drawable.icon_hockey, setSportIcon(Hockey, mapFragment), 0);
        assertEquals(R.drawable.icon_pingpong, setSportIcon(Pingpong, mapFragment), 0);
        assertEquals(R.drawable.icon_trekking, setSportIcon(Trekking, mapFragment), 0);
        assertEquals(R.drawable.icon_rugby, setSportIcon(Rugby, mapFragment), 0);
        assertEquals(R.drawable.icon_badminton, setSportIcon(Badminton, mapFragment), 0);
        assertEquals(R.drawable.icon_running, setSportIcon(Running, mapFragment), 0);
        assertEquals(R.drawable.icon_swim, setSportIcon(Swimming, mapFragment), 0);
        assertEquals(R.drawable.icon_tricking, setSportIcon(Tricking, mapFragment), 0);
        assertEquals(R.drawable.icon_parkour, setSportIcon(Parkour, mapFragment), 0);
    }

    private int setSportIcon(Sport sport, @NotNull MainMapFragment mapFragment) {
        Activity activity = new Activity("activity id", "organizer id", "title", 2, new ArrayList<>(), 0, 0,
                "description", "documentPath", new Date(), 1, sport, "here", new Date());
        return mapFragment.chooseIcon(activity);
    }

    @Test
    public void userClickingOnMapAddsNewActivity() throws UiObjectNotFoundException, InterruptedException {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        //User must be logged to add new activity
        assertNotNull(fAuth.getCurrentUser());
        assertNull(mapFragment.newActivityMarker);

        mapFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapFragment.onMapClick(new LatLng(0, 0));

                if (mapFragment.googleMap != null) {
                    assertNotNull(mapFragment.newActivityMarker);
                    //When clicking on the info window, it removes the marker and creates an intent to the upload activity class
                    mapFragment.onInfoWindowClick(mapFragment.newActivityMarker);
                    assertNull(mapFragment.newActivityMarker);
                }
            }
        });
        if (mapFragment.googleMap != null)
            intended(hasComponent(UploadActivityActivity.class.getName()));
    }

    @After
    public void after() {
        Navigation.profileField = false;
        Intents.release();
    }

    private void waitFor(int duration) {
        try {
            Thread.sleep(duration);
        } catch (Exception e) {
            Log.d(TAG, "sleep failed");
        }
    }
}