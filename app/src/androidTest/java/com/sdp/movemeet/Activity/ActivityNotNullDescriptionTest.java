/* package com.sdp.movemeet.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

;import java.util.ArrayList;
import java.util.Date;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

@RunWith(AndroidJUnit4.class)
public class ActivityNotNullDescriptionTest {

    private final static String DUMMY_ACTIVITY_ID = "12345";
    private final static String DUMMY_ORGANISATOR_ID = "1";
    private final static String DUMMY_TITLE = "title";
    private final static int DUMMY_NUMBER_PARTICIPANT = 2;
    private final static ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>();
    private final static double DUMMY_LONGITUDE = 2.45;
    private final static double DUMMY_LATITUDE = 3.697;
    private final static String DUMMY_DESCRIPTION = "description";
    private final static Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
    private final static double DUMMY_DURATION = 10.4;
    private final static Sport DUMMY_SPORT = Sport.Running;
    private final static String DUMMY_ADDRESS = "address";

    private Activity activity = new Activity(
            DUMMY_ACTIVITY_ID,
            DUMMY_ORGANISATOR_ID,
            DUMMY_TITLE,
            DUMMY_NUMBER_PARTICIPANT,
            DUMMY_PARTICIPANTS_ID,
            DUMMY_LONGITUDE,
            DUMMY_LATITUDE,
            DUMMY_DESCRIPTION,
            DUMMY_DATE,
            DUMMY_DURATION,
            DUMMY_SPORT,
            DUMMY_ADDRESS
    );

    @Rule
    public ActivityScenarioRule<ActivityDescriptionActivity> testRule = new ActivityScenarioRule<>(new Intent(getApplicationContext(), ActivityDescriptionActivity.class).putExtra("activity", activity));

    @Test
    public void create() {
        Intents.init();
        Intents.release();
    }

} */