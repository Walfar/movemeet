package com.sdp.movemeet.HomeScreen;

import androidx.annotation.NonNull;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.util.regex.Pattern.matches;

@RunWith(AndroidJUnit4.class)
public class HomeScreenActivityTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> HomeScreenTestRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Test
    public void mainActivity_signInLogged() throws InterruptedException {

        onView(withId(R.id.signInButton)).perform(click());
        Thread.sleep(2000);
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void mainActivity_signInUnlogged() throws InterruptedException {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.signInWithEmailAndPassword("test@test.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                onView(withId(R.id.signInButton)).perform(click());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intended(hasComponent(MainActivity.class.getName()));
            }
        });
        Thread.sleep(5000);
    }

    @Test
    public void mainActivity_noAccount() {
        onView(withId(R.id.noAccountButton)).perform(click());
    }
}

