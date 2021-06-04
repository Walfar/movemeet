package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Sport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
public class ActivityDescriptionActivityRegisterToActivityTest {

    private static final String TAG = "ActDescActTest";

    private final static String DUMMY_ACTIVITY_ID = "12345";
    private final static String DUMMY_ORGANISATOR_ID = "jOUwVmwQVFgYKdH35jYm50l9TJx1"; // Antho3
    private final static String DUMMY_TITLE = "dummy_title";
    private final static int DUMMY_NUMBER_PARTICIPANT = 23;
    private final static ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>() {{add("jOUwVmwQVFgYKdH35jYm50l9TJx1");}}; // Antho3
    private final static double DUMMY_LONGITUDE = 2.45;
    private final static double DUMMY_LATITUDE = 3.697;
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_DOCUMENT_PATH = "activities/lTGOJB8PbU0Dx3SnntOs"; // Tricking with Antho3
    private final static Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
    private final static double DUMMY_DURATION = 10.4;
    private final static Sport DUMMY_SPORT = Sport.Running;
    private final static String DUMMY_ADDRESS = "dummy_address";
    public FirebaseAuth fAuth;
    private BackendManager<Activity> activityManager;

    private final static String USER_ID = "kNJiyA3rF4boRYhPVGp1YQabXlh2"; // Antho4

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

        Intent intent = new Intent(getApplicationContext(), ActivityDescriptionActivity.class).putExtra(ActivityDescriptionActivity.DESCRIPTION_ACTIVITY_KEY, activity);


        ActivityScenario testRule = ActivityScenario.launch(intent);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }
    }


    @Test
    public void registerToActivity() {

        try (ActivityScenario<ActivityDescriptionActivity> scenario = ActivityScenario.launch(ActivityDescriptionActivity.class)) {

            scenario.onActivity(activityDescriptionActivity -> {

                activityManager = new FirestoreActivityManager(FirestoreActivityManager.ACTIVITIES_COLLECTION, new ActivitySerializer());

                // Register to activity
                activityDescriptionActivity.registerToActivityImplementation(activity, USER_ID);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    assert (false);
                }
                // Assert user successfully registered to activity
                Task<DocumentSnapshot> documentRegister = (Task<DocumentSnapshot>) activityManager.get(activity.getDocumentPath());
                documentRegister.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentRegister = task.getResult();
                            if (documentRegister.exists()) {
                                ActivitySerializer activitySerializer = new ActivitySerializer();
                                Activity updatedActivity = activitySerializer.deserialize(documentRegister.getData());
                                Log.d(TAG, "activity.getParticipantId(): " + updatedActivity.getParticipantId());
                                assertThat(updatedActivity.getParticipantId().contains(USER_ID), is(true));
                                Log.d(TAG, "User successfully registered to activity!");
                            } else {
                                Log.d(TAG, "No such document!");
                            }
                        } else {
                            Log.d(TAG, "Get failed with: ", task.getException());
                        }
                    }
                });

            });

        } catch (Exception e) {
            Log.d("TAG", "deleteAccount Exception: " + e);
            e.printStackTrace();
        }
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