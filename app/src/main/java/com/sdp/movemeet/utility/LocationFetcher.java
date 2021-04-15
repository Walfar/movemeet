package com.sdp.movemeet.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.map.MainMapFragment;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

public abstract class LocationFetcher {

    public static Location currentLocation;

    public static void fetchLastLocation(FusedLocationProviderClient fusedLocationProviderClient, SupportMapFragment supportMapFragment, OnMapReadyCallback callback) {
        Activity activity = supportMapFragment.getActivity();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) currentLocation = location;
                else currentLocation = defaultLocation();
                Toast.makeText(activity, currentLocation.getLatitude() + ", " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                supportMapFragment.getMapAsync(callback);
            }
        });
    }

    public static Location defaultLocation() {
        Location location = new Location("default location");
        location.setLongitude(0);
        location.setLatitude(0);
        return location;
    }

}
