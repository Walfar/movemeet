package com.sdp.movemeet.view.activity;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivityUnregister;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

@RunWith(AndroidJUnit4.class)
public class ActivityDescriptionActivityUnregisterTest {

    private final static String DUMMY_ACTIVITY_ID = "12345";
    private final static String DUMMY_ORGANISATOR_ID = "1";
    private final static String DUMMY_TITLE = "title";
    private final static int DUMMY_NUMBER_PARTICIPANT = 2;
    private final static ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>();
    private final static double DUMMY_LONGITUDE = 2.45;
    private final static double DUMMY_LATITUDE = 3.697;
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_DOCUMENT_PATH = "documentPath";
    private final static Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
    private final static double DUMMY_DURATION = 10.4;
    private final static Sport DUMMY_SPORT = Sport.Running;
    private final static String DUMMY_ADDRESS = "address";
    public FirebaseAuth fAuth;

    private Activity activity = new Activity(
            DUMMY_ACTIVITY_ID,
            DUMMY_ORGANISATOR_ID,
            DUMMY_TITLE,
            DUMMY_NUMBER_PARTICIPANT,
            DUMMY_PARTICIPANTS_ID,
            DUMMY_LONGITUDE,
            DUMMY_LATITUDE,
            DUMMY_DESCRIPTION,
            DUMMY_DOCUMENT_PATH,
            DUMMY_DATE,
            DUMMY_DURATION,
            DUMMY_SPORT,
            DUMMY_ADDRESS,
            DUMMY_DATE
    );

    @Before
    public void signIn() {
        CountDownLatch latch = new CountDownLatch(1);

        fAuth = FirebaseAuth.getInstance();

        ActivityScenarioRule<ActivityDescriptionActivityUnregister> testRule = new ActivityScenarioRule<>(new Intent(getApplicationContext(), ActivityDescriptionActivityUnregister.class).putExtra("activity", activity));
    }

    @Test
    public void create() {
        Intents.init();
        Intents.release();
    }

}
