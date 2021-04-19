package com.sdp.movemeet.map;

import android.Manifest;
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
    public ActivityScenarioRule<UploadActivityActivity> testRule = new ActivityScenarioRule<>(UploadActivityActivity.class);

    @Rule
    public ActivityTestRule rule = new ActivityTestRule<>(UploadActivityActivity.class);

    @Rule
    public FragmentTestRule<?, MiniMapFragment> fragmentTestRule =
            FragmentTestRule.create(MiniMapFragment.class);

    @Before
    public void setUp() throws InterruptedException {
        rule.getActivity().getFragmentManager().beginTransaction();
        Thread.sleep(2000);
        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
                user = fAuth.getCurrentUser();
            }
        });
        Thread.sleep(2000);
    }


    @Test
    public void miniMapFragment_isDisplayed() throws InterruptedException {
        onView(withId(R.id.fragment_map)).check(matches((isDisplayed())));
    }

    @Test
    public void miniMapFragment_onclickSetsLocation() throws InterruptedException {

    }
}
