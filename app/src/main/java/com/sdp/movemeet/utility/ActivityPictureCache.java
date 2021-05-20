package com.sdp.movemeet.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageTask;
import com.sdp.movemeet.backend.firebase.storage.StorageImageManager;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public abstract class ActivityPictureCache {

    public static String TAG = "Cache TAG";


    public static void saveToCache(ImageView imageView, String path) {
        Log.d(TAG, "saving image to cache");
        Bitmap bitmap = getBitmapFromView(imageView);
        String root = Environment.getExternalStorageDirectory().toString();
        File imagesDir = new File(root, "/saved_images");
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
        File activityDir = new File(imagesDir, "/" + path);
        if (!activityDir.exists()) {
            activityDir.mkdirs();
        }
        File file = new File(activityDir, "/activityImage.jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile(); // if file already exists will do nothing
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d(TAG, "succcessfully cached image at path " + file.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadFromCache(String path) {
        Log.d(TAG, "Loading image from cache");
        String imagePath = Environment.getExternalStorageDirectory().toString() + path;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeFile(imagePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


}