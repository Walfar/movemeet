package com.sdp.movemeet.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.DistanceCalculator;
import com.sdp.movemeet.R;
import com.sdp.movemeet.utility.ActivitiesUpdater;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private List<Activity> activities;
    private double perimeterRadius = 0.004;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    private static final String TAG = "Maps TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        activities = updater.getActivities();

        user = fAuth.getCurrentUser();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.setOnMarkerClickListener(this);

        LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("I am here !");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17.0f));
        Marker posMarker = googleMap.addMarker(markerOptions);
        posMarker.setTag("my position");

        /*for (Activity act: activities) {
            LatLng actLatLng = new LatLng(act.getLatitude(), act.getLongitude());
            MarkerOptions markerOpt = new MarkerOptions().position(actLatLng).title(act.getTitle());
            markerOpt.icon(BitmapDescriptorFactory.fromResource(chooseIcon(act)));
            Marker marker = googleMap.addMarker(markerOpt);
            marker.setTag(act);
        } */


        getNearbyMarkers(googleMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (!marker.getTag().equals("my position")) {
            Activity act = (Activity) marker.getTag();
            Intent intent;
            //if (user == null) intent = new Intent(MapsActivity.this, ActivityDescriptionActivityUnregistered.class);
            //else
            intent = new Intent(MapsActivity.this, ActivityDescriptionActivity.class);
            intent.putExtra("activity", act);
            startActivity(intent);
        }
        return true;
    }


    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this::onSuccess);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNearbyMarkers(GoogleMap googleMap) {
        DistanceCalculator dc = new DistanceCalculator(currentLocation.getLatitude(), currentLocation.getLongitude());

        dc.setActivities((ArrayList<Activity>) activities);
        dc.calculateDistances();
        dc.sort();

        for (Activity act : dc.getTopActivities(10)) {
            LatLng actLatLng = new LatLng(act.getLatitude(), act.getLongitude());

            MarkerOptions markerOpt = new MarkerOptions().position(actLatLng).title(act.getTitle());
            markerOpt.icon(BitmapDescriptorFactory.fromResource(chooseIcon(act)));
            Marker marker = googleMap.addMarker(markerOpt);
            marker.setTag(act);
        }
    }




    private int chooseIcon(Activity activity) {
        switch (activity.getSport()) {
            case Soccer:
                return R.drawable.icon_soccer;
            case Running:
                return R.drawable.icon_running;
            case Tennis:
                return R.drawable.icon_tennis;
            case Swimming:
                return R.drawable.icon_swim;
            case Badminton:
                return R.drawable.icon_badminton;
            default:
                return -1;
        }
    }
    private void onSuccess(Location location) {
        if (location != null) {
            Log.d(TAG, "location found !");
            currentLocation = location;
            Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            supportMapFragment.getMapAsync(MapsActivity.this);
        }
    }



}