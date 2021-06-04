package com.sdp.movemeet.view.profile;

import android.content.DialogInterface;
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
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firebaseDB.FirebaseDBMessageManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.firebase.storage.StorageImageManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.MessageSerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.models.Message;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.home.LoginActivity;

import java.util.ArrayList;

import static com.sdp.movemeet.utility.PermissionChecker.isStorageReadPermissionGranted;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private static final int REQUEST_IMAGE = 1000;

    private ImageView profileImage;
    private EditText profileFullName, profileEmail, profilePhone, profileDescription;
    private ProgressBar progressBar;
    private ImageButton saveBtn;

    private String userId, fullNameString, emailString, phoneString, descriptionString, userImagePath,
            createdActivityPath, registeredActivityPath;

    private FirebaseAuth fAuth;
    private FirebaseUser firebaseUser;
    private BackendManager<User> userManager;
    private BackendManager<Activity> activityManager;
    private BackendManager<Message> chatManager;
    private FirebaseStorage fStorage;
    private BackendManager<Image> imageBackendManager;

    private ArrayList<String> createdActivitiesId;
    private ArrayList<String> registeredActivitiesId;
    private int deleteActivitiesCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        firebaseUser = fAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        } else {
            userId = firebaseUser.getUid();
        }

        Intent data = getIntent();
        fullNameString = data.getStringExtra(ProfileActivity.EXTRA_FULL_NAME);
        emailString = data.getStringExtra(ProfileActivity.EXTRA_EMAIL);
        phoneString = data.getStringExtra(ProfileActivity.EXTRA_PHONE);
        descriptionString = data.getStringExtra(ProfileActivity.EXTRA_DESCRIPTION);
        createdActivitiesId = data.getStringArrayListExtra(ProfileActivity.EXTRA_CREATED_ACTIVITIES);
        registeredActivitiesId = data.getStringArrayListExtra(ProfileActivity.EXTRA_REGISTERED_ACTIVITIES);
        assignViewsAndAdjustData();

        fStorage = BackendInstanceProvider.getStorageInstance();
        userImagePath = FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId
                + ImageHandler.PATH_SEPARATOR + ImageHandler.USER_IMAGE_NAME;
        Image image = new Image(null, profileImage);
        image.setDocumentPath(userImagePath);
        ImageHandler.loadImage(image, this);

        userManager = new FirestoreUserManager(FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
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


    public void changeProfilePicture(View view) {
        if (isStorageReadPermissionGranted(this)) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, REQUEST_IMAGE);
        }
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                Image image = new Image(imageUri, profileImage);
                image.setDocumentPath(userImagePath);
                ImageHandler.uploadImage(image, this);
            }
        }
    }


    public void saveUserData(View view) {
        if (profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty() || profilePhone.getText().toString().isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Description is optional, but one or more contact fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String email = profileEmail.getText().toString();

        if (firebaseUser != null) {
            firebaseUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
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


    public void accessFirestoreUsersCollectionForUpdate() {
        User user = new User(profileFullName.getText().toString(), profileEmail.getText().toString(), profilePhone.getText().toString(),
                profileDescription.getText().toString(), createdActivitiesId, registeredActivitiesId);
        userManager.add(user, FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId).addOnSuccessListener(new OnSuccessListener() {
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


    public void deleteUserAccountCall(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete account");
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteOrganizedActivities();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }


    // 1) Delete organized activities and related chats
    public void deleteOrganizedActivities() {
        Log.d(TAG, "Entering deleteOrganizedActivities");
        activityManager = new FirestoreActivityManager(FirestoreActivityManager.ACTIVITIES_COLLECTION, new ActivitySerializer());
        Log.d(TAG, "Created activityManager");
        if (createdActivitiesId != null) {
            if (createdActivitiesId.size() > 0) {
                Log.d(TAG, "Entering if");
                for (int i=0; i < createdActivitiesId.size(); i++) {
                    createdActivityPath = createdActivitiesId.get(i);
                    // 1.1) Delete organized activities document in Firebase Firestore
                    activityManager.delete(createdActivityPath).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            deleteActivitiesCounter += 1;
                            Log.d(TAG, "deleteUserAccount - 1.1) ✅ Deleted organized activity n°: " + deleteActivitiesCounter);
                            // 1.2) Delete chats related to organized activities
                            deleteOrganizedActivityChat(createdActivityPath);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "deleteUserAccount - 1) ❌ FAILED to delete organized activity n°: " + deleteActivitiesCounter);
                        }
                    });
                }
            }
        }
        unregisterUserFromActivities();
    }


    // 1.2) Delete chats related to organized activities
    public void deleteOrganizedActivityChat(String activityPath) {
        Log.d(TAG, "Entering deleteOrganizedActivityChat");
        chatManager = new FirebaseDBMessageManager(new MessageSerializer());
        String activityChatPath = activityPath.replace(FirestoreActivityManager.ACTIVITIES_COLLECTION, ChatActivity.CHATS_CHILD);
        chatManager.delete(activityChatPath);
        Log.d(TAG, "deleteUserAccount - 1.2) ✅ deleted chat of organized activity n°: " + deleteActivitiesCounter);
    }


    // 2) Remove user from activities he registered to (to free up his place for other participants)
    public void unregisterUserFromActivities() {
        Log.d(TAG, "Entering unregisterUserFromActivities");
        if (registeredActivitiesId != null) {
            if (registeredActivitiesId.size() > 0) {
                for (int i=0; i<registeredActivitiesId.size(); i++) {
                    registeredActivityPath = registeredActivitiesId.get(i);
                    activityManager.update(registeredActivityPath, ActivityDescriptionActivity.PARTICIPANT_ID_FIELD, userId, ActivityDescriptionActivity.UPDATE_FIELD_REMOVE);
                }
                Log.d(TAG, "✅ deleteUserAccount - 2) user successfully unregistered from all activities");
            }
        }
        deleteUserAccount(userId, firebaseUser);
    }


    // 3) Delete user information
    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public void deleteUserAccount(String userId, FirebaseUser firebaseUser) {
        // 3.1) Delete the profile picture of the user from Firebase Storage (in case it exists)
        Log.d(TAG, "deleteUserAccount - 3.1) Firebase Storage user profile picture deletion");
        String userImagePath = FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId
                + ImageHandler.PATH_SEPARATOR + ImageHandler.USER_IMAGE_NAME;
        imageBackendManager = new StorageImageManager();
        imageBackendManager.delete(userImagePath);
        // 3.2) Delete all the user data from Firebase Firestore
        deleteFirestoreData(userId, firebaseUser);
    }


    // 3.2) Delete all the user data from Firebase Firestore
    public void deleteFirestoreData(String userId, FirebaseUser firebaseUser) {
        Log.d(TAG, "deleteUserAccount - 3.2) Firebase Firestore user data deletion");
        BackendManager<User> userManager = new FirestoreUserManager(FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        userManager.delete(FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId);
        deleteUserFromFirebaseAuthentication(firebaseUser);
    }


    // 3.3) Delete the user from Firebase Authentication
    private void deleteUserFromFirebaseAuthentication(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "deleteUserAccount - 3.3) ✅ Firebase Authentication for current user successfully deleted!");
                    Toast.makeText(EditProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                    // Sending the user to HomeScreenActivity
                    Intent intent = new Intent(EditProfileActivity.this, HomeScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "deleteUserAccount - 3.3) ❌ Firebase Authentication for current user could not be deleted! Error message: " + task.getException().getMessage());
                    Toast.makeText(EditProfileActivity.this, "Account not properly deleted. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}