package com.sdp.movemeet.Profile;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdp.movemeet.EditProfileActivity;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.ProfileActivity;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class ProfileEditProfileIntentTest {

    public static final String TEST_FULL_NAME = "Your Name";
    public static final String TEST_EMAIL = "Your Email Address";
    public static final String TEST_PHONE = "Phone Number";
    public static final String TEST_DESCRIPTION = "Description";

    @Rule
    public ActivityScenarioRule<LoginActivity> testRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void editProfileActivity_getsCorrectData() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(context, EditProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_FULL_NAME, TEST_FULL_NAME);
        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_EMAIL, TEST_EMAIL);
        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_PHONE, TEST_PHONE);
        intent.putExtra(ProfileActivity.EXTRA_MESSAGE_DESCRIPTION, TEST_DESCRIPTION);

        try (ActivityScenario<EditProfileActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.edit_text_edit_profile_full_name)).check(matches(withText(TEST_FULL_NAME)));
            onView(withId(R.id.edit_text_edit_profile_email)).check(matches(withText(TEST_EMAIL)));
            onView(withId(R.id.edit_text_edit_profile_phone)).check(matches(withText(TEST_PHONE)));
            onView(withId(R.id.edit_text_edit_profile_description)).check(matches(withText(TEST_DESCRIPTION)));
        }
    }

}
