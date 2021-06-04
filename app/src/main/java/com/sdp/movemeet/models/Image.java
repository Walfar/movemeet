package com.sdp.movemeet.models;

import android.net.Uri;
import android.widget.ImageView;

/**
 * This class represents a message
 */
public class Image implements FirebaseObject {
    private Uri imageUri;
    private ImageView imageImageView;
    private String documentPath;

    /**
     * Constructor for a new image
     *
     * @param imageUri URI pointing to the image file
     */
    public Image(Uri imageUri, ImageView imageImageView) {

        if (imageUri == null && imageImageView == null) {
            throw new IllegalArgumentException();
        }

        this.imageUri = imageUri;
        this.imageImageView = imageImageView;
    }

    /**
     * @return the URI of the image
     */
    public Uri getImageUri() {
        return imageUri;
    }

    /**
     * @return the ImageView that holds the image
     */
    public ImageView getImageView() {
        return imageImageView;
    }

    /**
     * @return the document path of the image in Firebase Storage
     */
    public String getDocumentPath() {
        return this.documentPath;
    }

    /**
     * @param imageUri set the URI of the image
     */
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * @param imageImageView set the URI of the image
     */
    public void setImageImageView(ImageView imageImageView) {
        this.imageImageView = imageImageView;
    }

    /**
     * @param path change the document path of the image in Firebase Storage
     */
    public String setDocumentPath(String path) {
        if (documentPath == null) documentPath = path;
        return documentPath;
    }


}
