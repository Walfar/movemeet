package com.sdp.movemeet.view.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.navigation.Navigation;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    public static final String EXTRA_MESSAGE_FULL_NAME = "fullName";
    public static final String EXTRA_MESSAGE_EMAIL = "email";
    public static final String EXTRA_MESSAGE_PHONE = "phone";
    public static final String EXTRA_MESSAGE_DESCRIPTION = "description";

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    ImageView profileImage;
    TextView fullName, email, phone, description;
    ProgressBar progressBar;

    String userId, userImagePath;
    User user;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    BackendManager<User> userManager;
    FirebaseStorage fStorage;
    StorageReference storageReference;
    StorageReference profileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.image_view_profile_image);
        progressBar = findViewById(R.id.progress_bar_profile);

        fullName = findViewById(R.id.text_view_activity_profile_name);
        email = findViewById(R.id.text_view_activity_profile_email);
        phone = findViewById(R.id.text_view_activity_profile_phone);
        description = findViewById(R.id.text_view_activity_profile_description);

        fStore = BackendInstanceProvider.getFirestoreInstance();
        userManager = new FirestoreUserManager(fStore, FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fStorage = BackendInstanceProvider.getStorageInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            displayRegisteredUserData();
            storageReference = fStorage.getReference();
            userImagePath = "users/" + userId + "/profile.jpg";
            Image image = new Image(null, profileImage);
            image.setDocumentPath(userImagePath);
            ImageHandler.loadImage(image, progressBar);
        }

        if(enableNav) new Navigation(this, R.id.nav_edit_profile).createDrawer();

        //The aim is to block any direct access to this page if the user is not logged
        //Smth must be wrong since it prevents automatic connection during certain tests
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

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
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "Get failed with: ", task.getException());
                }
            }
        });
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
                Log.d(TAG, "deleteUserAccount - 1) ✅ Firebase Storage user profile picture successfully deleted!");
                // 2) Deleting all the user data from Firebase Firestore
                deleteFirestoreDataAndAuthentication();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "deleteUserAccount - 1) ❌ Firebase Storage user profile picture could not be deleted! User account won't be deleted!");
            }
        });
    }


    public void deleteFirestoreDataAndAuthentication() {
        // Delete all user data from Firebase Firestore
        userManager.delete(FirestoreUserManager.USERS_COLLECTION + "/" + userId).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "deleteUserAccount - 2) ✅ Firebase Firestore user data successfully deleted!");
                // 3) Deleting the user from Firebase Authentication
                deleteUserFromFirebaseAuthentication();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "deleteUserAccount - 2) ❌ Firebase Firestore user document could not be fetched! User account won't be deleted!");
            }
        });
    }


    private void deleteUserFromFirebaseAuthentication() {
        // Delete user from Firebase Authentication
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            // TODO: Bug to fix --> check why this function sometimes doesn't delete the Firebase "user authentication" (i.e. the actual user account)
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "deleteUserAccount - 3) ✅ Firebase Authentication for current user successfully deleted!");
                                Toast.makeText(ProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                                // Sending the user to the login screen
                                startActivity(new Intent(ProfileActivity.this, HomeScreenActivity.class));
                                finish();
                            } else {
                                Log.d(TAG, "deleteUserAccount - 3) ❌ Firebase Authentication for current user could not be deleted!");
                            }
                        }
                    });
        }
    }

}