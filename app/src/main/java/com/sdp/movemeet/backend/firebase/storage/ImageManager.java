package com.sdp.movemeet.backend.firebase.storage;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.models.Image;
import com.squareup.picasso.Picasso;

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
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.profile.EditProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    private static final String TAG = "FirebaseInteraction";

    private static BackendManager<Image> imageBackendManager;

    public static void loadImage(Image image, ProgressBar progressBar) {
        imageBackendManager = new StorageImageManager();
        Task<Uri> document = (Task<Uri>) imageBackendManager.get(image.getDocumentPath());
        document.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "Image successfully fetched from Firebase Storage!");
                image.setImageUri(uri);
                Picasso.get().load(uri).into(image.getImageView());
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Image could not be fetched from Firebase Storage! Don't panic!" +
                        " It's probably because no images have been saved in Firebase Storage for" +
                        " this document yet!");
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    public static void uploadImage(Image image, ProgressBar progressBar) {
        imageBackendManager = new StorageImageManager();
        UploadTask uploadTask = (UploadTask) imageBackendManager.add(image, image.getDocumentPath());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                loadImage(image, progressBar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
