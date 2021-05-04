package com.sdp.movemeet.utility;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.view.map.MainMapFragment;

import java.util.ArrayList;
import java.util.Map;

import static com.sdp.movemeet.backend.BackendActivityManager.ACTIVITIES_COLLECTION;

public class ActivitiesUpdater {

    private FirebaseFirestore db;
    private FirestoreActivityManager manager;
    private BackendSerializer<Activity> serializer;

    private ArrayList<Activity> activities;

    private final String TAG = "Activities updater TAG";

    private static ActivitiesUpdater instance;

    private ActivitiesUpdater() {
        activities = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        serializer = new ActivitySerializer();
        manager = new FirestoreActivityManager(db, ACTIVITIES_COLLECTION, serializer);
    }

    /**
     * Gets current instance of updater, or creates a new one if none exists
     * @return instance of ActivitiesUpdater
     */
    public static ActivitiesUpdater getInstance() {
        if (instance == null) {
            instance = new ActivitiesUpdater();
        }
        return instance;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    /**
     * Updates the local list of activities, by retrieving only newly added activities in db collection
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Task<QuerySnapshot> updateListActivities(OnSuccessListener listener) {
        //Count the number of elements in the collection
        return manager.getAll().addOnSuccessListener(queryDocumentSnapshots -> {
            // Calculate the number of newly added activities in the collection
            int size = queryDocumentSnapshots.getDocuments().size() - activities.size();
            //If somehow the list contains deprecated activites (activities that are not in the DB), we clear the list and refetch all activities
            if (size < 0) {
                activities.clear();
                //fetchListActivities();
                addActivitiesOnSuccess(manager.getAll());
                return;
            }
            //add to the list only those newly added activities
            Log.d(TAG, "activities size is " + activities.size());
            Log.d(TAG, "diff number of activities is " + size);
            if (size > 0) addActivitiesOnSuccess(manager.getRecentlyAddedActivities(size)).addOnSuccessListener(listener);
        });


    }

   /* private Activity convertDocSnapToActivity(DocumentSnapshot docSnap) {

        String activityId = convertObjToString(docSnap.get("activityId"));
        String organizerId = convertObjToString(docSnap.get("organizerId"));
        String title = convertObjToString(docSnap.get("title"));
        int numberParticipant = convertLongToInt(docSnap.getLong("numberParticipant"));
        double longitude = convertObjToDouble(docSnap.get("longitude"));
        double latitude = convertObjToDouble(docSnap.get("latitude"));
        String description = convertObjToString(docSnap.get("description"));
        String documentPath = convertObjToString(docSnap.get("documentPath"));
        Date date = getDateObj(docSnap.getDate("date"));
        double duration = convertObjToDouble(docSnap.get("duration"));
        String address = convertObjToString(docSnap.get("address"));
        Date createdAt = getDateObj(docSnap.getDate("createdAt"));

        Object participantsIdobj = docSnap.get("participantId");
        ArrayList<String> partcipantsId;
        if (participantsIdobj == null) partcipantsId = new ArrayList<>();
        else partcipantsId = (ArrayList<String>) participantsIdobj;

        String sportobj = docSnap.getString("sport");
        Sport sport;
        if (sportobj == null) sport = Running;
        else sport = Sport.valueOf(sportobj);

        Activity act = new Activity(activityId, organizerId, title, numberParticipant, partcipantsId, longitude, latitude, description, documentPath, date, duration, sport, address, createdAt);
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
    } */

    /**
     * On success, add the activities from db to the local list of activities, and display activity markers on map
     * @param task task containing the requested snapshots, that would then be deserialized into activities
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private Task<QuerySnapshot> addActivitiesOnSuccess(Task<QuerySnapshot> task) {
        return task.addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot docSnap : queryDocumentSnapshots.getDocuments()) {
                activities.add(serializer.deserialize((Map) docSnap));
                Log.d(TAG, "activities size is " + activities.size());
            }
        }).addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }

    /**
     * Clear the local list of activities
     */
    public void clearLocalActivities() {
        activities.clear();
    }
}
