package com.sdp.movemeet.view.chat;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.main.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth fAuth;

    /*@Rule
    public ActivityTestRule<HomeScreenActivity> mActivityTestRule = new ActivityTestRule<>(HomeScreenActivity.class);*/

    @Test
    public void chatActivityTest() {
        ChatActivity.enableNav = false;
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

        ActivityScenario scenario = ActivityScenario.launch(ChatActivity.class);

        onView(withId(R.id.message_input_text)).perform(replaceText("my message"), closeSoftKeyboard());

        onView(withId(R.id.button_send_message)).perform(click()); //.perform(forceDoubleClick());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference rootRef = firebaseDatabase.getReference();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalSize = (int) dataSnapshot.getChildrenCount();
                onView(withIndex(withId(R.id.message_text), totalSize)).check(matches(withText("my message")));

                FirebaseUser user = fAuth.getCurrentUser();
                if (user != null) {
                    // Logging out the user from Firebase
                    FirebaseAuth.getInstance().signOut();
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
}
