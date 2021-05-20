package com.sdp.movemeet.backend.providers;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.sdp.movemeet.view.home.HomeScreenActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class AuthenticationInstanceProviderTest {

    FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> testRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Before
    public void setup() {
        AuthenticationInstanceProvider.fAuth = mockFirebaseAuth;
    }

    @Test
    public void AuthenticationInstanceProviderReturnsMocksCorrectly() {
        assertEquals(mockFirebaseAuth, AuthenticationInstanceProvider.getAuthenticationInstance());
    }

    @After
    public void tearDown() {
        AuthenticationInstanceProvider.fAuth = FirebaseAuth.getInstance();
    }
}