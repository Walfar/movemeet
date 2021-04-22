package com.sdp.movemeet.map;

import android.Manifest;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
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
import com.sdp.movemeet.UploadActivityActivity;

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
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MiniMapFragmentTest {
    private UiDevice uiDevice;
    private FirebaseAuth fAuth;
    private FirebaseUser user;

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public FragmentTestRule<?, MiniMapFragment> fragmentTestRule =
            FragmentTestRule.create(MiniMapFragment.class);

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
        sleep(2000);
    }

    @Test
    public void miniMapFragment_isDisplayed() throws InterruptedException {
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
    }


    @Test
    public void miniMapFragment_onClickSetsLocation() {
        //What could be the right coords of the mini map ?
        uiDevice.click(uiDevice.getDisplaySizeDp().x/2, uiDevice.getDisplaySizeDp().y/2);
        sleep(2000);
        UploadActivityActivity act = ((UploadActivityActivity) fragmentTestRule.getFragment().getActivity());
        LatLng address = act.getAddressLocation();
        assertNotNull(address);
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
