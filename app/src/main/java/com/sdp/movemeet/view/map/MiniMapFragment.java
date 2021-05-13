package com.sdp.movemeet.view.map;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.utility.LocationFetcher;

import static com.sdp.movemeet.view.map.MainMapFragment.ZOOM_VALUE;

public class MiniMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private SupportMapFragment supportMapFragment;
    private Location currentLocation;

    private LocationFetcher locationFetcher;

    private GoogleMap googleMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                currentLocation = locationResult.getLastLocation();
                if (googleMap != null) {
                    locationFetcher.stopLocationUpdates();
                    zoomOnAddressLocation();
                }
            }
        };
        locationFetcher = new LocationFetcher(supportMapFragment, locationCallback);
        locationFetcher.startLocationUpdates();

        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        googleMap.setOnMapClickListener(this::onMapClick);

        if (!locationFetcher.isPermissionGranted()) {
            currentLocation = locationFetcher.getDefaultLocation();
            zoomOnAddressLocation();
        }
    }

    private void zoomOnAddressLocation() {
        LatLng location = ((UploadActivityActivity) getActivity()).getAddressLocation();
        if (location == null) location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_VALUE));
    }

    @Override
    public void onResume() {
        super.onResume();
        locationFetcher.startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationFetcher.stopLocationUpdates();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        ((UploadActivityActivity) getActivity()).retrieveAddress(latLng);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_VALUE));
    }
}
