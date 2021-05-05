package com.sdp.movemeet.backend.firebase.storage;

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
import com.sdp.movemeet.models.Image;

import java.io.File;
import java.io.FileOutputStream;

public class CachedImageStorageManager extends ImageStorageManager {

    private Activity activity;

    private String TAG = "Cache TAG";

    public CachedImageStorageManager(Activity activity) {
        this.activity = activity;
    }

    @Override
    public StorageTask add(Image image, String path) {
        //Add to local cache
        if (isStorageWritePermissionGranted()) {
            Log.d(TAG, "saving image to cache");
            Bitmap bitmap = getBitmapFromView(image.getImageView());
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
        //Add to storage
        return super.add(image, path);
    }

    @Override
    public Task<Uri> get(String path) {
        Log.d(TAG, "Loading image from cache");
        if (isStorageReadPermissionGranted()) {
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/saved_images/" + path + "/activityImage.jpg";
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                if (bitmap == null) {
                    Log.d(TAG, "image could not be decoded");
                    return null;
                }
                //imageView.setImageBitmap(bitmap);
                Log.d(TAG, "image has been set");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.get(path);
    }

    public static Bitmap getBitmapFromView(View view) {
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


    private boolean isStorageWritePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Write permission is granted");
                return true;
            } else {
                Log.v(TAG, "Write permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Write permission is granted");
            return true;
        }
    }

    private boolean isStorageReadPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Read permission is granted");
                return true;
            } else {
                Log.v(TAG, "Read permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Read permission is granted");
            return true;
        }
    }
}