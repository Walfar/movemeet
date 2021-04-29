package com.sdp.movemeet.Backend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows various types of interaction with Firestore services (Firestore, Storage and
 * Authentication) to both save and fetch information about the user. It also allows to store and
 * retrieve images (user profile picture and activity header picture) by interacting with Firebase
 * Storage and avoid code duplicates through diverse Activity.
 */
public class FirebaseInteraction {

    private static final String TAG = "FirebaseInteraction";

    /**
     * Retrieve user data (full name, email, phone number and profile description) from Firebase
     * Firestore and directly update corresponding TextViews.
     *
     * @param fStore Firebase Firestore reference
     * @param userId The ID of the user
     * @param textViewArray A TextView array containing the TextViews referencing user data (full
     *                      name, email, phone number and profile description) to be updated
     * @param activity The activity from which this function is called
     */
    public static TextView[] retrieveDataFromFirebase(FirebaseFirestore fStore, String userId, TextView[] textViewArray, Activity activity) {
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot == null) {
                    //nothing
                } else if (textViewArray.length == 4) {
                    textViewArray[0].setText(documentSnapshot.getString("fullName"));
                    textViewArray[1].setText(documentSnapshot.getString("email"));
                    textViewArray[2].setText(documentSnapshot.getString("phone"));
                    textViewArray[3].setText(documentSnapshot.getString("description"));
                } else {
                    textViewArray[0].setText(documentSnapshot.getString("fullName"));
                    textViewArray[1].setText(documentSnapshot.getString("email"));
                    textViewArray[2].setText(documentSnapshot.getString("phone"));
                }
            }
        });

        return textViewArray;
    }

    /**
     * Create a HashMap containing the user data to be updated in Firebase Firestore. This function
     * is used in {@link com.sdp.movemeet.EditProfileActivity} to adjust the user data.
     *
     * @param profileFullName TextView corresponding to the full name of the user
     * @param profileEmail TextView corresponding to the email of the user
     * @param profilePhone TextView corresponding to the phone number of the user
     * @param profileDescription TextView corresponding to the profile description set by the user
     * @return A HashMap containing the updated user information
     */
    public static Map<String, Object> updateDataInFirebase(TextView profileFullName, TextView profileEmail, TextView profilePhone, TextView profileDescription) {
        Map<String, Object> edited = new HashMap<>();
        edited.put("fullName", profileFullName.getText().toString());
        edited.put("email", profileEmail.getText().toString());
        edited.put("phone", profilePhone.getText().toString());
        edited.put("description", profileDescription.getText().toString());

        return edited;
    }

    /**
     * Check if the user is still signed in. If it is not the case, the user is automatically
     * redirected to the {@link LoginActivity}.
     *
     * @param fAuth The Firebase Authentication reference that allows to access to the user object
     * @param activity The activity from which this function is called
     */
    public static void checkIfUserSignedIn(FirebaseAuth fAuth, Activity activity) {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user == null) {
            // If not signed in, then launch the LoginActivity
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Context mContext = activity.getApplicationContext();
            mContext.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * Sign out the user in case it is null (i.e. Firebase Authentication service can't retrieve the
     * user object anymore).
     *
     * @param fAuth The Firebase Authentication reference that allows to access to the user object
     * @param activity The activity from which this function is called
     */
    public static void logoutIfUserNonNull(FirebaseAuth fAuth, Activity activity) {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            // Logging out the user from Firebase
            FirebaseAuth.getInstance().signOut();
            // Launching the LoginActivity
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Context mContext = activity.getApplicationContext();
            mContext.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * Fetch an image (user profile picture or activity header picture) from Firebase Storage.
     *
     * @param imageRef Reference object to Firebase Storage
     * @param imageView ImageView holder for the image to fetch from Firebase Storage
     * @param progressBar A ProgressBar displayed as long as the image is not yet fetched
     */
    public static void getImageFromFirebase(StorageReference imageRef, ImageView imageView, ProgressBar progressBar) {
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "Image successfully fetched from Firebase Storage!");
                Picasso.get().load(uri).into(imageView);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Image could not be fetched from Firebase Storage!");
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Upload an image (user profile picture or activity header picture) to Firebase Storage.
     *
     * @param storageReference Reference object to Firebase Storage
     * @param imagePath Path leading to the location in Firebase Storage in which the image will be
     *                  uploaded
     * @param imageUri URI of the selected image
     * @param imageView ImageView holding the selected image
     * @param progressBar A ProgressBar displayed as long as the image is not yet placed in the
     *                    ImageView imageView
     */
    public static void uploadImageToFirebase(StorageReference storageReference, String imagePath, Uri imageUri, ImageView imageView, ProgressBar progressBar) {
        final StorageReference fileRef = storageReference.child(imagePath);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView); // Picasso helps us link the URI to the ImageView
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
