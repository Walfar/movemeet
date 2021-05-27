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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.R;

import androidx.annotation.VisibleForTesting;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private static final int REQUEST_IMAGE = 1000;


    private ImageView profileImage;
    private EditText profileFullName, profileEmail, profilePhone, profileDescription;
    private ProgressBar progressBar;
    private ImageButton saveBtn;

    private String userId, fullNameString, emailString, phoneString, descriptionString, userImagePath;

    private FirebaseAuth fAuth;
    private FirebaseUser firebaseUser;
    private BackendManager<User> userManager;
    private FirebaseStorage fStorage;
    private StorageReference storageReference;
    private StorageReference profileRef;

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
        fullNameString = data.getStringExtra(ProfileActivity.EXTRA_MESSAGE_FULL_NAME);
        emailString = data.getStringExtra(ProfileActivity.EXTRA_MESSAGE_EMAIL);
        phoneString = data.getStringExtra(ProfileActivity.EXTRA_MESSAGE_PHONE);
        descriptionString = data.getStringExtra(ProfileActivity.EXTRA_MESSAGE_DESCRIPTION);
        assignViewsAndAdjustData();

        fStorage = BackendInstanceProvider.getStorageInstance();
        storageReference = fStorage.getReference();
        userImagePath = FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId
                + ImageHandler.PATH_SEPARATOR + ImageHandler.USER_IMAGE_NAME;
        Image image = new Image(null, profileImage);
        image.setDocumentPath(userImagePath);
        ImageHandler.loadImage(image, progressBar);

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
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                Image image = new Image(imageUri, profileImage);
                image.setDocumentPath(userImagePath);
                ImageHandler.uploadImage(image, progressBar);
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


    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE) // making this method always public for testing and private otherwise
    public void accessFirestoreUsersCollectionForUpdate() {
        User user = new User(profileFullName.getText().toString(), profileEmail.getText().toString(), profilePhone.getText().toString(), profileDescription.getText().toString());
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




    //==========================
//    public void deleteUserAccount(View view) {
//
//        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "deleteUserAccount - 3) ✅ Firebase Authentication for current user successfully deleted!");
//                    Toast.makeText(EditProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
//                    // Sending the user to HomeScreenActivity
//                    Intent intent = new Intent(EditProfileActivity.this, HomeScreenActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                } else {
//                    Log.d(TAG, "deleteUserAccount - 3) ❌ Firebase Authentication for current user could not be deleted! Error message: " + task.getException().getMessage());
//                }
//            }
//        });
//    }
    //==========================






    //==========================================

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
                Log.d(TAG, "deleteUserAccount - 1) ❌ Firebase Storage user profile picture could not be fetched because it probably doesn't exist!");
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
        userManager.delete(FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId).addOnSuccessListener(new OnSuccessListener() {
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

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "deleteUserAccount - 3) ✅ Firebase Authentication for current user successfully deleted!");
                    Toast.makeText(EditProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                    // Sending the user to HomeScreenActivity
                    Intent intent = new Intent(EditProfileActivity.this, HomeScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "deleteUserAccount - 3) ❌ Firebase Authentication for current user could not be deleted! Error message: " + task.getException().getMessage());
                }
            }
        });

        //-----------------------------------
//        if (firebaseUser != null) {
//
//            AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
//            // Prompt the user to re-provide their sign-in credentials
//            firebaseUser.reauthenticate(credential)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if (task.isSuccessful()) {
//                                Log.e("TAG", "onComplete: authentication complete");
//
//
//                                //---------
//                                // TODO: Bug to fix --> check why this function sometimes doesn't delete the Firebase "user authentication" (i.e. the actual user account)
//                                firebaseUser.delete()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Log.d(TAG, "deleteUserAccount - 3) ✅ Firebase Authentication for current user successfully deleted!");
//                                                    Toast.makeText(EditProfileActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
//                                                    // Sending the user to the login screen
//                                                    startActivity(new Intent(EditProfileActivity.this, HomeScreenActivity.class));
//                                                    finish();
//                                                } else {
//                                                    Log.d(TAG, "deleteUserAccount - 3) ❌ Firebase Authentication for current user could not be deleted!");
//                                                }
//                                            }
//                                        });
//                                //---------
//
//
//                            } else {
//                                Toast.makeText(EditProfileActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//                    });
//        }
        //-----------------------------------


    }

    // TODO: to completely delete the user account, it would be great to delete all the activiies he created as an organiizer!
    //  For this, we have to have access to all the activities he organized with an additional field in Firebase Firestore for user
    //  That would be called "organizedActivitiesId" (in addition to the already existing "activitiesId" created by Roxane for the
    //  ListOfActivities)

    //==========================================

}