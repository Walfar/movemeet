package com.sdp.movemeet.Activity;

import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.R;
import com.sdp.movemeet.UploadActivityActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4ClassRunner.class)
public class UploadActivityActivityTest {

    @Rule
    public ActivityScenarioRule<UploadActivityActivity> testRule = new ActivityScenarioRule<>(UploadActivityActivity.class);

    public FirebaseAuth mAuth;
    public String uid;

    public static ViewAction forceDoubleClick() {
        return new ViewAction() {
            @Override public Matcher<View> getConstraints() {
                return allOf(isClickable(), isEnabled(), isDisplayed());
            }

            @Override public String getDescription() {
                return "force click";
            }

            @Override public void perform(UiController uiController, View view) {
                view.performClick(); // perform click without checking view coordinates.
                view.performClick();
                uiController.loopMainThreadUntilIdle();
            }
        };
    }


    @Before
    public void signIn() {
        CountDownLatch latch = new CountDownLatch(1);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("movemeet@gmail.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sleep(500);
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

    // This test has to take extra time or the Views won't update fast enough and it'll fail on CI
    @Test
    @LargeTest
    public void endToEnd() {
        ActivityScenario scenario = testRule.getScenario();

        try{
            Thread.sleep(1000);
        }catch(Exception e){}

        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validParticipants == false);
        });

        onView(withId(R.id.editTextDate)).perform(forceDoubleClick());

        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(
                PickerActions.setDate(
                        2025, 0, 20
                )
        );

        assert(sleep(1000));

        onView(withId(R.id.editTextNParticipants))
                .perform(typeText("5"), closeSoftKeyboard());

        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validParticipants == true);
            assert (((UploadActivityActivity) activity).validLocation == false);
        });

        onView(withId(R.id.editTextLocation))
                .perform(typeText("Dubai, UAE"), closeSoftKeyboard());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assert(sleep(1000));

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validLocation == true);
            assert (((UploadActivityActivity) activity).validStartTime == false);
        });

        onView(withId(R.id.editTextStartTime)).perform(forceDoubleClick());

        assert(sleep(1000));

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        9, 15
                )
        );

        assert(sleep(1000));

        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assert(sleep(1000));

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validStartTime == true);
            assert (((UploadActivityActivity) activity).validDate == false);
        });

        onView(withId(R.id.editTextTime)).perform(forceDoubleClick());

        assert(sleep(1000));

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        2, 30
                )
        );

        assert(sleep(1000));

        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assert(sleep(1000));

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validDate == true);
        });


        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assert(sleep(1000));

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validDate == true);
        });

        //mAuth.signOut();
        scenario.close();

    }

    @After
    public void deleteAndSignOut() {
        CountDownLatch latch = new CountDownLatch(1);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        BackendActivityManager bam = new BackendActivityManager(db, BackendActivityManager.ACTIVITIES_COLLECTION);

        bam.getActivitiesCollectionReference()
                .whereEqualTo("organizerId", mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot docSnap: queryDocumentSnapshots.getDocuments()) {
                            docSnap.getReference().delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // Silently fail if document couldn't be deleted ->
                                            // prevent simultaneous deletions causing problems
                                            latch.countDown();
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assert(false);
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert(false);
        }

        mAuth.signOut();
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
