package com.sdp.movemeet.backend.serialization;

import com.sdp.movemeet.models.Image;

import java.util.HashMap;
import java.util.Map;

import android.widget.ImageView;

public class ImageSerializer implements BackendSerializer<Image> {

    // The key used to access the imageView attribute of a serialized Image
    public static final ImageView IMAGE_VIEW_KEY = null;
    // The key used to access the imagePath attribute of a serialized Image
    public static final String IMAGE_PATH_KEY = "imagePath";
    // The key used to access the imageUri attribute of a serialized Image
    public static final String IMAGE_URI = "title";



}
