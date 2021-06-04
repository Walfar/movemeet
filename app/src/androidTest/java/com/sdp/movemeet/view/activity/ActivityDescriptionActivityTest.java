package com.sdp.movemeet.view.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.view.chat.ChatActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class ActivityDescriptionActivityTest {

    private static final String TAG = "ActDescActTest";

    private final static String DUMMY_ACTIVITY_ID = "12345";
    private final static String DUMMY_ORGANISATOR_ID = "jOUwVmwQVFgYKdH35jYm50l9TJx1"; // Antho3
    private final static String DUMMY_ORGANISATOR_NAME = "Antho3";
    private final static String DUMMY_TITLE = "dummy_title";
    private final static int DUMMY_NUMBER_PARTICIPANT = 23;
    private final static ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>() {{
        add("jOUwVmwQVFgYKdH35jYm50l9TJx1");
    }}; // Antho3
    private final static double DUMMY_LONGITUDE = 2.45;
    private final static double DUMMY_LATITUDE = 3.697;
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_DOCUMENT_PATH = "activities/lTGOJB8PbU0Dx3SnntOs"; // Tricking with Antho3
    private final static Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
    private final static double DUMMY_DURATION = 10.4;
    private final static Sport DUMMY_SPORT = Sport.Running;
    private final static String DUMMY_ADDRESS = "dummy_address";
    public FirebaseAuth fAuth;
    private String user;
    private Intent intent;
    private Intent intent_2;

    private final Activity activity = new Activity(
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

    private final Activity activity_2 = new Activity(
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
            Sport.Badminton,
            DUMMY_ADDRESS,
            DUMMY_DATE
    );

    @Before
    public void signIn() {
        CountDownLatch latch = new CountDownLatch(1);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();

        fAuth.signInWithEmailAndPassword("movemeet@gmail.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    latch.countDown();
                } else {
                    assert (false);
                }
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert (false);
        }

    }

    @Test
    public void create() {
        Intents.init();
        Intents.release();
    }

    @Test
    public void testValue() {
        Intents.init();
        Intent intent = new Intent(getApplicationContext(), ActivityDescriptionActivity.class).putExtra(ActivityDescriptionActivity.DESCRIPTION_ACTIVITY_KEY, activity);
        ActivityScenario testRule = ActivityScenario.launch(intent);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(withId(R.id.activity_title_description)).check(matches(withText(DUMMY_TITLE)));
        onView(withId(R.id.activity_address_description)).check(matches(withText(DUMMY_ADDRESS)));
        onView(withId(R.id.activity_sport_description)).check(matches(withText(String.valueOf(DUMMY_SPORT))));
        onView(withId(R.id.activity_organisator_description)).check(matches(withText(DUMMY_ORGANISATOR_NAME))); // DUMMY_ORGANISATOR_ID

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }
        testRule.close();
        Intents.release();
    }

    @Test
    public void chatButtonIsCorrect() {
        Intents.init();
        activity.addParticipantId(fAuth.getUid());
        Intent intent = new Intent(getApplicationContext(), ActivityDescriptionActivity.class).putExtra(ActivityDescriptionActivity.DESCRIPTION_ACTIVITY_KEY, activity);
        ActivityScenario testRule = ActivityScenario.launch(intent);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(withId(R.id.activityChatDescription)).perform(scrollTo(), click());
        Intents.intended(allOf(hasComponent(ChatActivity.class.getName())));

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }

        testRule.close();
        Intents.release();
    }

    @After
    public void signOut() {
        fAuth.signOut();
    }

    public boolean sleep(int millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

}