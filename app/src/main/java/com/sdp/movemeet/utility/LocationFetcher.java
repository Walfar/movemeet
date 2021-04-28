package com.sdp.movemeet.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Utility class used to fetch and update the user's location
 */
public class LocationFetcher {

    public static int REQUEST_CODE = 101;

    private Location defaultLocation;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public Location currentLocation;

    //Values for location requests intervals
    private static final int LOCATION_REQUEST_INTERVAL = 10_000;
    private static final int LOCATION_REQUEST_SHORTEST = 5_000;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public FusedLocationProviderClient fusedLocationProviderClient;

    private SupportMapFragment supportMapFragment;
    private LocationCallback locationCallback;

    //Boolean that indicates if the location is being periodically updated
    private boolean updatingLocation;

    /**
     * Constructor for the Location fetcher, using a default LocationCallback, and synchronizing the map
     * @param supportMapFragment supportMapFragment used to update the map and set the fusedLocationProviderClient used to request the updates on location
     * @param callback map to synchronize
     */
    public LocationFetcher(SupportMapFragment supportMapFragment, OnMapReadyCallback callback) {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(supportMapFragment.getActivity());
        this.supportMapFragment = supportMapFragment;
        this.updatingLocation = false;

        Location location = new Location("default location");
        location.setLongitude(0);
        location.setLatitude(0);
        this.defaultLocation = location;

        //By default, we use this callback: updating the current location, and synchronizing the map
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                //Whenever we get an update from the fusedLocationProviderClient, we update the current location
                currentLocation = locationResult.getLastLocation();
                supportMapFragment.getMapAsync(callback);
            }
        };
    }

    /**
     * Constructor for the Location fetcher, using a personalized LocationCallback
     * @param supportMapFragment supportMapFragment used to update the map set the fusedLocationProviderClient used to request the updates on location
     * @param locationCallback personalized LocationCallback
     */
    public LocationFetcher(SupportMapFragment supportMapFragment, LocationCallback locationCallback) {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(supportMapFragment.getActivity());
        this.supportMapFragment = supportMapFragment;
        this.locationCallback = locationCallback;
        this.updatingLocation = false;

        Location location = new Location("default location");
        location.setLongitude(0);
        location.setLatitude(0);
        this.defaultLocation = location;
    }

    /**
     * Starts periodically requesting location updates to the fusedLocationProviderClient, if the permission is granted
     */
    public void startLocationUpdates() {
        if (!updatingLocation) {
            Activity activity = supportMapFragment.getActivity();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //If permission is granted, request the fusedLocationProviderClient to update the location (and set the interval between each request)
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
                locationRequest.setFastestInterval(LOCATION_REQUEST_SHORTEST);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                        locationCallback,
                        Looper.getMainLooper());
            //If permissions are not granted, we can simply stop updating (as it would be useless calculations)
            } else stopLocationUpdates();
        }
        //Indicate that the location is being periodically updated
        updatingLocation = true;
    }

    /**
     * Stops periodically requesting location updates to the fusedLocationProviderClient
     */
    public void stopLocationUpdates() {
        if (updatingLocation) fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        updatingLocation = false;
    }

    /**
     * Gets the current location, fetched and updated from the fusedLocationProviderClient
     * @return the current location
     */
    public Location getCurrentLocation() {
        if (currentLocation == null) return defaultLocation;
        else return currentLocation;
    }

    /**
     * Gets the default location, i.e (0, 0)
     * @return the default location
     */
    public Location getDefaultLocation() {
        return defaultLocation;
    }

}
