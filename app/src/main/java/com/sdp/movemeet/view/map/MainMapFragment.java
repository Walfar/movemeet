package com.sdp.movemeet.view.map;

import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivityUnregister;
import com.sdp.movemeet.utility.DistanceCalculator;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.utility.LocationFetcher;

import java.util.ArrayList;

import static com.sdp.movemeet.utility.ActivitiesUpdater.activities;
import static com.sdp.movemeet.utility.ActivitiesUpdater.getActivities;
import static com.sdp.movemeet.utility.ActivitiesUpdater.updateListActivities;
import static com.sdp.movemeet.utility.PermissionChecker.isLocationPermissionGranted;


/**
 * Fragment used in the main activity screen to represent the map, that the user can interact with
 */
public class MainMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    //current location of the user on the map
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public Location currentLocation;

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public FirebaseUser user;

    //marker representing the clicked position where the user wants to create a new activity
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public Marker newActivityMarker;
    //marker representing the position of the user on the map
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public Marker positionMarker;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public SupportMapFragment supportMapFragment;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public GoogleMap googleMap;

    private LocationFetcher locationFetcher;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public ArrayList<Marker> activitiesMarkers;

    private static final String TAG = "Maps TAG";

    //Zoom value to animate camera on user at launch of the map
    public static final float ZOOM_VALUE = 15.0f;

    //Boolean used to check if the callback is called for the first time. Useful, to avoid repeating certain actions (e.g zooming on user's location)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public boolean first_callback;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        //At creation, we haven't called the locationCallback yet
        first_callback = true;
        activitiesMarkers = new ArrayList<>();

        user = fAuth.getCurrentUser();

        //When the location is updated, we change the current location value
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                currentLocation = locationResult.getLastLocation();
                displayMarkerOnMapReadyAndZoomInFirstCallback();
            }
        };
        //Start fetching and updating the user's location in real time
        locationFetcher = new LocationFetcher(supportMapFragment, locationCallback);
        locationFetcher.startLocationUpdates();

        //Update the local list of activities from the database. On success, we update the map by dispalying the activities markers
        updateListActivities(o -> displayNearbyMarkers());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "map is ready");
        this.googleMap = googleMap;

        if (googleMap != null) {
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnInfoWindowClickListener(this);
            googleMap.setOnMapClickListener(this::onMapClick);
        }

        //In the case where the user didn't grant permission, we set a default location
        if (!isLocationPermissionGranted(getActivity())) currentLocation = locationFetcher.getDefaultLocation();

    }

    /**
     * Displays the user marker on the map, when it is ready. In case this is the first callback (i.e first update), we zoom
     * on the marker
     */
    public void displayMarkerOnMapReadyAndZoomInFirstCallback() {
        displayUserMarker();
        if (first_callback) {
            if (googleMap != null) googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), ZOOM_VALUE));
            first_callback = false;
        }
    }


    /**
     * Method used to display the markers of the activities on the map
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void displayUserMarker() {
        //Set marker for the user's position on map
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title("I am here !");

        //We avoid duplicating the position marker when updating the user's location
        if (positionMarker != null) positionMarker.remove();
        if (googleMap != null) {
            positionMarker = googleMap.addMarker(markerOptions);
            positionMarker.setTag("my position");
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        //If user accidently touches the marker instead of the infowindow, act like he clicked on the infowindow
        if (marker.getTag().equals("new activity position")) onInfoWindowClick(marker);
        else if (!marker.getTag().equals("my position")) {
            //If user clicks on an activity marker, we bring him to the description of the given activity
            Activity act = (Activity) marker.getTag();
            Intent intent;
            // Different activity if user unregistered
            if (user == null) {
                intent = new Intent(getActivity(), ActivityDescriptionActivityUnregister.class);
            } else {
                intent = new Intent(getActivity(), ActivityDescriptionActivity.class);
            }
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
            if (googleMap != null) {
                Marker newActivity = googleMap.addMarker(
                        new MarkerOptions().position(latLng).title("new activity").snippet("Click her to create new activity")
                );
                newActivityMarker = newActivity;
                newActivityMarker.setTag("new activity position");
                newActivity.showInfoWindow();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //To avoid starting the updates two times in a row, we make sure this isn't the first launch of the view
        locationFetcher.startLocationUpdates();
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
    public void displayNearbyMarkers() {
        Log.d(TAG, "displaying act markers");
        Log.d(TAG, "activities size is " + activities.size());
        if (currentLocation == null) currentLocation = locationFetcher.getDefaultLocation();

        DistanceCalculator dc = new DistanceCalculator(currentLocation.getLatitude(), currentLocation.getLongitude());

        dc.setActivities(getActivities());
        dc.calculateDistances();
        dc.sort();

        int idx = 1;
        //For the moment, we get all activities as the distance calculator is not fully functional yet
        for (Activity act : dc.getTopActivities(getActivities().size())) {
            //We display all activities on the corresponding location and with the icon associated to the sport
            LatLng actLatLng = new LatLng(act.getLatitude(), act.getLongitude());

            MarkerOptions markerOpt = new MarkerOptions().position(actLatLng).title(act.getTitle());
            if (googleMap != null) {
                Log.d(TAG, "idx is " + idx);
                Log.d(TAG, markerOpt.getTitle());
                idx++;
                markerOpt.icon(BitmapDescriptorFactory.fromResource(chooseIcon(act)));
                Log.d(TAG, markerOpt.getIcon().toString());
                Marker marker = googleMap.addMarker(markerOpt);
                marker.setTag(act);
                activitiesMarkers.add(marker);
            }
        }
    }


    /**
     * Chooses the icon corresponding to the sport of a given activity. Used to display the activity's icon on the map
     *
     * @param activity activity to display
     * @return the drawable corresponding to the icon
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
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


    @Override
    public void onInfoWindowClick(Marker marker) {
        //When we click on the info window of the marker for the activity we want to create, we remove the marker from the map and bring to the upload activity screen
        newActivityMarker.remove();
        newActivityMarker = null;
        Intent intent = new Intent(supportMapFragment.getActivity(), UploadActivityActivity.class);
        //use Bundle to pass latlng instance to intent
        Bundle arg = new Bundle();
        arg.putParcelable("position", marker.getPosition());
        intent.putExtra("bundle", arg);
        startActivity(intent);
    }
}