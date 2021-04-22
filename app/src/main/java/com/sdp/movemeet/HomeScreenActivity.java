package com.sdp.movemeet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.sdp.movemeet.map.GPSRecordingActivity;

import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.utility.LocationFetcher;


public class HomeScreenActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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

        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        // Always clear activities first, to prevent duplicates if multiple intents are created
        updater.clearLocalActivities();
        updater.fetchListActivities();
    }

    public void signIn(View v) {
        if (user != null) startActivity(new Intent(this, MainActivity.class));
        else startActivity(new Intent(this, LoginActivity.class)); // redirecting the user to the "Login" activity
    }

    public void noAccount(View v) {
        if (user != null) startActivity(new Intent(this, MainActivity.class));
        else startActivity(new Intent(this, MainUnregister.class));
    }

    public void RecordRun(View v) {
        startActivity(new Intent(this, GPSRecordingActivity.class)); // Redirect the user to the GPS recording activity
    }

}