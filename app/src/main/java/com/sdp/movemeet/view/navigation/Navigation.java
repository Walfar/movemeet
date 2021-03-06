package com.sdp.movemeet.view.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.workout.WorkoutActivity;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.profile.ProfileActivity;


public class Navigation extends AppCompatActivity {

    private final Activity activity;
    private final int activityId;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean profileField = true;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    /**
     * A Navigation object capable of creating a Navigation drawer for a
     * specified Android Activity
     *
     * @param activity   the Android activity to work with
     * @param activityId the R.id integer referring to this activity
     */
    public Navigation(Activity activity, int activityId) {
        this.activity = activity;
        this.activityId = activityId;
    }

    /**
     * Starts the ActivityUpload activity
     *
     * @param view the View in which to start the activity
     */
    public static void goToActivityUpload(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UploadActivityActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    /**
     * Starts the User Profile activity
     *
     * @param view the View in which to start the activity
     */
    public static void goToUserProfileActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    /**
     * Starts the MainActivity activity
     *
     * @param view the View in which to start the activity
     */
    public static void goToHome(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts ActivityList activity
     * @param view the View in which to start the activity
     */
    /*public static void goToListOfActivities(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ActivityListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }*/

    /**
     * Starts the Workout activity
     * @param view the View in which to start the activity
     */
    public static void goToWorkoutActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, WorkoutActivity.class);
        context.startActivity(intent);
    }


    /**
     * Sign out the user in case it is not null (i.e. in case the Firebase Authentication service
     * is able to retrieve the user object).
     *
     * @param fAuth    The Firebase Authentication reference that allows to access to the user object
     * @param activity The activity from which this function is called
     */
    public static void logoutIfUserNonNull(FirebaseAuth fAuth, Activity activity) {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            // Logging out the user from Firebase
            fAuth.signOut();
            // Launching the LoginActivity
            Intent intent = new Intent(activity, LoginActivity.class);
            Context context = activity.getApplicationContext();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("EXIT", true);
            context.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * Initializes a Navigation drawer, filling in all fields and setting up associated functions,
     * inside the activity passed to this object's constructor.
     */
    public void createDrawer() {
        drawerLayout = activity.findViewById(R.id.drawer_layout);
        navigationView = activity.findViewById(R.id.nav_view);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        navigationView.inflateHeaderView(R.layout.header);


        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(this.activityId);

    }

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != this.activityId) {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Navigation.goToHome(navigationView);
                    finish();
                    break;
                case R.id.nav_edit_profile:
                    Navigation.goToUserProfileActivity(navigationView);
                    finish();
                    break;
                case R.id.nav_add_activity:
                    Navigation.goToActivityUpload(navigationView);
                    finish();
                    break;
                case R.id.nav_logout:
                    logoutIfUserNonNull(
                            AuthenticationInstanceProvider.getAuthenticationInstance(),
                            this.activity);
                    finish();
                    break;
                case R.id.nav_workouts:
                    Navigation.goToWorkoutActivity(this.navigationView);
                    finish();
                    break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

