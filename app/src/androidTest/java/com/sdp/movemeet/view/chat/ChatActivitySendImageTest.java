package com.sdp.movemeet.view.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firebaseDB.FirebaseDBMessageManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.serialization.MessageSerializer;
import com.sdp.movemeet.models.Message;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.profile.DrawableMatcher;
import com.sdp.movemeet.view.profile.ProfileActivity;

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
import static org.mockito.Mockito.mock;

public class ChatActivitySendImageTest {

    //private StorageReference storageReference;

    private Uri DUMMY_IMAGE_URI = mock(Uri.class);

    private static final String TEST_FULL_NAME = "Antho1";
    private static final String TEST_EMAIL = "antho1@gmail.com";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_USER_ID = "B6PwbuQT3rRIyOemWtNQu7xfXmq2";
    private FirebaseAuth fAuth;
    private BackendManager<Message> messageManager;


    @Rule
    public IntentsTestRule<ChatActivity> activityRule = new IntentsTestRule<>(ChatActivity.class);

    @Before
    public void setUp() {
        // Setting up Firebase Storage
        //storageReference = BackendInstanceProvider.getStorageInstance().getReference();

        // Saving the mocked picked image
        savePickedImage();

        // Logging in
        CountDownLatch latch = new CountDownLatch(1);
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fAuth.signInWithEmailAndPassword(TEST_EMAIL, TEST_PASSWORD).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
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

        // Removing all messages in "default_chat"
        messageManager = new FirebaseDBMessageManager(new MessageSerializer());
        messageManager.delete(ChatActivity.CHATS_CHILD + ImageHandler.PATH_SEPARATOR + ChatActivity.DEFAULT_CHAT_CHILD);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            assert (false);
        }

        ActivityScenario scenario = ActivityScenario.launch(ChatActivity.class);

    }

    @Test
    public void sendImageViaChat() {
        File dir = activityRule.getActivity().getExternalCacheDir();
        File file = new File(dir.getPath(), "pickImageResult.png");
        Uri imageUri = Uri.fromFile(file);
        activityRule.getActivity().createTempMessage(imageUri, TEST_FULL_NAME, TEST_USER_ID);

        try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {
            assert (false);
        }

        // Check that image matches
        onView(withId(R.id.messageImageView)).check(matches(withDrawable(R.drawable.icon_tricking)));

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
