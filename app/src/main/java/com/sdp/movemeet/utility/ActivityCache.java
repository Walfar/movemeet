/*package com.sdp.movemeet.utility;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.sdp.movemeet.Activity.Activity;

import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ActivityCache {

    public void saveActivitiesInCache(ArrayList<Activity> activities) {
        isExternalStorageWritable();
        try {
            String str = "activity";
            File root = new File(Environment.getExternalStorageDirectory(), "cacheActivities");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "act");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(str);
            writer.flush();
            writer.close();

            FileReader reader = new FileReader(gpxfile);
            char[] string = new char[8];
            reader.read(string);
            Log.d("character read is ", string.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Activity cache", "there was a problem while writing");
        }
        Log.d("Activity cache", "list of activities saved in cache ");
    }

    public static ArrayList<Activity> loadActivitiesFromCache() {
        ObjectInput in;
        ArrayList<Activity> activities = null;
        try {
            Log.d("Activity cache", "defining infile");
            in = new ObjectInputStream(new FileInputStream("cached_activities"));
            Log.d("Activity cache", "reading activities");
            activities = (ArrayList<Activity>) in.readObject();
            Log.d("Activity cache", "defining outfile");
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Activity cache", "there was a problem");
        }
        Log.d("Cache activities", activities.get(0).getTitle());
        return activities;
    }


    public static boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d("Activity cache", "SD is writable !");
            return true;
        } else {
            Log.d("Activity cache", "SD is not writable");
            return false;
        }
    }


} */
