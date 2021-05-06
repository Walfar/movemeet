package com.sdp.movemeet.viewTest.mapTest;

import android.Manifest;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import static androidx.test.espresso.action.ViewActions.click;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.map.MiniMapFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class MiniMapFragmentTest {

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public ActivityScenarioRule<UploadActivityActivity> testRule = new ActivityScenarioRule<>(UploadActivityActivity.class);

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Before
    public void setUp() throws InterruptedException {
        //The map is tested when the user is logged in
        firebaseAuth.signInWithEmailAndPassword("test@test.com", "password");
        Thread.sleep(2000);
    }

    @Test
    public void miniMapFragment_isDisplayed() throws InterruptedException {
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
    }

    @Test
    public void noAddressSetIfNoClick() {
        testRule.getScenario().onActivity(activity -> {
            LatLng address = ((UploadActivityActivity) activity).getAddressLocation();
            assertNull(address);
        });
    }

    @Test
    public void onClickSetsLocation() throws InterruptedException {
        Thread.sleep(3000);
        onView(withId(R.id.fragment_map)).perform(click());
        Thread.sleep(1000);
        testRule.getScenario().onActivity(activity -> {
            LatLng address = ((UploadActivityActivity) activity).getAddressLocation();
            assertNotNull(address);
        });
    }

}
