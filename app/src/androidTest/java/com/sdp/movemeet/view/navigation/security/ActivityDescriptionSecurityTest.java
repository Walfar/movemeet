package com.sdp.movemeet.view.navigation.security;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)

public class ActivityDescriptionSecurityTest {

    @Before
    public void setup() {
        FirebaseAuth mockAuth = mock(FirebaseAuth.class);
        AuthenticationInstanceProvider.fAuth = mockAuth;
        when(mockAuth.getCurrentUser()).thenReturn(null);
        Intents.init();
    }

    @Test
    public void redirectionTest() {
        Navigation.profileField = false;
        ActivityScenario scenario = ActivityScenario.launch(ActivityDescriptionActivity.class);

        intended(hasComponent(hasClassName(LoginActivity.class.getName())));
    }

    @After
    public void tearDown() {
        Intents.release();
        AuthenticationInstanceProvider.fAuth = FirebaseAuth.getInstance();
    }
}