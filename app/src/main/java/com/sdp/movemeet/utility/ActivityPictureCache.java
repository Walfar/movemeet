package com.sdp.movemeet.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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


    public static void saveToCache(Bitmap bitmap, String path) {
        String[] directories = path.split("/");
        String fileName = directories[directories.length-1];
        String imagePath = path.substring(0, path.length() - fileName.length());
        Log.d(TAG, "saving image to cache");
        Log.d(TAG, "filename is " + fileName);
        Log.d(TAG, "imagePath is " + imagePath);
        String root = Environment.getExternalStorageDirectory().toString();
        File imageDir = new File(root + "/" + imagePath);
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


    private static Bitmap getBitmapFromView(ImageView view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return returnedBitmap;
    }


}