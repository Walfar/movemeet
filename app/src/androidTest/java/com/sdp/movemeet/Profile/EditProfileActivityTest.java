package com.sdp.movemeet.Profile;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.EditProfileActivity;
import com.sdp.movemeet.ProfileActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.chat.ChatActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class EditProfileActivityTest {

    public static final String TEST_FULL_NAME = "Anthony Guinchard";
    public static final String TEST_EMAIL = "antho2@gmail.com";
    public static final String TEST_PHONE = "00412356788";
    public static final String TEST_DESCRIPTION = "Hi there! I love Judo!";

    public static final String USER_ID = "cyYedhxTkGV6uihozBJpO5YDyGs1"; // antho2@gmail.com
    public static final String URI = "https://firebasestorage.googleapis.com/v0/b/movemeet-4cbf5.appspot.com/o/users%2FcyYedhxTkGV6uihozBJpO5YDyGs1%2Fprofile.jpg?alt=media&token=70637297-7622-4f99-90ac-7d6a12340066";

    private FirebaseDatabase mDatabase;
    private FirebaseAuth fAuth;

    //@Rule
    //public ActivityScenarioRule<EditProfileActivity> testRule = new ActivityScenarioRule<>(EditProfileActivity.class);

    @Before
    public void signIn(){
        CountDownLatch latch = new CountDownLatch(1);

        fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("movemeet@gmail.com", "password").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                latch.countDown();
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

        ActivityScenario scenario = ActivityScenario.launch(EditProfileActivity.class);
    }

    @Test
    public void updateProfileByEnteringFieldsAndClickingSaveButton() {

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_full_name))
                .perform(replaceText(TEST_FULL_NAME), closeSoftKeyboard()); // To solve the "Android :java.lang.SecurityException: Injecting to another application requires INJECT_EVENTS permission" issue --> cf.: "I solved using replaceText instead of TypeText action" (https://stackoverflow.com/questions/22163424/android-java-lang-securityexception-injecting-to-another-application-requires)

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_email))
                .perform(replaceText(TEST_EMAIL), closeSoftKeyboard());

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_phone))
                .perform(replaceText(TEST_PHONE), closeSoftKeyboard());

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_description))
                .perform(replaceText(TEST_DESCRIPTION), closeSoftKeyboard());

        onView(withId(R.id.button_edit_profile_save_profile_data)).perform(click());

    }

    @After
    public void SignOut() {
        fAuth.signOut();
    }

}