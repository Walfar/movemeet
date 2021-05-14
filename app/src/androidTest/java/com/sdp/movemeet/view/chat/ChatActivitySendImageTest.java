package com.sdp.movemeet.view.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.profile.EditProfileActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.Assert.assertTrue;

public class ChatActivitySendImageTest {

//    private StorageReference storageReference;
//    @Rule
//    public IntentsTestRule<ChatActivity> activityRule = new IntentsTestRule<>(ChatActivity.class);
//
//    @Before
//    public void setUp() {
//        // Setting up Firebase Storage
//        storageReference = FirebaseStorage.getInstance().getReference();
//
//        // Saving the mocked picked image
//        savePickedImage();
//    }
//
//    @Test
//    public void sendImageViaChat() {
//        File dir = activityRule.getActivity().getExternalCacheDir();
//        File file = new File(dir.getPath(), "pickImageResult.jpeg");
//        Uri imageUri = Uri.fromFile(file);
//        String fullNameString = "Anthony";
//        String userId = "xzkBeYkTY7ccFIYY6F9OHDuup8I2";
//        activityRule.getActivity().createTempMessage(imageUri, fullNameString, userId);
//    }
//
//
//
//    private void savePickedImage() {
//        Bitmap bm = BitmapFactory.decodeResource(activityRule.getActivity().getResources(), R.drawable.run_man);
//        assertTrue(bm != null);
//        File dir = activityRule.getActivity().getExternalCacheDir();
//        File file = new File(dir.getPath(), "pickImageResult.jpeg");
//        System.out.println(file.getAbsolutePath());
//        FileOutputStream outStream = null;
//        try {
//            outStream = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
