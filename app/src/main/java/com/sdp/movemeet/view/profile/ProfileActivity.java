package com.sdp.movemeet.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    public static final String EXTRA_FULL_NAME = "fullName";
    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_PHONE = "phone";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_CREATED_ACTIVITIES = "createdActivities";
    public static final String EXTRA_REGISTERED_ACTIVITIES = "registeredActivities";

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    private TextView fullName, email, phone, description;
    private ArrayList<String> createdActivities;
    private ArrayList<String> registeredActivities;

    private String userId, userImagePath;
    private User user;

    private FirebaseAuth fAuth;
    private BackendManager<User> userManager;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        //The aim is to block any direct access to this page if the user is not logged
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            userId = fAuth.getCurrentUser().getUid();
        }

        if (enableNav) new Navigation(this, R.id.nav_edit_profile).createDrawer();

        ImageView profileImage = findViewById(R.id.image_view_profile_image);
        progressBar = findViewById(R.id.progress_bar_profile);

        fullName = findViewById(R.id.text_view_activity_profile_name);
        email = findViewById(R.id.text_view_activity_profile_email);
        phone = findViewById(R.id.text_view_activity_profile_phone);
        description = findViewById(R.id.text_view_activity_profile_description);

        userManager = new FirestoreUserManager(FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        displayRegisteredUserData();
        userImagePath = FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId
                + ImageHandler.PATH_SEPARATOR + ImageHandler.USER_IMAGE_NAME;
        Image image = new Image(null, profileImage);
        image.setDocumentPath(userImagePath);
        ImageHandler.loadImage(image, this);
    }


    public void displayRegisteredUserData() {
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + "/" + userId);
        document.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserSerializer userSerializer = new UserSerializer();
                        user = userSerializer.deserialize(document.getData());
                        fullName.setText(user.getFullName());
                        email.setText(user.getEmail());
                        phone.setText(user.getPhoneNumber());
                        description.setText(user.getDescription());
                        createdActivities = user.getCreatedActivitiesId();
                        registeredActivities = user.getRegisteredActivitiesId();
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "Get failed with: ", task.getException());
                }
            }
        });
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }


    public void goToEditProfileActivity(View view) {
        Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
        i.putExtra(EXTRA_FULL_NAME, fullName.getText().toString()); // key, value
        i.putExtra(EXTRA_EMAIL, email.getText().toString());
        i.putExtra(EXTRA_PHONE, phone.getText().toString());
        i.putExtra(EXTRA_DESCRIPTION, description.getText().toString());
        i.putExtra(EXTRA_CREATED_ACTIVITIES, createdActivities);
        i.putExtra(EXTRA_REGISTERED_ACTIVITIES, registeredActivities);
        startActivity(i);
    }

}