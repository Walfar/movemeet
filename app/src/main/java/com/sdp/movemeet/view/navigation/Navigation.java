package com.sdp.movemeet.view.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.activity.ActivityListActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.profile.ProfileActivity;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.chat.ChatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Navigation extends AppCompatActivity {

    private Activity activity;
    private int activityId;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    /**
     * A Navigation object capable of creating a Navigation drawer for a
     * specified Android Activity
     * @param activity the Android activity to work with
     * @param activityId the R.id integer referring to this activity
     */
    public Navigation(Activity activity, int activityId) {
        this.activity = activity;
        this.activityId = activityId;
    }

    /**
     * Starts the default ActivityDescription activity
     * @param view the View in which to start the activity
     */
    public static void startActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ActivityDescriptionActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the ActivityUpload activity
     * @param view the View in which to start the activity
     */
    public static void goToActivityUpload(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UploadActivityActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the User Profile activity
     * @param view the View in which to start the activity
     */
    public static void goToUserProfileActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the MainActivity activity
     * @param view the View in which to start the activity
     */
    public static void goToHome(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the chat activity
     * @param view the View in which to start the activity
     */
    public static void goToChat(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("ACTIVITY_CHAT_ID", "general_chat");
        context.startActivity(intent);
    }

    /**
     * Starts ActivityList activity
     * @param view the View in which to start the activity
     */
    public static void goToListOfActivities(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ActivityListActivity.class);
        context.startActivity(intent);
    }


    /**
     * Fills in the navigation bar's text fields with the user's information
     * 0 = fullName
     * 1 = email
     * 2 = phone number
     * 3 = description (optional)
     * @param fields the fields to fill in, following the above order
     * @return the array of updated TextViews
     */
    public static TextView[] fillNavigationProfileFields(TextView[] fields) {
        FirebaseUser firebaseUser = AuthenticationInstanceProvider.getAuthenticationInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            UserSerializer serializer = new UserSerializer();
            FirestoreUserManager backendUserManager =
                    new FirestoreUserManager(BackendInstanceProvider.getFirestoreInstance(),
                            FirestoreUserManager.USERS_COLLECTION,
                            serializer);
            backendUserManager.getUserFromUid(userId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = serializer.deserialize(task.getResult().getData());

                        fields[0].setText(user.getFullName());
                        fields[1].setText(user.getEmail());
                        fields[2].setText(user.getPhoneNumber());
                        if (fields.length > 3 && user.getDescription() != null && !user.getDescription().isEmpty())
                            fields[3].setText(user.getDescription());
                    }
                }
            });
        }

        return fields;
    }

    /**
     * Initializes a Navigation drawer, filling in all fields and setting up associated functions,
     * inside the activity passed to this object's constructor.
     */
    public void createDrawer() {
        drawerLayout = activity.findViewById(R.id.drawer_layout);
        navigationView = activity.findViewById(R.id.nav_view);
        TextView textView = activity.findViewById(R.id.textView);
        Toolbar toolbar = activity.findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        View hView = navigationView.inflateHeaderView(R.layout.header);

        TextView fullName = hView.findViewById(R.id.text_view_profile_name);
        TextView phone = hView.findViewById(R.id.text_view_profile_phone);
        TextView email = hView.findViewById(R.id.text_view_profile_email);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(this.activityId);

        fillNavigationProfileFields(new TextView[] {fullName, email, phone});
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
                    FirebaseInteraction.logoutIfUserNonNull(
                            AuthenticationInstanceProvider.getAuthenticationInstance(),
                            this.activity);
                    finish();
                    break;
                case R.id.nav_start_activity:
                    Navigation.startActivity(this.navigationView);
                    finish();
                    break;
                case R.id.nav_chat:
                    Navigation.goToChat(this.navigationView);
                    finish();
                    break;
                case R.id.nav_list_activities:
                    Navigation.goToListOfActivities(this.navigationView);
                    finish();
                    break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
