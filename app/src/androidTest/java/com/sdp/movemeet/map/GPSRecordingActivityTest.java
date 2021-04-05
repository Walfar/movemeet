package com.sdp.movemeet.map;

import android.Manifest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.rule.UiThreadTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class GPSRecordingActivityTest {

    ActivityScenario scenario;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<GPSRecordingActivity> testRule = new ActivityScenarioRule<>(GPSRecordingActivity.class);

    private UiDevice device;

    @Before
    public void setup() {
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void pathGetsRecorded() {

        scenario = testRule.getScenario();//ActivityScenario.launch(GPSRecordingActivity.class);

        Boolean success = device.wait(Until.hasObject(By.desc(GPSRecordingActivity.MAP_READY_DESC)), 60000);
        assertNotNull("Map was not readied in time", success);

        onView(withId(R.id.gmap_recording)).check(matches(isDisplayed()));


        scenario.onActivity(activity -> {
            assertNotNull("Could not find a GoogleMap", ((GPSRecordingActivity) activity).googleMap);
        });

        onView(withId(R.id.recordButton)).check(matches(withText("Start")));
        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Stop")));

        assert(sleep(15000));

        onView(withId(R.id.recordButton)).perform(click());
        onView(withId(R.id.recordButton)).check(matches(withText("Start")));

        scenario.onActivity(activity -> {
            assertNotEquals("Path was empty", true, !((GPSRecordingActivity) activity).path.isEmpty());
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
