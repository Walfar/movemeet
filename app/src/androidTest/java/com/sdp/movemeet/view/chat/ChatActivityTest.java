package com.sdp.movemeet.view.chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firebaseDB.FirebaseDBMessageManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.MessageSerializer;
import com.sdp.movemeet.models.Message;
import com.sdp.movemeet.utility.ImageHandler;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {

    private FirebaseAuth fAuth;
    public static final String CHAT_MESSAGE = "my message";
    private static final String TEST_EMAIL = "movemeet@gmail.com";
    public static final String TEST_PASSWORD = "password";

    private BackendManager<Message> messageManager;

    @Before
    public void signIn() {
        CountDownLatch latch = new CountDownLatch(1);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();

        fAuth.signInWithEmailAndPassword(TEST_EMAIL, TEST_PASSWORD).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    latch.countDown();
                } else {
                    assert (false);
                }
            }
        });

        try {
            latch.await();
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
    public void chatActivityTest() {
        ChatActivity.enableNav = false;

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(withId(R.id.message_input_text)).perform(replaceText(CHAT_MESSAGE), closeSoftKeyboard());

        onView(withId(R.id.button_send_message)).perform(forceDoubleClick()); //.perform(click()); //.perform(forceDoubleClick());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }

        FirebaseDatabase firebaseDatabase = BackendInstanceProvider.getDatabaseInstance();

        DatabaseReference rootRef = firebaseDatabase.getReference();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalSize = (int) dataSnapshot.getChildrenCount();
                onView(withIndex(withId(R.id.message_text), totalSize)).check(matches(withText(CHAT_MESSAGE)));

                FirebaseUser user = fAuth.getCurrentUser();
                if (user != null) {
                    // Logging out the user from Firebase
                    fAuth.signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

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

    @After
    public void deleteAndSignOut() {

        // TODO: delete the default_chat node in Firebase Realtime Database here
        //ChatActivity.DEFAULT_CHAT_CHILD;


        fAuth.signOut();
    }
}