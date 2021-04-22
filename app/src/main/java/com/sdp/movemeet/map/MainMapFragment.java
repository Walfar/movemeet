package com.sdp.movemeet.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;

import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.DistanceCalculator;
import com.sdp.movemeet.HomeScreenActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.UploadActivityActivity;
import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.utility.LocationFetcher;



public class MainMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private Location currentLocation;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    private Marker newActivityMarker;
    private Marker positionMarker;

    private SupportMapFragment supportMapFragment;

    private GoogleMap googleMap;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public FusedLocationProviderClient fusedLocationProviderClient;

    private ActivitiesUpdater updater;

    private static final String TAG = "Maps TAG";

    public static final float ZOOM_VALUE = 15.0f;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(supportMapFragment.getActivity());
        //LocationFetcher.fetchLastLocation(fusedLocationProviderClient, supportMapFragment, this);
        LocationFetcher.updateLocation(fusedLocationProviderClient, supportMapFragment, this);

        this.updater = ActivitiesUpdater.getInstance();
        updater.updateListActivities(this);

        user = fAuth.getCurrentUser();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        this.currentLocation = LocationFetcher.currentLocation;
        if (this.currentLocation == null) this.currentLocation = LocationFetcher.defaultLocation();
        Log.d(TAG, currentLocation.toString());

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapClickListener(this::onMapClick);

        if (positionMarker != null) positionMarker.remove();
        LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("I am here !");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, ZOOM_VALUE));
        positionMarker = googleMap.addMarker(markerOptions);
        positionMarker.setTag("my position");

        getNearbyMarkers(googleMap);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        //If user accidently touches the marker instead of the infowindow, act like he clicked on the infowindow
        if (marker.getTag().equals("new activity position")) onInfoWindowClick(marker);
        else if (!marker.getTag().equals("my position")) {
            Activity act = (Activity) marker.getTag();
            Intent intent;
            // Different activity if user unregistered
            //if (user == null) intent = new Intent(MapsActivity.this, ActivityDescriptionActivityUnregistered.class);
            //else
            intent = new Intent(supportMapFragment.getActivity(), ActivityDescriptionActivity.class);
            intent.putExtra("activity", act);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "map is clicked");
        if (newActivityMarker != null) newActivityMarker.remove();
        //Only if user is logged we allow the possibility of creating a new activity
        if (user != null) {
            Log.d(TAG, "user is logged");
            Marker newActivity = googleMap.addMarker(
                    new MarkerOptions().position(latLng).title("new activity").snippet("Click her to create new activity")
            );
            newActivityMarker = newActivity;
            newActivityMarker.setTag("new activity position");
            newActivity.showInfoWindow();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNearbyMarkers(GoogleMap googleMap) {
        DistanceCalculator dc = new DistanceCalculator(currentLocation.getLatitude(), currentLocation.getLongitude());

        dc.setActivities(updater.getActivities());
        dc.calculateDistances();
        dc.sort();

        for (Activity act : dc.getTopActivities(updater.getActivities().size())) {
            LatLng actLatLng = new LatLng(act.getLatitude(), act.getLongitude());

            MarkerOptions markerOpt = new MarkerOptions().position(actLatLng).title(act.getTitle());
            markerOpt.icon(BitmapDescriptorFactory.fromResource(chooseIcon(act)));
            Marker marker = googleMap.addMarker(markerOpt);
            marker.setTag(act);
        }
    }



    public int chooseIcon(Activity activity) {
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
            case Boxing:
                return R.drawable.icon_boxing;
            case Yoga:
                return R.drawable.icon_yoga;
            case Windsurfing:
                return R.drawable.icon_windsurfing;
            case Trekking:
                return R.drawable.icon_trekking;
            case Hockey:
                return R.drawable.icon_hockey;
            case Gym:
                return R.drawable.icon_gym;
            case Pingpong:
                return R.drawable.icon_pingpong;
            case Climbing:
                return R.drawable.icon_climbing;
            case Golf:
                return R.drawable.icon_golf;
            case VolleyBall:
                return R.drawable.icon_volleyball;
            case Rugby:
                return R.drawable.icon_rugby;
            case Dancing:
                return R.drawable.icon_dancing;
            case Tricking:
                return R.drawable.icon_tricking;
            case Parkour:
                return R.drawable.icon_parkour;
            default:
                return -1;
        }
    }

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return fusedLocationProviderClient;
    }

    public SupportMapFragment getSupportMapFragment() {
        return supportMapFragment;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        newActivityMarker.remove();
        Intent intent = new Intent(supportMapFragment.getActivity(), UploadActivityActivity.class);
        //use Bundle to pass latlng instance to intent
        Bundle arg = new Bundle();
        arg.putParcelable("position",marker.getPosition());
        intent.putExtra("bundle", arg);
        //remove marker
        startActivity(intent);
    }
}