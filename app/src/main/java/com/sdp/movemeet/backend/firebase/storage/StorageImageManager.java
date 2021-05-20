package com.sdp.movemeet.backend.firebase.storage;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.models.Image;
import com.squareup.picasso.Picasso;

public class StorageImageManager extends StorageManager<Image> {

    private StorageReference imageRef;

    public StorageImageManager() {
        this.imageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public StorageTask add(Image image, String path) {
        return imageRef.child(path).putFile(image.getImageUri());
    }


    @Override
    public Task<Uri> get(String path) {
        return imageRef.getDownloadUrl();
    }

}