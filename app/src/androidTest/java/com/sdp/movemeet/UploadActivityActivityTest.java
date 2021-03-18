package com.sdp.movemeet;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4ClassRunner.class)
public class UploadActivityActivityTest {

    @Rule
    public ActivityScenarioRule<UploadActivityActivity> testRule = new ActivityScenarioRule<>(UploadActivityActivity.class);

    @Test
    public void endToEnd() {
        ActivityScenario scenario = testRule.getScenario();
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validParticipants == false);
        });

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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validLocation == true);
            assert (((UploadActivityActivity) activity).validStartTime == false);
        });

        onView(withId(R.id.editTextStartTime)).perform(click());
        onView(withId(R.id.editTextStartTime)).perform(click());

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        9, 15
                )
        );
        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validStartTime == true);
            assert (((UploadActivityActivity) activity).validDate == false);
        });

        onView(withId(R.id.editTextTime)).perform(click());
        onView(withId(R.id.editTextTime)).perform(click());

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        2, 30
                )
        );
        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validDate == true);
        });


        onView(withId(R.id.editTextDate)).perform(click());
        onView(withId(R.id.editTextDate)).perform(click());

        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(
                PickerActions.setDate(
                        2025, 0, 20
                )
        );
        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validDate == true);
        });

    }
}

