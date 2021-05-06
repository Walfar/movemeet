package com.sdp.movemeet.viewTest.activityTest;

import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendActivityManager;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.main.MainActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Callback;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4ClassRunner.class)
public class UploadActivityActivityTest {

    private Task<DocumentSnapshot> getUserTask;

    private BackendSerializer<Activity> activitySerializer;
    private FirestoreActivityManager backendActivityManager;


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
    public void setup() {
        BackendInstanceProvider.firestore = mock(FirebaseFirestore.class);
        AuthenticationInstanceProvider.fAuth = mock(FirebaseAuth.class);

        FirebaseUser user = mock(FirebaseUser.class);
        String uid = "uid";

        when(AuthenticationInstanceProvider.fAuth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn(uid);

        Intents.init();
    }

    @Test
    public void confirmActivityUploadSilentlyFailsOnNullActivity() {

        UploadActivityActivity.enableNav = false;

        ActivityScenario scenario = ActivityScenario.launch(UploadActivityActivity.class);

        scenario.onActivity(activity -> {
            UploadActivityActivity spyActivity = spy((UploadActivityActivity) activity);
            doReturn(null).when(spyActivity).validateActivity();
            spyActivity.confirmActivityUpload(null);
        });
    }

    @Test
    public void confirmActivityUploadReturnsToMainOnSuccessfulUpload() {

        UploadActivityActivity.enableNav = false;

        Activity mockActivity = mock(Activity.class);

        Task addTask = mock(Task.class);

        BackendManager<Activity> activityBackendManager = new FirestoreActivityManager(
                BackendInstanceProvider.getFirestoreInstance(),
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                new ActivitySerializer()
        );
        BackendManager<Activity> spyActivityManager = spy(activityBackendManager);
        doReturn(addTask).when(spyActivityManager).add(any(Activity.class), anyString());

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
                    spyActivity.confirmActivityUpload(activity.getCurrentFocus());
                    successCaptor.getValue().onSuccess(null);
                }
            });
        });
        intended(hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }


    // This test has to take extra time or the Views won't update fast enough and it'll fail on CI
    //@Test
    //@LargeTest
    /*public void endToEnd() {
        ActivityScenario scenario = ActivityScenario.launch(UploadActivityActivity.class);

        sleep(1000);

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

        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assert(sleep(1000));

        onView(withId(R.id.editTextNParticipants))
                .perform(typeText("5"), closeSoftKeyboard());

        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validParticipants == true);
            assert (((UploadActivityActivity) activity).validLocation == false);
        });

        assert(sleep(1000));

        onView(withId(R.id.editTextLocation))
                .perform(typeText("Dubai, UAE"), closeSoftKeyboard());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        sleep(1000);

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validLocation == true);
            //assert (((UploadActivityActivity) activity).validStartTime == false);
        });

        onView(withId(R.id.editTextStartTime)).perform(forceDoubleClick());

        sleep(1000);

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        9, 15
                )
        );

        sleep(1000);

        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        sleep(1000);

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validStartTime == true);
            assert (((UploadActivityActivity) activity).validDate == false);
        });

        onView(withId(R.id.editTextTime)).perform(forceDoubleClick());

        sleep(1000);

        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(
                PickerActions.setTime(
                        2, 30
                )
        );

        onView(withText("OK")).perform(click());
        sleep(1000);

        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validDate == true);
        });


        onView(withId(R.id.editTextDate)).perform(forceDoubleClick());

        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(
                PickerActions.setDate(
                        2025, 0, 20
                )
        );

        assert(sleep(1000));

        onView(withText("OK")).perform(click());
        onView(withId(R.id.buttonConfirmUpload)).perform(click());

        assert(sleep(1000));

        scenario.onActivity(activity -> {
            assert (((UploadActivityActivity) activity).validDate == true);
        });

        scenario.close();

    }*/

    /*public boolean sleep(int millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }*/

}
