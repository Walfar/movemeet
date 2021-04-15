package com.sdp.movemeet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.sdp.movemeet.map.GPSRecordingActivity;

import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.utility.LocationFetcher;


public class HomeScreenActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    /*private SharedPreferences cachePrefs;
    public static boolean cacheAllowed;

    private ActivityResultLauncher<String> requestCachePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    SharedPreferences.Editor editor = cachePrefs.edit();
                    editor.putBoolean("cache_allowed", true);
                    editor.apply();
                    cacheAllowed = true;
                }
            }); */


    private ActivityResultLauncher<String> requestLocationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (!isGranted) {
            LocationFetcher.currentLocation = LocationFetcher.defaultLocation();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

       /* cachePrefs = getSharedPreferences(
                "cache_allowed", Context.MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestCachePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        cacheAllowed = cachePrefs.getBoolean("cache_allowed", false); */



        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        updater.fetchListActivities();
    }

    public void signIn(View v) {
        //if (user != null) startActivity(new Intent(this, MainActivity.class));
        startActivity(new Intent(this, LoginActivity.class)); // redirecting the user to the "Login" activity
    }

    public void noAccount(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void RecordRun(View v) {
        startActivity(new Intent(this, GPSRecordingActivity.class)); // Redirect the user to the GPS recording activity
    }

}