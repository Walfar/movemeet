package com.sdp.movemeet.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Utility class used to fetch and update the user's location
 */
public class LocationFetcher {

    public static int REQUEST_CODE = 101;

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
     * Constructor for the Location fetcher, using a personalized LocationCallback
     * @param supportMapFragment supportMapFragment used to update the map set the fusedLocationProviderClient used to request the updates on location
     * @param locationCallback personalized LocationCallback
     */
    public LocationFetcher(SupportMapFragment supportMapFragment, LocationCallback locationCallback) {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(supportMapFragment.getActivity());
        this.supportMapFragment = supportMapFragment;
        this.locationCallback = locationCallback;
        this.updatingLocation = false;
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
     * Checks that permission for accessing GPS location is granted
     * @return true if permission is granted, false otherwise
     */
    public boolean isPermissionGranted() {
        return (ActivityCompat.checkSelfPermission(supportMapFragment.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(supportMapFragment.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Getter for the default location, i.e (0, 0)
     * @return the default location
     */
    public Location getDefaultLocation() {
        Location defaultLocation = new Location("default location");
        defaultLocation.setLongitude(0);
        defaultLocation.setLatitude(0);
        return defaultLocation;
    }

}
