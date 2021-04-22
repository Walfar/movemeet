package com.sdp.movemeet.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.sdp.movemeet.map.MainMapFragment.ZOOM_VALUE;

public abstract class LocationFetcher {

    public static Location currentLocation;
    private static final int LOCATION_REQUEST_INTERVAL = 10_000;
    private static final int LOCATION_REQUEST_SHORTEST = 5_000;

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

    public static void updateLocation(FusedLocationProviderClient fusedLocationProviderClient, SupportMapFragment supportMapFragment, OnMapReadyCallback callback) {
        Activity activity = supportMapFragment.getActivity();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            currentLocation = defaultLocation();
            supportMapFragment.getMapAsync(callback);
            return;
        }
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                currentLocation = locationResult.getLastLocation();
                supportMapFragment.getMapAsync(callback);
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(LOCATION_REQUEST_INTERVAL, LOCATION_REQUEST_SHORTEST, LocationRequest.PRIORITY_HIGH_ACCURACY),
                locationCallback,
                Looper.getMainLooper());
    }

    public static LocationRequest createLocationRequest(long intervalMillis, long fastestIntervalMillis, int priority) {
        LocationRequest locreq = LocationRequest.create();
        locreq.setInterval(intervalMillis);
        locreq.setFastestInterval(fastestIntervalMillis);
        locreq.setPriority(priority);

        return locreq;
    }

    public static Location defaultLocation() {
        Location location = new Location("default location");
        location.setLongitude(0);
        location.setLatitude(0);
        return location;
    }

}
