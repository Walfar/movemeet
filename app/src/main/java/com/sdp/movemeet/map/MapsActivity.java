package com.sdp.movemeet.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sdp.movemeet.Backend.BackendActivityManager.ACTIVITIES_COLLECTION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private List<Activity> activities;
    private double perimeterRadius = 0.004;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private BackendActivityManager bam;
    private String userId;

    private static final String TAG = "Maps TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        activities = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        bam = new BackendActivityManager(db, ACTIVITIES_COLLECTION);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        //Temporary activity test
        Date date = new Date();
        date.setTime(1200);
        ArrayList<String> participants = new ArrayList<>();

        updateListActivities();

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMarkerClickListener(this);

        LatLng userLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("I am here !");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17.0f));
        Marker posMarker = googleMap.addMarker(markerOptions);
        posMarker.setTag("my position");

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
            Intent intent = new Intent(MapsActivity.this, ActivityDescriptionActivity.class);
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

    private void getNearbyMarkers(GoogleMap googleMap) {
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
                Log.d(TAG, "Coordinates not in perimeter: " + act.getActivityId());
            }
        }
    }

    private void updateListActivities() {
       Query q = bam.getActivitiesCollectionReference();

        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot docSnap: queryDocumentSnapshots.getDocuments()) {
                        //Log.d(TAG, "here");
                        Object activityIdobj = docSnap.get("activityId");
                        if (activityIdobj == null) Log.d(TAG, "activityId is null");
                        Object organizerIdobj = docSnap.get("organizerId");
                        if (organizerIdobj == null) Log.d(TAG, "organizerId is null");
                        Object titleobj = docSnap.get("title");
                        if (titleobj == null) Log.d(TAG, "title is null");
                        Object numberParticipantobj = docSnap.get("numberParticipant");
                        if (numberParticipantobj == null) Log.d(TAG, "numbPaetcipatns is null");
                        Object participantsIdobj = docSnap.get("participantsId");
                        if (participantsIdobj == null) Log.d(TAG, "partcipants is null");
                        Object longitudeobj =  docSnap.get("longitude");
                        if (longitudeobj == null) Log.d(TAG, "longitutde is null");
                        Object latitudeobj =  docSnap.get("latitude");
                        if (latitudeobj == null) Log.d(TAG, "latitude is null");
                        Object descriptionobj =  docSnap.get("description");
                        if (descriptionobj == null) Log.d(TAG, "description is null");
                        Object dateobj = docSnap.get("date");
                        if (dateobj == null) Log.d(TAG, "date is null");
                        Object durationobj = docSnap.get("duration");
                        if (durationobj == null) Log.d(TAG, "duration is null");
                        Object sportobj =  docSnap.get("sport");
                        if (sportobj == null) Log.d(TAG, "sport is null");
                        Object addressobj = docSnap.get("address");
                        if (addressobj == null) Log.d(TAG, "address is null");

                        if (activityIdobj != null && organizerIdobj != null && titleobj != null && numberParticipantobj != null && participantsIdobj != null && longitudeobj != null
                        && latitudeobj != null && descriptionobj != null && dateobj != null && durationobj != null && sportobj != null && addressobj != null) {
                            Log.d(TAG, "done");
                            String activityId = (String) activityIdobj;
                            String organizerId = (String) organizerIdobj;
                            String title = (String) titleobj;
                            int numberParticipant = (int) numberParticipantobj;
                            ArrayList<String> participantsId = (ArrayList<String>) participantsIdobj;
                            double longitude = (double) longitudeobj;
                            double latitude = (double) latitudeobj;
                            String description = (String) descriptionobj;
                            Date date = (Date) dateobj;
                            double duration = (double) durationobj;
                            Sport sport = (Sport) sportobj;
                            String address = (String) addressobj;
                            Activity act = new Activity(activityId, organizerId, title, numberParticipant, participantsId, longitude, latitude, description, date, duration, sport, address);
                            activities.add(act);

                        }

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "failure");
            }
        });
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
            currentLocation = location;
            Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            supportMapFragment.getMapAsync(MapsActivity.this);
        }
    }


}