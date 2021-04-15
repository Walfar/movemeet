/*package com.sdp.movemeet.utility;


import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.movemeet.Activity.Activity;

import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ActivityCache extends AppCompatActivity {

    public void saveActivitiesInCache(ArrayList<Activity> activities) {
        ObjectOutput out;
        try {
            File outFile = new File(Environment.getExternalStorageDirectory(), "cached_activities");
            out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(activities);
            out.close();
        } catch (Exception e) {e.printStackTrace();}
        Log.d("Activity cache", "list of activities saved in cache ");
    }

    public ArrayList<Activity> loadActivitiesFromCache() {
        ObjectInput in;
        ArrayList<Activity> activities = null;
        try {
            in = new ObjectInputStream(new FileInputStream("cached_activities"));
            activities = (ArrayList<Activity>) in.readObject();
            in.close();
        } catch (Exception e) {e.printStackTrace();}
        Log.d("Cache activities", activities.get(0).getTitle());
        return activities;
    }


} */
