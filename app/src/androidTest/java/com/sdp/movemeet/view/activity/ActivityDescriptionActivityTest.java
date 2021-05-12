package com.sdp.movemeet.view.activity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
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

;

@RunWith(AndroidJUnit4.class)
public class ActivityDescriptionActivityTest {

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

    private FirebaseFirestore fStore;
    private StorageReference storageReference;

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

        ActivityScenarioRule<ActivityDescriptionActivity> testRule = new ActivityScenarioRule<>(new Intent(getApplicationContext(), ActivityDescriptionActivity.class).putExtra("activity", activity));
    }

    /*@Before
    public void setUp(){
        fStore = mock(FirebaseFirestore.class);
        storageReference = mock(StorageReference.class);
        activity = mock(Activity.class);

    }*/

    @Test
    public void create() {
        Intents.init();
        Intents.release();
    }

    @Test
    public void displayDescriptionActivityDataIsCorrect(){
        ActivityDescriptionActivity activity = new ActivityDescriptionActivity();
        activity.displayDescriptionActivityData();
    }

    /*@Test
    public void createTitleViewTest(){
        ViewGroup view = inflate(R.layout.activity_description);
        TextView textView = view.findViewById(R.id.activity_title_description);

        assertEquals(textView.getText().toString().isEmpty(), false);
    }*/


    @After
    public void deleteAndSignOut() {
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