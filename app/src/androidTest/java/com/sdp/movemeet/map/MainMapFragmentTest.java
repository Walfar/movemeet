package com.sdp.movemeet.map;

import android.Manifest;
import android.graphics.Point;
import android.location.Location;
import android.view.Display;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.sdp.movemeet.R;
import com.sdp.movemeet.UploadActivityActivity;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainMapFragmentTest {

    private static final double FAKE_GPS_LATITUDE = 123.76;
    private static final double FAKE_GPS_LONGITUDE = 37.92;
    private static final float FAKE_GPS_ACCURACY = 3.0f;
    private UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

   @Rule
   public FragmentTestRule<?, MainMapFragment> fragmentTestRule =
           FragmentTestRule.create(MainMapFragment.class);


    @Test
    public void mainMapFragment_testLocation(){
        Location newLocation = new Location("flp");
        newLocation.setLatitude(FAKE_GPS_LATITUDE);
        newLocation.setLongitude(FAKE_GPS_LONGITUDE);
        newLocation.setAccuracy(FAKE_GPS_ACCURACY);
    }

    @Test
    public void mainMapFragment_userHasMarkerOnMap() {
        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("I am here !"));
        assertNotNull(marker);
    }

    @Test
    public void mainMapFragment_clickOnMapAddsNewActivity() throws UiObjectNotFoundException, InterruptedException {
        int twoSeconds = 2000;

        uiDevice.click(10, 10);

        waitFor(twoSeconds);

        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("new activity"));
        assertNotNull(marker);
        marker.click();


        waitFor(twoSeconds);

        // Calculate the (x,y) position of the InfoWindow, using the screen size
        Display display = fragmentTestRule.getActivity().getDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int x = screenWidth / 2;
        int y = (int) (screenHeight * 0.43);

        // Click on the InfoWindow, using UIAutomator
        uiDevice.click(x, y);
        waitFor(twoSeconds);

        UiObject marker2 = uiDevice.findObject(new UiSelector().descriptionContains("new activity"));
        assertNull(marker2);
        intended(hasComponent(UploadActivityActivity.class.getName()));
    }


    @Test
    public void mainMapFragment_isDisplayed() throws InterruptedException {
        waitFor(5000);
        onView(withId(R.id.google_map)).check(matches((isDisplayed())));
    }


    private void waitFor(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }
}

