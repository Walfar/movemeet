package com.sdp.movemeet.map;

import android.Manifest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.rule.UiThreadTestRule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.sdp.movemeet.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class GPSRecordingActivityTest {

    ActivityScenario scenario;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<GPSRecordingActivity> testRule = new ActivityScenarioRule<>(GPSRecordingActivity.class);

    @Rule public UiThreadTestRule threadTestRule = new UiThreadTestRule();


    CountDownLatch latch;



    @Before
    public void setup() throws Throwable {
        scenario = ActivityScenario.launch(GPSRecordingActivity.class);

        latch = new CountDownLatch(1);


        final SupportMapFragment[] mapFragment = new SupportMapFragment[1];
        scenario.onActivity(activity -> {
            mapFragment[0] = ((GPSRecordingActivity) activity).supportMapFragment;
        });

        OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
            @Override public void onMapReady(GoogleMap googleMap) {
                latch.countDown();

                scenario.onActivity(activity -> {
                    ((GPSRecordingActivity) activity).onMapReady(googleMap);
                });
            }
        };

        threadTestRule.runOnUiThread(new Runnable() {
            @Override public void run() {
                mapFragment[0].getMapAsync(onMapReadyCallback);
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            assert(false);
        }

    }

    @Test
    public void pathGetsRecorded() {

        onView(withId(R.id.gmap_recording)).check(matches(isDisplayed()));


        scenario.onActivity(activity -> {
            assert(((GPSRecordingActivity) activity).googleMap != null);
        });

        onView(withId(R.id.recordButton)).check(matches(withText("Start")));
        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Stop")));

        assert(sleep(15000));

        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Start")));

        scenario.onActivity(activity -> {
            assert(!((GPSRecordingActivity) activity).path.isEmpty());
        });

        assert(true);

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
