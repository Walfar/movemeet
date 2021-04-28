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
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
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


/**
 * Fragment used in the main activity screen to represent the map, that the user can interact with
 */
public class MainMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    //current location of the user on the map
    private Location currentLocation;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    //marker representing the clicked position where the user wants to create a new activity
    private Marker newActivityMarker;
    //marker representing the position of the user on the map
    private Marker positionMarker;

    private SupportMapFragment supportMapFragment;

    private GoogleMap googleMap;

    private ActivitiesUpdater updater;
    private LocationFetcher locationFetcher;


    //Boolean to determine if the onMapReady method is called for the first time
    private boolean first_callback;

    private static final String TAG = "Maps TAG";

    //Zoom value to animate camera on user at launch of the map
    public static final float ZOOM_VALUE = 15.0f;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        first_callback = true;

        //Update the local list of activities from the database
        this.updater = ActivitiesUpdater.getInstance();
        updater.updateListActivities(this);

        user = fAuth.getCurrentUser();

        locationFetcher = new LocationFetcher(supportMapFragment, this);
        locationFetcher.startLocationUpdates();

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //This is necessary, because onMapReady won't be called if the permissions are not granted, and the activities will not display
            supportMapFragment.getMapAsync(this);
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //If no location has been found, use a default location
        currentLocation = locationFetcher.getCurrentLocation();

        //Set marker for the user's position on map
        LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("I am here !");

        //We avoid duplicating the position marker when updating the user's location
        if (positionMarker != null) positionMarker.remove();
        positionMarker = googleMap.addMarker(markerOptions);
        positionMarker.setTag("my position");

        //Those calculations are only needed when this method is called for the first time
        if (first_callback) {

            this.googleMap = googleMap;

            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnInfoWindowClickListener(this);
            googleMap.setOnMapClickListener(this::onMapClick);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, ZOOM_VALUE));

            getNearbyMarkers();

            first_callback= false;
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        //If user accidently touches the marker instead of the infowindow, act like he clicked on the infowindow
        if (marker.getTag().equals("new activity position")) onInfoWindowClick(marker);
        else if (!marker.getTag().equals("my position")) {
            //If user clicks on an activity marker, we bring him to the description of the given activity
            Activity act = (Activity) marker.getTag();
            Intent intent = new Intent(supportMapFragment.getActivity(), ActivityDescriptionActivity.class);
            intent.putExtra("activity", act);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "map is clicked");
        //To avoid duplication of markers, we remove the marker for creating activities on map whenever the user clicks on the map again
        if (newActivityMarker != null) newActivityMarker.remove();
        //Only if  the user is logged we allow the possibility of creating a new activity
        if (user != null) {
            Log.d(TAG, "user is logged");
            //On map click, the user determines the location of his activity by displaying a marker on the map
            Marker newActivity = googleMap.addMarker(
                    new MarkerOptions().position(latLng).title("new activity").snippet("Click her to create new activity")
            );
            newActivityMarker = newActivity;
            newActivityMarker.setTag("new activity position");
            newActivity.showInfoWindow();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //To avoid starting the updates two times in a row, we make sure this isn't the first launch of the view
        if (!first_callback) locationFetcher.startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationFetcher.stopLocationUpdates();
    }


    /**
     * Gets all the activity markers near the user, depending on the maximum distance set
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNearbyMarkers() {
        DistanceCalculator dc = new DistanceCalculator(currentLocation.getLatitude(), currentLocation.getLongitude());

        dc.setActivities(updater.getActivities());
        dc.calculateDistances();
        dc.sort();
        //For the moment, we get all activities as the distance calculator is not fully functional yet
        for (Activity act : dc.getTopActivities(updater.getActivities().size())) {
            Log.d(TAG, "showing activity");
            //We display all activities on the corresponding location and with the icon associated to the sport
            LatLng actLatLng = new LatLng(act.getLatitude(), act.getLongitude());

            MarkerOptions markerOpt = new MarkerOptions().position(actLatLng).title(act.getTitle());
            markerOpt.icon(BitmapDescriptorFactory.fromResource(chooseIcon(act)));
            Marker marker = googleMap.addMarker(markerOpt);
            marker.setTag(act);
        }
    }


    /**
     * Chooses the icon corresponding to the sport of a given activity. Used to display the activity's icon on the map
     *
     * @param activity activity to display
     * @return the drawable corresponding to the icon
     */
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

    /**
     * Getter for the supportMapFragment of the map, used in the Location Fetcher if needed
     *
     * @return supportMapFragment
     */
    public SupportMapFragment getSupportMapFragment() {
        return supportMapFragment;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        //When we click on the info window of the marker for the activity we want to create, we remove the marker from the map and bring to the upload activity screen
        newActivityMarker.remove();
        Intent intent = new Intent(supportMapFragment.getActivity(), UploadActivityActivity.class);
        //use Bundle to pass latlng instance to intent
        Bundle arg = new Bundle();
        arg.putParcelable("position", marker.getPosition());
        intent.putExtra("bundle", arg);
        //remove marker
        startActivity(intent);
    }
}