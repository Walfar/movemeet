//package com.sdp.movemeet.backend.serialization;
//
//import com.sdp.movemeet.models.Image;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import android.net.Uri;
//import android.widget.ImageView;
//
//public class ImageSerializer implements BackendSerializer<Image> {
//
//    // The key used to access the imageView attribute of a serialized Image
//    public static final ImageView IMAGE_VIEW_KEY = null;
//    // The key used to access the imageUri attribute of a serialized Image
//    public static final Uri IMAGE_URI_KEY = null;
//    // The key used to access the imagePath attribute of a serialized Image
//    public static final String IMAGE_PATH_KEY = "imagePath";
//
//    public Image deserialize(Map<String, Object> data) {
//
//        Image image = new Image(
//
//                (ImageView) data.get(IMAGE_VIEW_KEY),
//
//                (Uri) data.get(IMAGE_URI_KEY),
//
//                (String) data.get(IMAGE_PATH_KEY)
//
//        );
//
//        return image;
//
//    }
//
//    public Map<String, Object> serialize(Image image) {
//
//        Map<String, Object> data = new HashMap<String, Object>();
//
//        data.put(IMAGE_VIEW_KEY, image.getImageView());
//
//        data.put(IMAGE_URI_KEY, image.getImageUri());
//
//        data.put(IMAGE_PATH_KEY, image.getDocumentPath())
//
//    }
//
//}