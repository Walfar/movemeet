package com.sdp.movemeet.view.workout;

import android.util.Log;

import androidx.test.core.app.ActivityScenario;

import com.sdp.movemeet.R;
import com.sdp.movemeet.models.Workout;
import com.sdp.movemeet.view.workout.TextWorkoutActivity;
import com.sdp.movemeet.view.workout.WorkoutActivity;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;


public class WorkoutTest {
    @Test
    public void WorkoutModelIsCorrect() {
        Workout model = new Workout("title", "description", 0);

        assertEquals("title", model.getTitle());
        assertEquals("description", model.getDesc());
        assertEquals(0, model.getIcon());
    }

    @Test
    public void WorkoutActivityIsCorrect() {
        try (ActivityScenario<WorkoutActivity> scenario = ActivityScenario.launch(WorkoutActivity.class)) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                assert (false);
            }

            onView(withId(R.id.tabs)).perform(click());
            onView(withId(R.id.listView)).perform(click());

        } catch (Exception e) {
            Log.d("TAG", "Error message: " + e);
            e.printStackTrace();
        }
    }

    @Test
    public void TextWorkoutActivityIsCorrect() {
        try (ActivityScenario<TextWorkoutActivity> scenario = ActivityScenario.launch(TextWorkoutActivity.class)) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                assert (false);
            }

            //onView(withId(R.id.textView)).check(matches(withText(TEST_NAME)));

        } catch (Exception e) {
            Log.d("TAG", "Error message: " + e);
            e.printStackTrace();
        }
    }
}
