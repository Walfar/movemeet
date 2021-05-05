package com.sdp.movemeet.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.view.navigation.Navigation;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    public static final String EXTRA_MESSAGE_FULL_NAME = "fullName";
    public static final String EXTRA_MESSAGE_EMAIL = "email";
    public static final String EXTRA_MESSAGE_PHONE = "phone";
    public static final String EXTRA_MESSAGE_DESCRIPTION = "description";

    ImageView profileImage;
    TextView fullName, email, phone, description;
    ProgressBar progressBar;

    String userId, userImagePath;
    User user;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    BackendManager<User> userManager;
    //FirestoreActivityManager FirestoreManager;
    StorageReference storageReference;
    StorageReference profileRef;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.image_view_profile_image);
        fullName = findViewById(R.id.text_view_activity_profile_name);
        email = findViewById(R.id.text_view_activity_profile_email);
        phone = findViewById(R.id.text_view_activity_profile_phone);
        description = findViewById(R.id.text_view_activity_profile_description);
        progressBar = findViewById(R.id.progress_bar_profile);

        fStore = FirebaseFirestore.getInstance();
        userManager = new FirestoreUserManager(fStore, FirestoreUserManager.USERS_COLLECTION, new UserSerializer());

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            displayRegisteredUserData();

            storageReference = FirebaseStorage.getInstance().getReference();
            loadRegisteredUserProfilePicture();
        }

        createDrawer();

        //handleRegisterUser();

        //The aim is to block any direct access to this page if the user is not logged
        //Smth must be wrong since it prevents automatic connection during certain tests
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }

    }

    public void createDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        View hView = navigationView.inflateHeaderView(R.layout.header);

        fullName = hView.findViewById(R.id.text_view_profile_name);
        phone = hView.findViewById(R.id.text_view_profile_phone);
        email = hView.findViewById(R.id.text_view_profile_email);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(R.id.nav_chat);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Navigation.goToHome(this.navigationView);
                finish();
                break;
            case R.id.nav_edit_profile:
                finish();
                break;
            case R.id.nav_add_activity:
                Navigation.goToActivityUpload(this.navigationView);
                finish();
                break;
            case R.id.nav_logout:
                FirebaseInteraction.logoutIfUserNonNull(fAuth, this);
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
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void handleRegisterUser() {
//        // Retrieve user data (full name, email and phone number) from Firebase Firestore
//        fAuth = FirebaseAuth.getInstance();
//        //fStore = FirebaseFirestore.getInstance();
//        if (fAuth.getCurrentUser() != null) {
//            userId = fAuth.getCurrentUser().getUid();
//            TextView[] textViewArray = {fullName, email, phone};
//            //FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ProfileActivity.this);
//            FirebaseInteraction.retrieveDataFromFirebase(FirestoreManager, userId, textViewArray, ProfileActivity.this);
//        }
//    }


    public void displayRegisteredUserData() {
        // TODO: to get values: userManager.get(userId)
        //fStore = FirebaseFirestore.getInstance();
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + "/" + userId);
        document.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        // TODO: retrieve data here
                        UserSerializer userSerializer = new UserSerializer();
                        user = userSerializer.deserialize(document.getData());
//                        fullNameString = (String) document.getData().get("fullName");
//                        organizerView.setText(fullNameString);
                        Log.i(TAG, "fullName: " + user.getFullName());
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "Get failed with: ", task.getException());
                }
            }
        });



        /*
        TextView[] textViewArray = {fullName, email, phone, description};
        textViewArray = FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ProfileActivity.this);
        fullName = textViewArray[0];
        email = textViewArray[1];
        phone = textViewArray[2];
        description = textViewArray[3];
        */
    }


    private void loadRegisteredUserProfilePicture() {
        userImagePath = "users/" + userId + "/profile.jpg";
        profileRef = storageReference.child(userImagePath);
        FirebaseInteraction.getImageFromFirebase(profileRef, profileImage, null);
    }


    public void goToEditProfileActivity(View view) {
        Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
        i.putExtra(EXTRA_MESSAGE_FULL_NAME, fullName.getText().toString()); // key, value
        i.putExtra(EXTRA_MESSAGE_EMAIL, email.getText().toString());
        i.putExtra(EXTRA_MESSAGE_PHONE, phone.getText().toString());
        i.putExtra(EXTRA_MESSAGE_DESCRIPTION, description.getText().toString());
        startActivity(i);
    }


    public void deleteUserAccount(View view) {
        // 1) Delete the profile picture of the user from Firebase Storage (in case it exists)
        profileRef = storageReference.child(userImagePath);
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                deleteProfilePicture();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "deleteUserAccount - 1) Firebase Storage user profile picture could not be fetched because it probably doesn't exist!");
                // 2) Deleting all the user data from Firebase Firestore
                deleteFirestoreDataAndAuthentication();
            }
        });

    }


    public void deleteProfilePicture() {
        profileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "deleteUserAccount - 1) Firebase Storage user profile picture successfully deleted!");
                // 2) Deleting all the user data from Firebase Firestore
                deleteFirestoreDataAndAuthentication();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "deleteUserAccount - 1) Firebase Storage user profile picture could not be deleted! User account won't be deleted!");
            }
        });
    }


    public void deleteFirestoreDataAndAuthentication() {
        // Delete all user data from Firebase Firestore
        fStore.collection("users").document(userId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        //FirestoreManager.get("users").getResult().getDocumentReference(userId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "deleteUserAccount - 2) Firebase Firestore user data successfully deleted!");
                // 3) Deleting the user from Firebase Authentication
                deleteUserFromFirebaseAuthentication();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "deleteUserAccount - 2) Firebase Firestore user document could not be fetched! User account won't be deleted!");
            }
        });
    }


    private void deleteUserFromFirebaseAuthentication() {
        // Delete user from Firebase Authentication
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "deleteUserAccount - 3) Firebase Authentication for current user successfully deleted!");
                                Toast.makeText(ProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                                // Sending the user to the login screen
                                startActivity(new Intent(ProfileActivity.this, HomeScreenActivity.class));
                                finish();
                            } else {
                                Log.d(TAG, "deleteUserAccount - 3) Firebase Authentication for current user could not be deleted!");
                            }
                        }
                    });
        }
    }

}