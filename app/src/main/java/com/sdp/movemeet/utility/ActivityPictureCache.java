package com.sdp.movemeet.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public abstract class ActivityPictureCache {

    public static String TAG = "Cache TAG";

    /**
     * Saves a bitmap to the local cache, for the given path
     *
     * @param bitmap bitmap to cache
     * @param path   path where we save the bitmap
     */
    public static void saveToCache(Bitmap bitmap, String path) {
        String[] directories = path.split("/");
        String fileName = directories[directories.length - 1];
        String imagePath = path.substring(0, path.length() - fileName.length());
        Log.d(TAG, "saving image to cache");
        String root = Environment.getExternalStorageDirectory().toString();
        File imageDir = new File(root + "/movemeet/" + imagePath);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        File file = new File(imageDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile(); // if file already exists will do nothing
            FileOutputStream out = new FileOutputStream(file);
            //Bitmap bitmap = getBitmapFromView(imageView);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.d(TAG, "succcessfully cached image at path " + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a bitmap from the local cache, at the given path, or null if no such bitmap exists
     *
     * @param path path in which the bitmap is stored
     * @return the bitmap loaded from cache, or null if failed to load
     */
    public static Bitmap loadFromCache(String path) {
        Log.d(TAG, "Loading image from cache");
        String imagePath = Environment.getExternalStorageDirectory().toString() + "/" + path;
        Log.d(TAG, "path is " + imagePath);
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeFile(imagePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}