package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.home.LoginActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
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
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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

    @Rule
    public ActivityScenarioRule<ActivityDescriptionActivityUnregister> testRule = new ActivityScenarioRule<>(new Intent(getApplicationContext(), ActivityDescriptionActivityUnregister.class).putExtra("activity", activity));

    @Before
    public void createDescription() {
         onView(withId(R.id.activity_title_description)).check(matches(withText(DUMMY_TITLE)));
        //onView(withId(R.id.activity_date_description)).check(matches(withText(String.valueOf(DUMMY_DATE))));
        onView(withId(R.id.activity_address_description)).check(matches(withText(DUMMY_ADDRESS)));
        onView(withId(R.id.activity_sport_description)).check(matches(withText(String.valueOf(DUMMY_SPORT))));
        //onView(withId(R.id.activity_duration_description)).check(matches(withText(String.valueOf(DUMMY_DURATION))));

        onView(withId(R.id.activity_organisator_description)).check(matches(withText(DUMMY_ORGANISATOR_ID)));
        //onView(withId(R.id.activity_number_description)).check(matches(withText(String.valueOf(DUMMY_NUMBER_PARTICIPANT))));
        //onView(withId(R.id.activity_participants_description)).check(matches(withText(String.valueOf(DUMMY_PARTICIPANTS_ID.size()))));
    }

    @Test
    public void create() {
        Intents.init();

        Intents.release();
    }

    @Test
    public void loginButtonIsCorrect(){
        Intents.init();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            assert (false);
        }
        onView(withId(R.id.activityGoDescription)).perform(scrollTo(), click());
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();
    }

}
