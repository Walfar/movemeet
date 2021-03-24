package com.sdp.movemeet.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sdp.movemeet.Sport.Soccer;
import static com.sdp.movemeet.Sport.Tennis;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private List<Activity> activities;
    private double perimeterRadius = 0.004;

    private static final String TAG = "Maps TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        activities = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(this::onSuccess);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMarkerClickListener(this);
        LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("I am here !");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17.0f));
        googleMap.addMarker(markerOptions);

        //Temporary activity test
        Date date = new Date();
        date.setTime(1200);
        ArrayList<String> participants = new ArrayList<>();
        Activity act1 = new Activity("0", "0", "soccer", 3, participants,
                currentLocation.getLongitude() - 0.002, currentLocation.getLatitude() + 0.002, "yes", date, 300, Soccer, "here");
        Activity act2 = new Activity("0", "0", "tennis", 5, participants,
                currentLocation.getLongitude() + 0.00001, currentLocation.getLatitude() + 0.00050, "yes", date, 300, Tennis, "here");
        activities.add(act1);
        activities.add(act2);

        for (Activity act: activities) {
            LatLng actLatLng = new LatLng(act.getLatitude(), act.getLongitude());
            MarkerOptions markerOpt = new MarkerOptions().position(actLatLng).title(act.getTitle());
            markerOpt.icon(BitmapDescriptorFactory.fromResource(chooseIcon(act)));
            Marker marker = googleMap.addMarker(markerOpt);
            marker.setTag(act);
        }


        //getNearbyMarkers(googleMap);
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
        Activity act = (Activity) marker.getTag();
        Intent intent = new Intent(MapsActivity.this, ActivityDescriptionActivity.class);
        intent.putExtra("activity", act);
        startActivity(intent);
        return true;
    }

    //How to update markers of perimeter when user moves ?
    /*private void getNearbyMarkers(GoogleMap googleMap) {
        LatLngBounds perimeter = new LatLngBounds(new LatLng(currentLocation.getLatitude() - perimeterRadius, currentLocation.getLongitude() - perimeterRadius),
                new LatLng(currentLocation.getLatitude() + perimeterRadius, currentLocation.getLongitude() + perimeterRadius));

        for (Activity act: activities) {
            LatLng actLatLng = new LatLng(act.getLatitude(), act.getLongitude());
            if (perimeter.contains(actLatLng)) {
                MarkerOptions markerOpt = new MarkerOptions().position(actLatLng).title(act.getTitle());
                markerOpt.icon(BitmapDescriptorFactory.fromResource(chooseIcon(act)));
                Marker marker = googleMap.addMarker(markerOpt);
                marker.setTag(act);
            } else {
                Toast.makeText(getApplicationContext(), "Coordinates not in perimeter", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

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
            default:
                return -1;
        }
    }
    private void onSuccess(Location location) {
        if (location != null) {
            currentLocation = location;
            Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            supportMapFragment.getMapAsync(MapsActivity.this);
        }
    }


}