package com.sdp.movemeet.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.R;
import com.sdp.movemeet.utility.LocationFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sdp.movemeet.map.MainMapFragment.ZOOM_VALUE;

public class GPSRecordingActivity extends FragmentActivity implements OnMapReadyCallback {

    // ----------------- GENERAL CONSTANTS --------------------------
    public static final Map<Boolean, String> BTN_TEXT_RES;

    static {
        BTN_TEXT_RES = new HashMap<>();
        BTN_TEXT_RES.put(true, "Stop");
        BTN_TEXT_RES.put(false, "Start");
    }

    public final static String MAP_NOT_READY_DESC = "Map isn't ready yet";
    public final static String MAP_READY_DESC = "Map is ready!";

    // ------------------ RECORDING VARIABLES -----------------------
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public boolean recording;
    private Button recButton;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public SupportMapFragment supportMapFragment;

    // ------------------ MAP AND DISPLAY OPTIONS --------------------
    public GoogleMap googleMap;

    private MarkerOptions markerOptions;
    public int POLYLINE_WIDTH = 25;
    public int POLYLINE_COLOR = Color.RED;
    private Polyline pathLine;
    private PolylineOptions pathLineOptions;

    private LocationFetcher locationFetcher;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            updatePath(locationResult.getLastLocation());
        }
    };

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public ArrayList<LatLng> path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s_recording);

        recording = false;
        recButton = findViewById(R.id.recordButton);

        pathLineOptions = new PolylineOptions().width(POLYLINE_WIDTH).color(POLYLINE_COLOR).geodesic(true);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap_recording);
        supportMapFragment.getView().setContentDescription(MAP_NOT_READY_DESC);
        supportMapFragment.getMapAsync(GPSRecordingActivity.this);

        path = new ArrayList<LatLng>();

        locationFetcher = new LocationFetcher(supportMapFragment, locationCallback);
        locationFetcher.startLocationUpdates();
    }


    public void toggleRecording(View view) {
        recording = !recording;
        recButton.setText(BTN_TEXT_RES.get(recording));
        if (recording) {
            path.clear();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        supportMapFragment.getView().setContentDescription(MAP_READY_DESC);
        pathLine = this.googleMap.addPolyline(pathLineOptions);
    }


    public void updatePath(Location location) {
        if (location == null) location = locationFetcher.getDefaultLocation();
        if (recording) {
            LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
            path.add(newPosition);

            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPosition, ZOOM_VALUE));
                drawPath();
            }
        }
    }

    private void drawPath() {
        googleMap.clear();
        LatLng position = path.get(path.size() - 1);
        markerOptions = new MarkerOptions().position(position).title("Current location");
        googleMap.addMarker(markerOptions);
        markerOptions = new MarkerOptions().position(path.get(0)).title("Starting location");
        googleMap.addMarker(markerOptions);
        pathLine = googleMap.addPolyline(pathLineOptions);
        pathLine.setPoints(path);
        pathLine.setVisible(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationFetcher.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationFetcher.stopLocationUpdates();
    }


}