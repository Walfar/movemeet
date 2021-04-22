package com.sdp.movemeet.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.map.MainMapFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.sdp.movemeet.Backend.BackendActivityManager.ACTIVITIES_COLLECTION;
import static com.sdp.movemeet.Sport.Running;

public class ActivitiesUpdater {

    private static BackendActivityManager bam;
    private FirebaseFirestore db;
    private ArrayList<Activity> activities;

    //private ActivityCache cache;
    private MainMapFragment mapFragment;

    private final String TAG = "Activities updater TAG";

    private static ActivitiesUpdater instance;

    private ActivitiesUpdater() {
        activities = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        bam = new BackendActivityManager(db, ACTIVITIES_COLLECTION);
        //this.cache = new ActivityCache();
    }

    public static ActivitiesUpdater getInstance() {
        if (instance == null) {
            instance = new ActivitiesUpdater();
        }
        return instance;
    }

    /* public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    } */

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    /**
     * This method is to be used when the user launches the home screen for the first time. He will fetch all activities
     * the collection
     */
    public void fetchListActivities() {
        addListeners(bam.getActivitiesCollectionReference(), false);
    }

    /**
     * This method is to be used when the user is accessing the map.
     */
    public void updateListActivities(MainMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        CollectionReference collection = bam.getActivitiesCollectionReference();
        //Count the number of elements in the collection
        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Calculate the number of newly added activities in the collection
                int size = queryDocumentSnapshots.getDocuments().size() - activities.size();
                //If somehow the list contains deprecated activites (activities that are not in the DB), we clear the list and refetch all activities
                if (size < 0) {
                    activities.clear();
                    fetchListActivities();
                    return;
                }
                //add to the list only those newly added activities
                Log.d(TAG, "activities size is " + activities.size());
                Log.d(TAG, "diff number of activities is " + size);
                if (size > 0) addListeners(collection.orderBy("createdAt", Query.Direction.DESCENDING).limit(size), true);
            }
        });
    }

    /*public void checkInternetAndFetchActivities() {
        if (internetIsConnected()) {
            fetchListActivities();
        } /* else {
            activities = cache.loadActivitiesFromCache();
        }
    }


    private boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    } */

    private Activity convertDocSnapToActivity(DocumentSnapshot docSnap) {

        String activityId = convertObjToString(docSnap.get("activityId"));
        String organizerId = convertObjToString(docSnap.get("organizerId"));
        String title = convertObjToString(docSnap.get("title"));
        int numberParticipant = convertLongToInt(docSnap.getLong("numberParticipant"));
        double longitude = convertObjToDouble(docSnap.get("longitude"));
        double latitude = convertObjToDouble(docSnap.get("latitude"));
        String description = convertObjToString(docSnap.get("description"));
        Date date = getDateObj(docSnap.getDate("date"));
        double duration = convertObjToDouble(docSnap.get("duration"));
        String address = convertObjToString(docSnap.get("address"));
        Date createdAt = getDateObj(docSnap.getDate("createdAt"));

        Object participantsIdobj = docSnap.get("participantsId");
        ArrayList<String> partcipantsId;
        if (participantsIdobj == null) partcipantsId = new ArrayList<>();
        else partcipantsId = (ArrayList<String>) participantsIdobj;

        String sportobj =  docSnap.getString("sport");
        Sport sport;
        if (sportobj == null) sport = Running;
        else sport = Sport.valueOf(sportobj);; //enum sotred in firebase ?");

        Activity act = new Activity(activityId, organizerId, title, numberParticipant, partcipantsId, longitude, latitude, description, date, duration, sport, address, createdAt);
        return act;
    }

    private String convertObjToString(Object obj) {
        if (obj instanceof String && obj != null) {
            return (String) obj;
        } else return null;
    }

    private int convertLongToInt(Long obj) {
        if (obj == null) return 0;
        else return obj.intValue();
    }

    private double convertObjToDouble(Object obj) {
        if (obj == null) return 0;
        else return (double) obj;
    }

    private Date getDateObj(Date date) {
        if (date == null) return new Date();
        else return date;
    }

    private void addListeners(Query q, boolean updateMap) {
        //Log.d(TAG, "cache allowed is" + cacheAllowed);
        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot docSnap: queryDocumentSnapshots.getDocuments()) {
                    activities.add(convertDocSnapToActivity(docSnap));
                    Log.d(TAG, "activities size is " + activities.size());
                }
                //if (cacheAllowed) cache.saveActivitiesInCache(activities);
                if (updateMap) ((SupportMapFragment) mapFragment.getChildFragmentManager().findFragmentById(R.id.google_map)).getMapAsync(mapFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }

    public void clearLocalActivities() {
        activities.clear();
    }
}
