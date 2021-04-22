package com.sdp.movemeet.Backend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class FirebaseInteraction {

    public static TextView[] retrieveDataFromFirebase(FirebaseFirestore fStore, String userId, TextView[] textViewArray, Activity activity) {
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot == null){
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

    public static Map<String,Object> updateDataInFirebase(TextView profileFullName, TextView profileEmail, TextView profilePhone, TextView profileDescription) {
        Map<String,Object> edited = new HashMap<>();
        edited.put("fullName", profileFullName.getText().toString());
        edited.put("email", profileEmail.getText().toString());
        edited.put("phone", profilePhone.getText().toString());
        edited.put("description", profileDescription.getText().toString());

        return edited;
    }

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

    public static void getImageFromFirebase(StorageReference imageRef, ImageView imageView, ProgressBar progressBar) {
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

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
