package com.sdp.movemeet.view.map;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.GPSPath;
import com.sdp.movemeet.utility.LocationFetcher;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.sdp.movemeet.view.map.MainMapFragment.ZOOM_VALUE;

public class GPSRecordingActivity extends FragmentActivity implements OnMapReadyCallback {

    // ----------------- GENERAL CONSTANTS --------------------------
    public static final Map<Boolean, String> BTN_TEXT_RES;

    static {
        BTN_TEXT_RES = new HashMap<>();
        BTN_TEXT_RES.put(true, "Stop");
        BTN_TEXT_RES.put(false, "Start");
    }

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static FirestoreActivityManager firestoreActivityManager = new FirestoreActivityManager(
            FirestoreActivityManager.ACTIVITIES_COLLECTION,
            new ActivitySerializer());

    public final static String MAP_NOT_READY_DESC = "Map isn't ready yet";
    public final static String MAP_READY_DESC = "Map is ready!";

    // ------------------ RECORDING VARIABLES -----------------------

    private Activity activity;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public boolean recording;
    private Button recButton;

    private long time;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public ArrayList<LatLng> path;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s_recording);

        Intent intent = getIntent();
        activity = (Activity) intent.getSerializableExtra(ActivityDescriptionActivity.RECORDING_EXTRA_NAME);

        if (AuthenticationInstanceProvider.getAuthenticationInstance().getCurrentUser() == null || activity == null) {
            intent = new Intent(GPSRecordingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        recording = false;
        recButton = findViewById(R.id.recordButton);

        pathLineOptions = new PolylineOptions().width(POLYLINE_WIDTH).color(POLYLINE_COLOR).geodesic(true);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap_recording);
        supportMapFragment.getView().setContentDescription(MAP_NOT_READY_DESC);

        path = new ArrayList<>();

        locationFetcher = new LocationFetcher(supportMapFragment, locationCallback);
        locationFetcher.startLocationUpdates();

        supportMapFragment.getMapAsync(GPSRecordingActivity.this);
    }


    /**
     * Toggles the recording of the GPS path
     * @param view the current View
     */
    public void toggleRecording(View view) {
        recording = !recording;
        recButton.setText(BTN_TEXT_RES.get(recording));
        if (recording) {
            time = System.currentTimeMillis();
            path.clear();
        } else {
            time = System.currentTimeMillis() - time;
        }
    }

    @Override
    public void onBackPressed() {
        if (!recording) {
            Intent intent = new Intent(GPSRecordingActivity.this, ActivityDescriptionActivity.class);

            String id = AuthenticationInstanceProvider.getAuthenticationInstance().getCurrentUser().getUid();
            activity.getParticipantRecordings().put(id, new GPSPath(path, time));
            GPSRecordingActivity recordingActivity = this;
            firestoreActivityManager.add(activity, null).addOnCompleteListener(task -> recordingActivity.finish());

            intent.putExtra(ActivityDescriptionActivity.DESCRIPTION_ACTIVITY_KEY, activity);
            startActivity(intent);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        supportMapFragment.getView().setContentDescription(MAP_READY_DESC);
        pathLine = this.googleMap.addPolyline(pathLineOptions);
    }


    private void updatePath(Location location) {
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