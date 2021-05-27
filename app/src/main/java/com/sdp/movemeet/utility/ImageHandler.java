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

import static com.sdp.movemeet.utility.ActivityPictureCache.loadFromCache;
import static com.sdp.movemeet.utility.ActivityPictureCache.saveToCache;
import static com.sdp.movemeet.utility.PermissionChecker.isStorageReadPermissionGranted;
import static com.sdp.movemeet.utility.PermissionChecker.isStorageWritePermissionGranted;

/**
 * This class allows to both load images from and upload images to the Firebase Storage service.
 * In case the image is not present in the local cache, it is fetched from Firebase Storage.
 * On the contrary, if the the image is already in the local cache, it is simply loaded from there.
 */
public abstract class ImageHandler {

    private static final String TAG = "ImageHandler";

    private static BackendManager<Image> imageBackendManager;


    /**
     * Fetch an image (user profile picture, activity header picture or chat image) from Firebase Storage.
     *
     * @param image Image object to be loaded from Firebase Storage or from the local cache.
     * @param activity Activity on which we want to fetch the image
     */
    public static void loadImage(Image image, Activity activity) {
        Log.d(TAG, "loading image");
        ProgressBar progressBar = getProgressBar(activity);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        Bitmap bitmap = null;
        ImageView imageView = image.getImageView();
        String imagePath = image.getDocumentPath();

        //First, we load from cache (will return null if bitmap is not in cache)
        if (isStorageReadPermissionGranted(activity)) bitmap = loadFromCache(imagePath);

        if (bitmap != null) {
            // bitmap was successfully loaded from cache
            Log.d(TAG, "bitmap stored");
            imageView.setImageBitmap(bitmap);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        } else {
            //Couldn't load from cache, try loading from Firebase
            imageBackendManager = new StorageImageManager();
            Task<Uri> document = (Task<Uri>) imageBackendManager.get(image.getDocumentPath());
            document.addOnSuccessListener(uri -> {
                Log.d(TAG, "Image successfully fetched from Firebase Storage!");
                image.setImageUri(uri);
                setImageBitMapAndSaveToCache(image, activity);
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

    /**
     * For a given image, sets the corresponding bitmap and saves it to cache (if permission)
     * @param image Image we want to store in the cache
     * @param activity Activity from which we want to save the image
     */
    private static void setImageBitMapAndSaveToCache(Image image, Activity activity) {
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


    /**
     * Upload an image (user profile picture, activity header picture or chat image) to Firebase Storage.
     *
     * @param image Image object to be uploaded to Firebase Storage or saved to the local cache.
     * @param activity Activity from which we want to upload the image
     */
    public static void uploadImage(Image image, Activity activity) {
        imageBackendManager = new StorageImageManager();
        UploadTask uploadTask = (UploadTask) imageBackendManager.add(image, image.getDocumentPath());
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            setImageBitMapAndSaveToCache(image, activity);
            loadImage(image, activity);
        }).addOnFailureListener(e -> getProgressBar(activity).setVisibility(View.GONE));
    }

    /**
     * Get the progress bar from an Activity
     * @param activity Activity from which we get the progress bar
     * @return the progress bar of the activity
     */
    private static ProgressBar getProgressBar(Activity activity) {
        ProgressBar progressBar = null;
        if (activity instanceof ActivityDescriptionActivity) progressBar = ((ActivityDescriptionActivity) activity).getProgressBar();
        else if (activity instanceof ActivityDescriptionActivityUnregister) progressBar = ((ActivityDescriptionActivityUnregister) activity).getProgressBar();
        else if (activity instanceof EditProfileActivity) progressBar = ((EditProfileActivity) activity).getProgressBar();
        else if (activity instanceof ProfileActivity) progressBar = ((ProfileActivity) activity).getProgressBar();
        return progressBar;
    }

}