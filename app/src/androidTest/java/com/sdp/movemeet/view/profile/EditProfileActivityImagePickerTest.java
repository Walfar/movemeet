package com.sdp.movemeet.view.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertTrue;

public class EditProfileActivityImagePickerTest {

    private FirebaseStorage fStorage;
    private StorageReference storageReference;
    private String imagePath = "users/ydXLPxZQMXRRVHY4oGUTggDpZvp1/profile.jpg";

    @Rule
    public IntentsTestRule<EditProfileActivity> activityRule = new IntentsTestRule<>(EditProfileActivity.class);

    @Before
    public void setUp() {
        // Setting up Firebase Storage
        fStorage = BackendInstanceProvider.getStorageInstance();
        storageReference = fStorage.getReference();

        // Saving the mocked picked image
        savePickedImage();
    }

    @Test
    public void uploadImageToFirebaseStorage() {
        File dir = activityRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.jpeg");
        Uri imageUri = Uri.fromFile(file);
        ProgressBar progressBar = activityRule.getActivity().findViewById(R.id.progress_bar_edit_profile);
        ImageView profileImage = activityRule.getActivity().findViewById(R.id.image_view_edit_profile_image);
        FirebaseInteraction.uploadImageToFirebase(storageReference, imagePath, imageUri, profileImage, progressBar);
    }

    private void savePickedImage() {
        Bitmap bm = BitmapFactory.decodeResource(activityRule.getActivity().getResources(), R.drawable.run_man);
        assertTrue(bm != null);
        File dir = activityRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.jpeg");
        System.out.println(file.getAbsolutePath());
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
