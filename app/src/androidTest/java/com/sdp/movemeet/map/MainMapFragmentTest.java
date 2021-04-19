package com.sdp.movemeet.map;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import androidx.annotation.NonNull;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.UploadActivityActivity;
import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.utility.LocationFetcher;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.sdp.movemeet.Sport.Badminton;
import static com.sdp.movemeet.Sport.Boxing;
import static com.sdp.movemeet.Sport.Climbing;
import static com.sdp.movemeet.Sport.Dancing;
import static com.sdp.movemeet.Sport.Golf;
import static com.sdp.movemeet.Sport.Gym;
import static com.sdp.movemeet.Sport.Hockey;
import static com.sdp.movemeet.Sport.Pingpong;
import static com.sdp.movemeet.Sport.Rugby;
import static com.sdp.movemeet.Sport.Running;
import static com.sdp.movemeet.Sport.Soccer;
import static com.sdp.movemeet.Sport.Swimming;
import static com.sdp.movemeet.Sport.Tennis;
import static com.sdp.movemeet.Sport.Trekking;
import static com.sdp.movemeet.Sport.VolleyBall;
import static com.sdp.movemeet.Sport.Windsurfing;
import static com.sdp.movemeet.Sport.Yoga;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainMapFragmentTest {

    private UiDevice uiDevice;
    private FirebaseAuth fAuth;
    private FirebaseUser user;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

   @Rule
   public FragmentTestRule<?, MainMapFragment> fragmentTestRule =
           FragmentTestRule.create(MainMapFragment.class);


    @Before
    public void setUp() throws InterruptedException {
        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                user = fAuth.getCurrentUser();
                uiDevice = UiDevice.getInstance(getInstrumentation());
            }
        });
        waitFor(2000);
    }

    @Test
    public void mainMapFragment_isDisplayed() throws InterruptedException {
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
    }

    @Test
    public void mainMapFragment_MarkerOnMapForUser() throws UiObjectNotFoundException {
        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("I am here !"));
        assertNotNull(marker);
    }

    @Test
    public void locationIsCorrectlyFetched() throws InterruptedException {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        LocationFetcher.fetchLastLocation(mapFragment.getFusedLocationProviderClient(), mapFragment.getSupportMapFragment(), mapFragment);
        Thread.sleep(3000);
        Location defaultLocation = LocationFetcher.defaultLocation();
        assertEquals(0, defaultLocation.getLongitude(), 0);
        assertEquals(0, defaultLocation.getLatitude(), 0);
    }



    @Test
    public void activitiesUpdatesOnAdd() {
        MainMapFragment mapFragment = fragmentTestRule.getFragment();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        BackendActivityManager bam = new BackendActivityManager(db, "activities");

        updater.fetchListActivities();
        updater.updateListActivities(mapFragment);

        Activity act = new Activity("activity",
                "me",
                "title",
                10,
                new ArrayList<String>(),
                0,
                0,
                "desc",
                new Date(),
                10,
                Sport.Running,
                "address",
                new Date());

        bam.uploadActivity(act,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updater.updateListActivities(mapFragment);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        List<Activity> activities = updater.getActivities();
                        Activity act_in_collection = activities.get(activities.size()-1);
                        assertEquals(act.getActivityId(), act_in_collection.getActivityId());
                        assertEquals(act.getAddress(), act_in_collection.getAddress());
                        assertEquals(act.getDate(), act_in_collection.getDate());
                        assertEquals(act.getDescription(), act_in_collection.getDescription());
                        assertEquals(act.getLatitude(), act_in_collection.getLatitude());
                        assertEquals(act.getLongitude(), act_in_collection.getLongitude());
                        assertEquals(act.getNumberParticipant(), act_in_collection.getNumberParticipant());
                        assertEquals(act.getParticipantId(), act_in_collection.getParticipantId());
                        assertEquals(act.getTitle(), act_in_collection.getTitle());
                        assertEquals(act.getOrganizerId(), act_in_collection.getOrganizerId());
                        assertEquals(act.getSport(), act_in_collection.getSport());
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }


    @Test
    public void testChooseIcons() {
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
    }

    private int setSportIcon(Sport sport, MainMapFragment mapFragment) {
        Activity activity = new Activity("activity id", "organizer id", "title", 2, new ArrayList<>(), 0, 0,
                "description", new Date(), 1, sport,"here", new Date());
        return mapFragment.chooseIcon(activity);
    }

    @Test
    public void mainMapFragment_userClickingOnMapAddsNewActivity() throws UiObjectNotFoundException, InterruptedException {

        //User must be logged to add new activity
        assertNotNull(user);

        //fragmentTestRule.launchFragment(new MainMapFragment());

        waitFor(2000);

        uiDevice.click(200, 500);

        waitFor(1000);

        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("new activity"));
        assertNotNull(marker);

        uiDevice.click(200, 400);
    }

    @After
    public void after() {
        fAuth.signOut();
    }


    private void waitFor(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }
}