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

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
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

public class MiniMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LocationFetcher locationFetcher;

    public static MiniMapFragment newInstance() {
        return new MiniMapFragment();
    }

     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(supportMapFragment.getActivity());
        LocationFetcher.fetchLastLocation(fusedLocationProviderClient, supportMapFragment, this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(this::onMapClick);
        currentLocation = LocationFetcher.currentLocation;
        LatLng location = ((UploadActivityActivity) getActivity()).getAddressLocation();
        //Zoom on the location that the user set, or on his GPS position if none found
        Log.d("MiniMapFragment TAG", "user current location is " + currentLocation.toString());
        if (location != null) Log.d("MiniMapFragmentTAG", "not null location us " + location.toString());
        if (location == null) location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(location));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ((UploadActivityActivity) getActivity()).retrieveAddress(latLng);

    }
}
