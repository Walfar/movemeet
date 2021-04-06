package com.sdp.movemeet.Chat;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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

    private FirebaseDatabase mDatabase;
    private FirebaseAuth fAuth;

    @Rule
    public ActivityTestRule<HomeScreenActivity> mActivityTestRule = new ActivityTestRule<>(HomeScreenActivity.class);

    @Test
    public void chatActivityTest() {

        onView(withId(R.id.signInButton)).perform(click());

        onView(withId(R.id.edit_text_email)).perform(replaceText("antho2@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.edit_text_password)).perform(replaceText("234567"), closeSoftKeyboard());

        onView(withId(R.id.button_login)).perform(forceDoubleClick());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            assert (false);
        }
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        try{
            Thread.sleep(500);
        }catch(Exception e){}
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_chat));

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        //onView(withId(R.id.activityChatDescription)).perform(click());
        //onView(withId(R.id.activityChatDescription)).perform(scrollTo()).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }

        onView(withId(R.id.message_input_text)).perform(replaceText("my message"), closeSoftKeyboard());

        onView(withId(R.id.button_send_message)).perform(click());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            assert (false);
        }


        mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference rootRef = mDatabase.getReference();
        //DatabaseReference messagesRef = rootRef.child("messages");

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

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
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
