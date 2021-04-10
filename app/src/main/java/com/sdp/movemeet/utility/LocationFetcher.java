package com.sdp.movemeet.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationFetcher {


    public static final int REQUEST_CODE = 101;
    private static Location currentLocation;

    public static void fetchLastLocation(SupportMapFragment supportMapFragment, FusedLocationProviderClient fusedLocationProviderClient, OnMapReadyCallback callback) {
        Activity activity = supportMapFragment.getActivity();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLocation = location;
                Toast.makeText(supportMapFragment.getActivity(), currentLocation.getLatitude() + ", " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                supportMapFragment.getMapAsync(callback);
            }
        });
    }

    public static Location getLocation() {
        return currentLocation;
    }


}
