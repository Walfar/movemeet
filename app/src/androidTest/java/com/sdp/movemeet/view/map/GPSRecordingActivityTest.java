package com.sdp.movemeet.view.map;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.utility.LocationFetcher;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class GPSRecordingActivityTest {

    ActivityScenario scenario;

    public static final double FAKE_LATITUDE = 30.00;
    public static final double FAKE_LONGITUDE = 5.00;
    public static final float FAKE_ACCURACY = 3.0f;
    public Location fakeLocation;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule CoarseLocationPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    Activity activity;

    /*@Rule
    public ActivityScenarioRule<GPSRecordingActivity> testRule = new ActivityScenarioRule<>(GPSRecordingActivity.class);*/

    private UiDevice device;

    private Task<Void> mockTask;
    private Task<Location> mockLocationTask;

    private LocationFetcher locationFetcher;

    @Before
    public void setup() {

        Navigation.profileField = false;
        ActivityDescriptionActivity.enableNav = false;

        activity = new Activity(
            "12345",
            "1",
            "title",
            2,
                new ArrayList<String>(),
            2.45,
                3.697,
                "description",
            "documentPath",
        new Date(2021, 11, 10, 1, 10),
        10.4,
        Sport.Running,
        "address",
                new Date()
        );

        FirebaseAuth fAuth = mock(FirebaseAuth.class);
        FirebaseUser mockUser = mock(FirebaseUser.class);

        when(fAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn("1");

        AuthenticationInstanceProvider.fAuth = fAuth;

        Intent intent = new Intent(getApplicationContext(), GPSRecordingActivity.class).putExtra(ActivityDescriptionActivity.RECORDING_EXTRA_NAME, activity);

        FirestoreActivityManager mockfStoreActivityManager = mock(FirestoreActivityManager.class);
        GPSRecordingActivity.firestoreActivityManager = mockfStoreActivityManager;
        when(mockfStoreActivityManager.add(any(), any())).thenReturn(mock(Task.class));

        scenario = ActivityScenario.launch(intent);


        device = UiDevice.getInstance(getInstrumentation());

        fakeLocation = new Location(LocationManager.PASSIVE_PROVIDER);
        fakeLocation.setLatitude(FAKE_LATITUDE);
        fakeLocation.setLongitude(FAKE_LONGITUDE);
        fakeLocation.setAccuracy(FAKE_ACCURACY);
        fakeLocation.setTime(Calendar.getInstance().getTimeInMillis());

        mockTask = mock(Task.class);
        mockLocationTask = mock(Task.class);

        device.wait(Until.hasObject(By.desc(GPSRecordingActivity.MAP_READY_DESC)), 60_000);


        scenario.onActivity(activity -> {
            GPSRecordingActivity gpsActivity = (GPSRecordingActivity) activity;
            SupportMapFragment supportMapFragment = (SupportMapFragment) gpsActivity.getSupportFragmentManager().findFragmentById(R.id.gmap_recording);

            locationFetcher = new LocationFetcher(supportMapFragment, gpsActivity.locationCallback);

            // This way of mocking the flpc makes it only fire one location callback, when the
            // location updates are requested.
            locationFetcher.fusedLocationProviderClient = mock(FusedLocationProviderClient.class);
            FusedLocationProviderClient flpc = locationFetcher.fusedLocationProviderClient;

            when(flpc.getLastLocation()).thenReturn(mockLocationTask);
            when(flpc
                    .requestLocationUpdates(any(LocationRequest.class), any(LocationCallback.class), any(Looper.class)))
                    .thenAnswer((Answer<Task<Void>>) invocation -> {
                        LocationCallback listener = (LocationCallback) invocation.getArguments()[1];
                        LocationResult mockRes = LocationResult.create(Arrays.asList(fakeLocation, fakeLocation, fakeLocation));
                        listener.onLocationResult(mockRes);
                        return mockTask;
                    });
        });
    }

    @Test
    public void checkPathGetsRecorded() {
        Intents.init();

        onView(withId(R.id.gmap_recording)).check(matches(isDisplayed()));

        onView(withId(R.id.recordButton)).check(matches(withText("Start")));
        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Stop")));

        scenario.onActivity(activity -> {
            locationFetcher.stopLocationUpdates();
            locationFetcher.startLocationUpdates();
        });
        assert(sleep(2_000));

        scenario.onActivity(activity -> {
            assertFalse("Path was empty", ((GPSRecordingActivity) activity).path.isEmpty());
        });

        scenario.onActivity(activity -> {
            ((GPSRecordingActivity) activity).path = new ArrayList();
        });

        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Start")));


        onView(isRoot()).perform(pressBack());
        intended(hasExtraWithKey(ActivityDescriptionActivity.DESCRIPTION_ACTIVITY_KEY));

        Intents.release();

    }

    @After
    public void teardown() {
        scenario.onActivity(activity -> {
            locationFetcher.stopLocationUpdates();
        });

        GPSRecordingActivity.firestoreActivityManager = new FirestoreActivityManager(BackendInstanceProvider.getFirestoreInstance(),
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                new ActivitySerializer());
        AuthenticationInstanceProvider.fAuth = FirebaseAuth.getInstance();
        Navigation.profileField = true;
        ActivityDescriptionActivity.enableNav = true;
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
