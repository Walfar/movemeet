package com.sdp.movemeet.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.R;
import com.sdp.movemeet.UploadActivityActivity;
import com.sdp.movemeet.utility.LocationFetcher;

import static com.sdp.movemeet.map.MainMapFragment.ZOOM_VALUE;

public class MiniMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;

    private LocationFetcher locationFetcher;

    //Boolean to determine if the onMapReady method is called for the first time
    private boolean first_callback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(supportMapFragment.getActivity());

        first_callback = true;

        locationFetcher = new LocationFetcher(fusedLocationProviderClient, supportMapFragment, this);
        locationFetcher.startLocationUpdates();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (first_callback) {
            googleMap.setOnMapClickListener(this::onMapClick);
            currentLocation = locationFetcher.getCurrentLocation();
            first_callback = false;
        }
        //Zooms on location set by user, or its own position if none was found
        LatLng location = ((UploadActivityActivity) getActivity()).getAddressLocation();
        if (location == null) location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_VALUE));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!first_callback) locationFetcher.startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationFetcher.stopLocationUpdates();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ((UploadActivityActivity) getActivity()).retrieveAddress(latLng);
        supportMapFragment.getMapAsync(this);
    }
}
