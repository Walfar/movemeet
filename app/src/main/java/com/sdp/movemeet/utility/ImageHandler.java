package com.sdp.movemeet.utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.storage.StorageImageManager;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivityUnregister;
import com.sdp.movemeet.view.profile.EditProfileActivity;
import com.sdp.movemeet.view.profile.ProfileActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;

import static com.sdp.movemeet.utility.ActivityPictureCache.loadFromCache;
import static com.sdp.movemeet.utility.ActivityPictureCache.saveToCache;
import static com.sdp.movemeet.utility.PermissionChecker.isStorageReadPermissionGranted;
import static com.sdp.movemeet.utility.PermissionChecker.isStorageWritePermissionGranted;

public abstract class ImageHandler {

    private static final String TAG = "ImageHandler";

    private static BackendManager<Image> imageBackendManager;

    public static void loadImage(Image image, Activity activity) {
        Log.d(TAG, "loading image");
        ProgressBar progressBar = getProgressBar(activity);
        Bitmap bitmap = null;
        ImageView imageView = image.getImageView();
        String imagePath = image.getDocumentPath();

        if (isStorageReadPermissionGranted(activity)) bitmap = loadFromCache(imagePath);

        if (bitmap != null) {
            Log.d(TAG, "bitmap stored");
            imageView.setImageBitmap(bitmap);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            imageBackendManager = new StorageImageManager();
            Task<Uri> document = (Task<Uri>) imageBackendManager.get(imagePath);
            document.addOnSuccessListener(uri -> {
                Log.d(TAG, "Image successfully fetched from Firebase Storage!");
                image.setImageUri(uri);
                setImageBitMapAndSaveToCache(activity, image);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(exception -> {
                Log.d(TAG, "Image could not be fetched from Firebase Storage! Don't panic!" +
                        " It's probably because no images have been saved in Firebase Storage for" +
                        " this document yet!");
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private static void setImageBitMapAndSaveToCache(Activity activity, Image image) {
        Glide.with(activity)
                .asBitmap()
                .load(image.getImageUri())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        image.getImageView().setImageBitmap(resource);
                        Log.d(TAG, "storage write permissions " + isStorageWritePermissionGranted(activity));
                        if (isStorageWritePermissionGranted(activity)) saveToCache(resource, image.getDocumentPath());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }



    public static void uploadImage(Image image, Activity activity) {
        //TODO use instance
        imageBackendManager = new StorageImageManager();
        UploadTask uploadTask = (UploadTask) imageBackendManager.add(image, image.getDocumentPath());
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            setImageBitMapAndSaveToCache(activity, image);
            loadImage(image, activity);
        }).addOnFailureListener(e -> getProgressBar(activity).setVisibility(View.GONE));
    }

    private static ProgressBar getProgressBar(Activity activity) {
        ProgressBar progressBar = null;
        if (activity instanceof ActivityDescriptionActivity) progressBar = ((ActivityDescriptionActivity) activity).getProgressBar();
        else if (activity instanceof ActivityDescriptionActivityUnregister) progressBar = ((ActivityDescriptionActivityUnregister) activity).getProgressBar();
        else if (activity instanceof EditProfileActivity) progressBar = ((EditProfileActivity) activity).getProgressBar();
        else if (activity instanceof ProfileActivity) progressBar = ((ProfileActivity) activity).getProgressBar();
        return progressBar;
    }

}