package com.sdp.movemeet.view.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.utility.ImageHandler;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;

// This test tests both ImageHandler.uploadImage and ImageHandler.loadImage since the later one is
// an integral part of the first one.
public class EditProfileActivityImagePickerTest {

    private static final String USER_IMAGE_PATH = "users/B6PwbuQT3rRIyOemWtNQu7xfXmq2/profile.jpg"; // Antho1

    private FirebaseAuth fAuth;

    @Rule
    public IntentsTestRule<EditProfileActivity> activityRule = new IntentsTestRule<>(EditProfileActivity.class);

    @Before
    public void setUp() {
        // Saving the mocked picked image
        savePickedImage();

        CountDownLatch latch = new CountDownLatch(1);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signInWithEmailAndPassword("antho1@gmail.com", "123456").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                latch.countDown();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assert (false);
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            assert (false);
        }

        ActivityScenario scenario = ActivityScenario.launch(EditProfileActivity.class);

    }

    @Test
    public void uploadImageToFirebaseStorage() {
        File dir = activityRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.png");
        Uri imageUri = Uri.fromFile(file);
        ProgressBar progressBar = activityRule.getActivity().findViewById(R.id.progress_bar_edit_profile);
        ImageView profileImage = activityRule.getActivity().findViewById(R.id.image_view_edit_profile_image);
        Image image = new Image(imageUri, profileImage);
        image.setDocumentPath(USER_IMAGE_PATH);
        ImageHandler.uploadImage(image, activityRule.getActivity());

        try {
            Thread.sleep(4000);
        } catch (Exception e) {
        }

        // Check that image matches
        onView(withId(R.id.image_view_edit_profile_image)).check(matches(withDrawable(R.drawable.icon_tricking)));
    }

    private void savePickedImage() {
        Bitmap bm = BitmapFactory.decodeResource(activityRule.getActivity().getResources(), R.drawable.icon_tricking);
        assertTrue(bm != null);
        File dir = activityRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.png");
        System.out.println(file.getAbsolutePath());
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    @After
    public void signOut() {
        fAuth.signOut();
    }

}
