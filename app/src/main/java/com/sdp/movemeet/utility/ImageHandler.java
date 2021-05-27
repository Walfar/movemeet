package com.sdp.movemeet.utility;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.storage.StorageImageManager;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.squareup.picasso.Picasso;

/**
 * This class allows to both load images from and upload images to the Firebase Storage service.
 * In case the image is not present in the local cache, it is fetched from Firebase Storage.
 * On the contrary, if the the image is already in the local cache, it is simply loaded from there.
 */
public class ImageHandler {

    private static final String TAG = "FirebaseInteraction";
    public static final String USER_IMAGE_NAME = "profile.jpg";
    public static final String ACTIVITY_IMAGE_NAME = "activityImage.jpg";
    public static final String CHAT_IMAGE_NAME = "chatImage.jpg";
    public static final String PATH_SEPARATOR = "/";

    private static BackendManager<Image> imageBackendManager;

    /**
     * Fetch an image (user profile picture, activity header picture or chat image) from Firebase Storage.
     *
     * @param image Image object to be loaded from Firebase Storage or from the local cache.
     * @param progressBar A ProgressBar displayed as long as the image is not yet fetched.
     */
    public static void loadImage(Image image, ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
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

    /**
     * Upload an image (user profile picture, activity header picture or chat image) to Firebase Storage.
     *
     * @param image Image object to be uploaded to Firebase Storage or saved to the local cache.
     * @param progressBar A ProgressBar displayed as long as the image is not yet uploaded or saved.
     */
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

    /**
     * Convert the Firebase Storage URL of the image into a path in Firebase Storage
     *
     * @param imageUrl URL of the image in Firebase Storage
     * @return the converted image path
     */
    public static String convertURLtoPath(String imageUrl) {
        int startIdx = imageUrl.indexOf(ChatActivity.CHATS_CHILD);
        int endIdx = imageUrl.indexOf(ImageHandler.CHAT_IMAGE_NAME) + ImageHandler.CHAT_IMAGE_NAME.length();
        String imagePath = imageUrl.substring(startIdx, endIdx);
        imagePath = imagePath.replace("%2F", ImageHandler.PATH_SEPARATOR);
        return imagePath;
    }
}
