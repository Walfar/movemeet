package com.sdp.movemeet.models;

import android.net.Uri;
import android.widget.ImageView;

public class Image implements FirebaseObject {
    private ImageView imageView;
    private String imagePath;
    private Uri imageUri;

    public Image(ImageView imageView, Uri imageUri, String imagePath) {
        this.imageView = imageView;
        this.imagePath = imagePath;
        this.imageUri = imageUri;
    }

    @Override
    public String getDocumentPath() {
        return imagePath;
    }

    @Override
    public String setDocumentPath(String path) {
        throw new UnsupportedOperationException();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}