package com.sdp.movemeet.viewTest.navigationTest.securityTest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.R;
import com.sdp.movemeet.view.chat.ChatActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class ChatActivitySecurityTest {

    @Rule
    public ActivityScenarioRule<ChatActivity> testRule = new ActivityScenarioRule<>(ChatActivity.class);

    @Test
    public void redirectionTest() {
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
    }
}