package com.sdp.movemeet.view.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.FirebaseInteraction;

import androidx.annotation.VisibleForTesting;

import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    ImageView profileImage;
    EditText profileFullName, profileEmail, profilePhone, profileDescription;
    ProgressBar progressBar;
    ImageButton saveBtn;

    String userId, fullNameString, emailString, phoneString, descriptionString, userImagePath;
    User user;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    BackendManager<User> userManager;
    FirebaseStorage fStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        fullNameString = data.getStringExtra("fullName");
        emailString = data.getStringExtra("email");
        phoneString = data.getStringExtra("phone");
        descriptionString = data.getStringExtra("description");

        assignViewsAndAdjustData();

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        fStore = BackendInstanceProvider.getFirestoreInstance();
        userManager = new FirestoreUserManager(fStore, FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        fStorage = BackendInstanceProvider.getStorageInstance();

        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            userImagePath = "users/" + userId + "/profile.jpg";
            storageReference = fStorage.getReference();
            loadRegisteredUserProfilePicture();
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }

    }

    private void assignViewsAndAdjustData() {
        profileImage = findViewById(R.id.image_view_edit_profile_image);
        profileFullName = findViewById(R.id.edit_text_edit_profile_full_name);
        profileEmail = findViewById(R.id.edit_text_edit_profile_email);
        profilePhone = findViewById(R.id.edit_text_edit_profile_phone);
        profileDescription = findViewById(R.id.edit_text_edit_profile_description);
        progressBar = findViewById(R.id.progress_bar_edit_profile);
        saveBtn = findViewById(R.id.button_edit_profile_save_profile_data);

        profileFullName.setText(fullNameString);
        profileEmail.setText(emailString);
        profilePhone.setText(phoneString);
        profileDescription.setText(descriptionString);
    }


    private void loadRegisteredUserProfilePicture() {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference profileRef = storageReference.child(userImagePath);
        FirebaseInteraction.getImageFromFirebase(profileRef, profileImage, progressBar);
    }


    public void changeProfilePicture(View view) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                progressBar.setVisibility(View.VISIBLE);
                FirebaseInteraction.uploadImageToFirebase(storageReference, userImagePath, imageUri, profileImage, progressBar);
            }
        }
    }


    public void saveUserData(View view) {
        if (profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() || profilePhone.getText().toString().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Description is optional, but one or more contact fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String email = profileEmail.getText().toString();

        FirebaseUser fUser = fAuth.getCurrentUser();

        if (fUser != null) {
            fUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //accessFirestoreUsersCollectionForUpdate();
                    accessFirestoreUsersCollectionForUpdate();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE) // making this method always public for testing and private otherwise
    public void accessFirestoreUsersCollectionForUpdate() {
        User user = new User(profileFullName.getText().toString(), profileEmail.getText().toString(), profilePhone.getText().toString(), profileDescription.getText().toString());
        userManager.add(user, FirestoreUserManager.USERS_COLLECTION + "/" + userId).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(EditProfileActivity.this, "Profile updated.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

}