package com.sdp.movemeet.utility;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.storage.StorageImageManager;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.squareup.picasso.Picasso;

import static com.sdp.movemeet.utility.ActivityPictureCache.loadFromCache;
import static com.sdp.movemeet.utility.ActivityPictureCache.saveToCache;

public class ImageHandler {

    private static final String TAG = "FirebaseInteraction";

    private static BackendManager<Image> imageBackendManager;

    public static void loadImage(Image image, ActivityDescriptionActivity activityDescriptionActivity) {

        Bitmap bitmap = null;
        ImageView imageView = image.getImageView();
        String imagePath = image.getDocumentPath();

        if (activityDescriptionActivity.isStorageReadPermissionGranted()) bitmap = loadFromCache(imagePath);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            ProgressBar progressBar = activityDescriptionActivity.getProgressBar();
            progressBar.setVisibility(View.VISIBLE);
            imageBackendManager = new StorageImageManager();
            Task<Uri> document = (Task<Uri>) imageBackendManager.get(imagePath);
            document.addOnSuccessListener(uri -> {
                Log.d(TAG, "Image successfully fetched from Firebase Storage!");
                image.setImageUri(uri);
                Picasso.get().load(uri).into(imageView);
                if (activityDescriptionActivity.isStorageWritePermissionGranted()) saveToCache(imageView, imagePath);
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


    public static void uploadImage(Image image, ActivityDescriptionActivity activityDescriptionActivity) {
        imageBackendManager = new StorageImageManager();
        UploadTask uploadTask = (UploadTask) imageBackendManager.add(image, image.getDocumentPath());
        uploadTask.addOnSuccessListener(taskSnapshot -> loadImage(image, activityDescriptionActivity)).addOnFailureListener(e -> activityDescriptionActivity.getProgressBar().setVisibility(View.GONE));
    }

}
