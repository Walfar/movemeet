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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.DistanceCalculator;
import com.sdp.movemeet.R;
import com.sdp.movemeet.UploadActivityActivity;
import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.utility.LocationFetcher;

import java.util.ArrayList;
import java.util.List;

import static com.sdp.movemeet.utility.LocationFetcher.REQUEST_CODE;

public class MainMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private Location currentLocation;

    private ArrayList<Activity> activities;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    private Marker newActivityMarker;

    private SupportMapFragment supportMapFragment;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final String TAG = "Maps TAG";

    public static MainMapFragment newInstance() {
        return new MainMapFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(supportMapFragment.getActivity());
        LocationFetcher.fetchLastLocation(supportMapFragment, fusedLocationProviderClient, this);

        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        activities = updater.getActivities();

        user = fAuth.getCurrentUser();

        return view;
    }

   /*
    @SuppressWarnings("deprecation")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    currentLocation = LocationFetcher.fetchLastLocation(supportMapFragment, fusedLocationProviderClient, this);
                }
                break;
        }
    } */


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        this.currentLocation = LocationFetcher.getLocation();

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMapClickListener(this::onMapClick);

        LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("I am here !");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12.0f));
        Marker posMarker = googleMap.addMarker(markerOptions);
        posMarker.setTag("my position");

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

        dc.setActivities((ArrayList<Activity>) activities);
        dc.calculateDistances();
        dc.sort();

        for (Activity act : dc.getTopActivities(activities.size())) {
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