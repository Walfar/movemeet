package com.sdp.movemeet.view.activity;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.view.main.MainActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class UploadActivityActivityTest {

    private Task<DocumentSnapshot> getUserTask;

    private BackendSerializer<Activity> activitySerializer;
    private FirestoreActivityManager backendActivityManager;
    private FirestoreActivityManager spyActivityManager;

    private Task addTask;
    private Activity mockActivity;
    private ArgumentCaptor<OnSuccessListener> successCaptor;
    private ArgumentCaptor<OnFailureListener> failureCaptor;


    @Before
    public void setup() {
        // Disable navigation for tests
        UploadActivityActivity.enableNav = false;
        MainActivity.enableNav = false;

        // Set up mocks and their behaviors
        // Set up fake database
        BackendInstanceProvider.firestore = mock(FirebaseFirestore.class);

        // Set up fake authentication
        AuthenticationInstanceProvider.fAuth = mock(FirebaseAuth.class);
        FirebaseUser user = mock(FirebaseUser.class);
        when(AuthenticationInstanceProvider.fAuth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn("uid");

        Intents.init();
    }

    @Test
    public void confirmActivityUploadSilentlyFailsOnNullActivity() {

        ActivityScenario scenario = ActivityScenario.launch(UploadActivityActivity.class);

        scenario.onActivity(activity -> {
            UploadActivityActivity spyActivity = spy((UploadActivityActivity) activity);
            doReturn(null).when(spyActivity).validateActivity();
            spyActivity.confirmActivityUpload(null);
        });

        scenario.close();
    }

    @Test
    public void confirmActivityUploadReturnsToMainOnSuccessfulUpload() {

        // Set up fake activity upload
        Activity mockActivity = mock(Activity.class);
        Task addTask = mock(Task.class);

        BackendManager<Activity> activityBackendManager = new FirestoreActivityManager(
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                new ActivitySerializer()
        );
        BackendManager<Activity> spyActivityManager = spy(activityBackendManager);
        doReturn(addTask).when(spyActivityManager).add(any(Activity.class), anyString());

        // Capture OnSuccess and OnFailure callbacks to call them manually
        ArgumentCaptor<OnSuccessListener> successCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);
        ArgumentCaptor<OnFailureListener> failureCaptor = ArgumentCaptor.forClass(OnFailureListener.class);

        when(addTask.addOnSuccessListener(successCaptor.capture())).thenReturn(addTask);
        when(addTask.addOnFailureListener(failureCaptor.capture())).thenReturn(addTask);

        ActivityScenario scenario = ActivityScenario.launch(UploadActivityActivity.class);

        scenario.onActivity(activity -> {
            UploadActivityActivity spyActivity = spy((UploadActivityActivity) activity);
            doReturn(mockActivity).when(spyActivity).validateActivity();
            spyActivity.activityBackendManager = spyActivityManager;

            spyActivity.runOnUiThread(new Runnable() {
                public void run() {
                    spyActivity.confirmActivityUpload(spyActivity.getCurrentFocus());
                    successCaptor.getValue().onSuccess(null);
                }
            });
        });
        intended(hasComponent(MainActivity.class.getName()));

        scenario.close();
    }

    @Test
    public void confirmActivityUploadStaysOnPageOnFailure() {

        // Set up fake activity upload
        Activity mockActivity = mock(Activity.class);
        Task addTask = mock(Task.class);

        BackendManager<Activity> activityBackendManager = new FirestoreActivityManager(
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                new ActivitySerializer()
        );
        BackendManager<Activity> spyActivityManager = spy(activityBackendManager);
        doReturn(addTask).when(spyActivityManager).add(any(Activity.class), anyString());

        // Capture OnSuccess and OnFailure callbacks to call them manually
        ArgumentCaptor<OnSuccessListener> successCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);
        ArgumentCaptor<OnFailureListener> failureCaptor = ArgumentCaptor.forClass(OnFailureListener.class);

        when(addTask.addOnSuccessListener(successCaptor.capture())).thenReturn(addTask);
        when(addTask.addOnFailureListener(failureCaptor.capture())).thenReturn(addTask);

        ActivityScenario scenario = ActivityScenario.launch(UploadActivityActivity.class);

        scenario.onActivity(activity -> {
            UploadActivityActivity spyActivity = spy((UploadActivityActivity) activity);
            doReturn(mockActivity).when(spyActivity).validateActivity();
            spyActivity.activityBackendManager = spyActivityManager;

            spyActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spyActivity.confirmActivityUpload(spyActivity.getCurrentFocus());
                    failureCaptor.getValue().onFailure(new Exception());
                }
            });
        });

        // Only 1 Intent was captured: The one created by ActivityScenario.launch()
        assertThat(Intents.getIntents().size(), is(1));

        scenario.close();
    }


    // This test has to take extra time or the Views won't update fast enough and it'll fail on CI
    @Test
    @LargeTest
    public void uploadFormWorksAsExpected() {
        ActivityScenario scenario = ActivityScenario.launch(UploadActivityActivity.class);
        ResultCaptor<Activity> resultCaptor = new ResultCaptor();

        CountDownLatch latch = new CountDownLatch(1);

        sleep(1000); // Give time for Activity to launch

        // Fails when no information is put in
        onView(withId(R.id.buttonConfirmUpload)).perform(click());
        assertValidationFails(scenario, resultCaptor, latch);
        assertThat(Intents.getIntents().size(), is(1));

        // Fails when only date is put int
        latch = new CountDownLatch(1);
        onView(withId(R.id.editTextDate)).perform(forceDoubleClick());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(
                PickerActions.setDate(
                        2025, 0, 20
                )
        );
        assert (sleep(1000));

        onView(withText("OK")).perform(click());
        //onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assertValidationFails(scenario, resultCaptor, latch);

        // Fail when only date + number of participants are put in
        latch = new CountDownLatch(1);
        onView(withId(R.id.editTextNParticipants))
                .perform(typeText("5"), closeSoftKeyboard());

        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assertValidationFails(scenario, resultCaptor, latch);

        // Fails when only date, nParticipants, and location are put in
        latch = new CountDownLatch(1);

        onView(withId(R.id.editTextLocation))
                .perform(typeText("Dubai, UAE"), closeSoftKeyboard());

        assertValidationFails(scenario, resultCaptor, latch);

        // Fails when only date, nParticipants, location and start time are put in
        latch = new CountDownLatch(1);


        onView(withId(R.id.editTextStartTime)).perform(forceDoubleClick());

        sleep(1000);

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        9, 15
                )
        );

        sleep(1000);

        onView(withText("OK")).perform(click());

        sleep(1000);

        assertValidationFails(scenario, resultCaptor, latch);

        // Fails when only date, nParticipants, location, start time and end time are put in
        latch = new CountDownLatch(1);

        onView(withId(R.id.editTextTime)).perform(forceDoubleClick());

        sleep(1000);

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        2, 30
                )
        );
        sleep(1000);

        onView(withText("OK")).perform(click());

        sleep(1000);

        // Verify that it now uploads correctly
        Task addTask = mock(Task.class);

        BackendManager<Activity> activityBackendManager = new FirestoreActivityManager(
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                new ActivitySerializer()
        );
        BackendManager<Activity> spyActivityManager = spy(activityBackendManager);
        doReturn(addTask).when(spyActivityManager).add(any(Activity.class), anyString());

        // Setup OnSuccess callback capture
        ArgumentCaptor<OnSuccessListener> successCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);
        when(addTask.addOnSuccessListener(successCaptor.capture())).thenReturn(addTask);

        scenario.onActivity(activity -> {
            UploadActivityActivity spyActivity = spy((UploadActivityActivity) activity);
            spyActivity.activityBackendManager = spyActivityManager;

            spyActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spyActivity.confirmActivityUpload(spyActivity.getCurrentFocus());
                    successCaptor.getValue().onSuccess(null);
                }
            });
        });

        assert (Intents.getIntents().size() == 2);

        scenario.close();

    }

    @After
    public void tearDown() {
        Intents.release();
        BackendInstanceProvider.database = FirebaseDatabase.getInstance();
        BackendInstanceProvider.storage = FirebaseStorage.getInstance();
        BackendInstanceProvider.firestore = FirebaseFirestore.getInstance();

        AuthenticationInstanceProvider.fAuth = FirebaseAuth.getInstance();
    }


    public static ViewAction forceDoubleClick() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isClickable(), isEnabled(), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "force click";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick(); // perform click without checking view coordinates.
                view.performClick();
                uiController.loopMainThreadUntilIdle();
            }
        };
    }

    private void assertValidationFails(ActivityScenario scenario, ResultCaptor<Activity> resultCaptor, CountDownLatch latch) {

        scenario.onActivity(activity -> {
            UploadActivityActivity spyActivity = spy((UploadActivityActivity) activity);
            spyActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spyActivity.confirmActivityUpload(activity.getCurrentFocus());
                    doAnswer(resultCaptor).when(spyActivity).validateActivity();
                    assertNull(resultCaptor.getResult());
                    latch.countDown();
                }
            });
        });

        assert (wait(latch));
    }


    // Taken from https://stackoverflow.com/questions/18906983/how-to-validate-the-return-value-when-calling-a-mocked-objects-method
    // -> Use to capture a spied function's result
    private class ResultCaptor<T> implements Answer {
        private T result = null;

        public T getResult() {
            return result;
        }

        @Override
        public T answer(InvocationOnMock invocationOnMock) throws Throwable {
            result = (T) invocationOnMock.callRealMethod();
            return result;
        }
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

    public boolean wait(CountDownLatch latch) {
        try {
            latch.await();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

}
