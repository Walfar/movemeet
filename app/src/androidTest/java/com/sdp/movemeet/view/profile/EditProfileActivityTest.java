package com.sdp.movemeet.view.profile;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class EditProfileActivityTest {

    private static final String TEST_FULL_NAME_INITIAL = "Antho1";
    private static final String TEST_EMAIL_INITIAL = "antho1@gmail.com";
    private static final String TEST_PASSWORD_INITIAL = "123456";
    private static final String TEST_PHONE_INITIAL = "+41798841818";
    private static final String TEST_DESCRIPTION_INITIAL = "Hi there!";

    private static final String TEST_FULL_NAME = "Anthony Guinchard";
    private static final String TEST_EMAIL = "antho2@gmail.com";
    private static final String TEST_PHONE = "000000000000";
    private static final String TEST_DESCRIPTION = "";

    private FirebaseAuth fAuth;

    @Before
    public void signIn(){

        // Logging in
        CountDownLatch latch = new CountDownLatch(1);
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signInWithEmailAndPassword(TEST_EMAIL_INITIAL, TEST_PASSWORD_INITIAL).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }

        ActivityScenario scenario = ActivityScenario.launch(ProfileActivity.class);

    }

    @Test
    public void updateProfileByEnteringFieldsAndClickingSaveButton() {

        onView(withId(R.id.button_update_profile)).perform(click());

        try {
            Thread.sleep(1000);
        } catch(Exception e) {}

        // TODO: but with an intent test between ProfileActivity and EditProfileActivity
//        onView(withId(R.id.edit_text_edit_profile_full_name)).check(matches(withText(TEST_NAME_INITIAL)));
//        onView(withId(R.id.edit_text_edit_profile_email)).check(matches(withText(TEST_EMAIL_INITIAL)));
//        onView(withId(R.id.edit_text_edit_profile_phone)).check(matches(withText(String.valueOf(TEST_PHONE_INITIAL))));
//        onView(withId(R.id.edit_text_edit_profile_description)).check(matches(withText(TEST_DESCRIPTION_INITIAL)));

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
    public void signOut() {

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_full_name))
                .perform(replaceText(TEST_FULL_NAME_INITIAL), closeSoftKeyboard()); // To solve the "Android :java.lang.SecurityException: Injecting to another application requires INJECT_EVENTS permission" issue --> cf.: "I solved using replaceText instead of TypeText action" (https://stackoverflow.com/questions/22163424/android-java-lang-securityexception-injecting-to-another-application-requires)

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_email))
                .perform(replaceText(TEST_EMAIL_INITIAL), closeSoftKeyboard());

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_phone))
                .perform(replaceText(TEST_PHONE_INITIAL), closeSoftKeyboard());

        onView(ViewMatchers.withId(R.id.edit_text_edit_profile_description))
                .perform(replaceText(TEST_DESCRIPTION_INITIAL), closeSoftKeyboard());

        onView(withId(R.id.button_edit_profile_save_profile_data)).perform(click());


        fAuth.signOut();
    }

}