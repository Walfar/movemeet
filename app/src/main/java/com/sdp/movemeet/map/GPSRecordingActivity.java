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

public class GPSRecordingActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final Map<Boolean, String> buttonTextRes;
    static {
        buttonTextRes = new HashMap<>();
        buttonTextRes.put(true, "Stop");
        buttonTextRes.put(false, "Start");
    };

    private final int REQUEST_CODE = 101;

    private boolean recording;
    private Button recButton;

    @VisibleForTesting
    protected SupportMapFragment supportMapFragment;

    @VisibleForTesting
    protected GoogleMap googleMap;

    private MarkerOptions markerOptions;
    private Polyline pathLine;
    private PolylineOptions pathLineOptions;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @VisibleForTesting
    protected ArrayList<LatLng> path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s_recording);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        recording = false;
        recButton = findViewById(R.id.recordButton);

        locationRequest = createLocationRequest(10000, 5000, LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                updatePath(locationResult.getLastLocation());
            }
        };

        pathLineOptions = new PolylineOptions().width(25).color(Color.RED).geodesic(true);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap_recording);
        supportMapFragment.getMapAsync(GPSRecordingActivity.this);

        path = new ArrayList<LatLng>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private LocationRequest createLocationRequest(long intervalMillis, long fastestIntervalMillis, int priority) {
        LocationRequest locreq = LocationRequest.create();
        locreq.setInterval(intervalMillis);
        locreq.setFastestInterval(fastestIntervalMillis);
        locreq.setPriority(priority);

        return locreq;
    }


    public void toggleRecording(View view) {
        Log.d("BUTTON", "Toggled!");
        recording = !recording;
        recButton.setText(buttonTextRes.get(recording));

        if (recording) {
            path.clear();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        pathLine = this.googleMap.addPolyline(pathLineOptions);

        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 20.0f));
                markerOptions = new MarkerOptions().position(position).title("Current location");
                googleMap.addMarker(markerOptions);
            }
        });

    }

    private void updatePath(Location location) {
        if (location != null && recording) {
            path.add(new LatLng(location.getLatitude(), location.getLongitude()));
            if (googleMap != null) {
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 20.0f));
                drawPath();
            }
        }
    }

    public ArrayList<LatLng> getPath() {
        return new ArrayList(this.path);
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

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                }
                break;
        }
    }

}