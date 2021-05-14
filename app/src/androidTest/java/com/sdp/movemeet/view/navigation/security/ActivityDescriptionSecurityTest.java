/*package com.sdp.movemeet.view.navigation.security;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class ActivityDescriptionSecurityTest {

    @Before
    public void checkUserNotLogged() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) firebaseAuth.signOut();
    }

    @Rule
    public ActivityScenarioRule<ActivityDescriptionActivity> testRule = new ActivityScenarioRule<>(ActivityDescriptionActivity.class);

    @Test
    public void redirectionTest() {
        //onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }
} */