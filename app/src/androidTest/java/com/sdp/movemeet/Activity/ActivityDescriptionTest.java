package com.sdp.movemeet.Activity;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.sdp.movemeet.R;
import com.sdp.movemeet.RegisterActivity;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.map.MapsActivity;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdp.movemeet.Sport.Soccer;

@RunWith(AndroidJUnit4.class)
public class ActivityDescriptionTest {

    @Rule
    public ActivityScenarioRule<ActivityDescriptionActivity> testRule = new ActivityScenarioRule<>(ActivityDescriptionActivity.class);



    @Test
    public void create() {
        Intents.init();

        Intents.release();
    }


    /*@Test
    public void addUserToActivity() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }
        Intents.init();
        onView(withId(R.id.activityRegisterDescription)).perform(click(), closeSoftKeyboard());

        Intents.release();
    }*/
}
