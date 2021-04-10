package com.sdp.movemeet.map;

import android.Manifest;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.UploadActivityActivity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNull;

import org.junit.Before;
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
    public void mainMapFragment_MarkerOnMapForUser() throws UiObjectNotFoundException {
        UiObject marker = uiDevice.findObject(new UiSelector().descriptionContains("I am here !"));
        assertNotNull(marker);
    }


    @Test
    public void mainMapFragment_isDisplayed() throws InterruptedException {
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
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




    private void waitFor(int duration) throws InterruptedException {
        Thread.sleep(duration);
    }
}