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
    };

    public final static String MAP_NOT_READY_DESC = "Map isn't ready yet";
    public final static String MAP_READY_DESC = "Map is ready!";

    private final int REQUEST_CODE = 101;

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

    // -------------- LOCATION REQUEST VALUES -------------------------
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;
    private final int LOCATION_REQUEST_INTERVAL = 10_000;
    private final int LOCATION_REQUEST_SHORTEST = 5_000;

    public boolean updatingLocation;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updatingLocation = false;
        recording = false;
        recButton = findViewById(R.id.recordButton);

        locationRequest = createLocationRequest(LOCATION_REQUEST_INTERVAL, LOCATION_REQUEST_SHORTEST, LocationRequest.PRIORITY_HIGH_ACCURACY);

        pathLineOptions = new PolylineOptions().width(POLYLINE_WIDTH).color(POLYLINE_COLOR).geodesic(true);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap_recording);
        supportMapFragment.getView().setContentDescription(MAP_NOT_READY_DESC);
        supportMapFragment.getMapAsync(GPSRecordingActivity.this);

        path = new ArrayList<LatLng>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        startLocationUpdates();
    }

    private LocationRequest createLocationRequest(long intervalMillis, long fastestIntervalMillis, int priority) {
        LocationRequest locreq = LocationRequest.create();
        locreq.setInterval(intervalMillis);
        locreq.setFastestInterval(fastestIntervalMillis);
        locreq.setPriority(priority);

        return locreq;
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
        if (location != null && recording) {
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
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void startLocationUpdates() {
        if (!updatingLocation) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }

            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }

        updatingLocation = true;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void stopLocationUpdates() {
        if (updatingLocation) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        updatingLocation = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }

}