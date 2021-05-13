package com.sdp.movemeet.view.navigation;

import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.profile.ProfileActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class NavigationTest {

    private FirebaseDatabase mDatabase;
    private FirebaseAuth fAuth;

    //@Rule
    //public ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class)


    @Test
    public void truc(){

}
    /*@Before
    public void signIn(){
        MainActivity.enableNav = true;
        UploadActivityActivity.enableNav = true;
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

        ActivityScenarioRule<MainActivity> testRule = new ActivityScenarioRule<>(MainActivity.class);
    }

    @Test
    public void NavigationTest() {

        ActivityScenario scenario = ActivityScenario.launch(MainActivity.class);
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.nav_home)).perform(click());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.nav_add_activity)).perform(click());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

        onView(withId(R.id.nav_start_activity)).perform(click());

        try{
            Thread.sleep(500);
        }catch(Exception e){}

    }

    @After
    public void SignOut() {
        fAuth.signOut();
    }*/
}

