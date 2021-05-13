package com.sdp.movemeet.backend.providers;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.view.home.HomeScreenActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class AuthentificationInstanceProviderTest {

    FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> testRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Before
    public void setup() {
        AuthenticationInstanceProvider.fAuth = mockFirebaseAuth;
    }

    @Test
    public void AuthentificationInstanceProviderReturnsMocksCorrectly() {
        assertEquals(mockFirebaseAuth, AuthenticationInstanceProvider.getAuthenticationInstance());
    }
}