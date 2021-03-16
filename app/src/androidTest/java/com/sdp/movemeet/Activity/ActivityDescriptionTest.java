package com.sdp.movemeet.Activity;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdp.movemeet.R;
import com.sdp.movemeet.RegisterActivity;
import com.sdp.movemeet.Sport;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ActivityDescriptionTest {

    @Rule
    public ActivityScenarioRule<ActivityDescriptionActivity> testRule = new ActivityScenarioRule<>(ActivityDescriptionActivity.class);


   /* @Test
    public void create() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ActivityDescriptionActivity.class);
        ActivityScenario.launch(intent);
        Activity act =  new Activity("activityId",
                "organizerId",
                "Snowboard",
                5,
                new ArrayList<>(),
                004,
                005,
                "BlaBlaBla Description de l'activité",
                new Date(),
                90,
                Sport.Running,
                "address");

        intent.putExtra("activity", act);
    } */
}
