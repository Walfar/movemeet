package com.sdp.movemeet.view.navigation.security;


import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.chat.ChatActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class ChatActivitySecurityTest {

    @Before
    public void checkUserNotLogged() {
        FirebaseAuth fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        if (fAuth.getCurrentUser() != null) fAuth.signOut();
    }

    @Test
    public void redirectionTest() {
        ChatActivity.enableNav = false;
        ActivityScenario scenario = ActivityScenario.launch(ChatActivity.class);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }
}