package com.sdp.movemeet.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.Sport;

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

    private static final String TAG = "Activities updater TAG";

    public static final int MAX_NUMBER_ACTIVITIES_TO_DISPLAY = 10;

    private static ActivitiesUpdater instance;

    public ActivitiesUpdater() {
        activities = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        bam = new BackendActivityManager(db, ACTIVITIES_COLLECTION);
    }

    public static ActivitiesUpdater getInstance() {
        if (instance == null) {
            instance = new ActivitiesUpdater();
        }
        return instance;
    }

    public ArrayList<Activity> getActivities() {
        updateListActivities();
        return activities;
    }

    public void updateListActivities() {
        Query q = bam.getActivitiesCollectionReference();


        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int idx = 0;
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot docSnap: queryDocumentSnapshots.getDocuments()) {
                        if (idx == MAX_NUMBER_ACTIVITIES_TO_DISPLAY) break;

                        Object activityIdobj = docSnap.get("activityId");
                        String activityId;
                        if (activityIdobj == null) activityId = "";
                        else activityId = (String) activityIdobj;

                        Object organizerIdobj = docSnap.get("organizerId");
                        String organizerId;
                        if (organizerIdobj == null) organizerId = "";
                        else organizerId = (String) organizerIdobj;

                        Object titleobj = docSnap.get("title");
                        String title;
                        if (titleobj == null) title = null;
                        else title = (String) titleobj;

                        Long numberParticipantobj = docSnap.getLong("numberParticipant");
                        int numberPartcipant;
                        if (numberParticipantobj == null) numberPartcipant = 0;
                        else numberPartcipant = numberParticipantobj.intValue();;

                        Object participantsIdobj = docSnap.get("participantsId");
                        ArrayList<String> partcipantsId;
                        if (participantsIdobj == null) partcipantsId = new ArrayList<>();
                        else partcipantsId = (ArrayList<String>) participantsIdobj;

                        Object longitudeobj =  docSnap.get("longitude");
                        double longitude;
                        if (longitudeobj == null) longitude = 0.0;
                        else longitude = (double) longitudeobj;

                        Object latitudeobj =  docSnap.get("latitude");
                        double latitude;
                        if (latitudeobj == null) latitude = 0.0;
                        else latitude = (double) latitudeobj;

                        Object descriptionobj =  docSnap.get("description");
                        String description;
                        if (descriptionobj == null) description = "";
                        else description = (String) descriptionobj;

                        Date dateobj = docSnap.getDate("date");
                        Date date;
                        if (dateobj == null) date = new Date();
                        else  date = dateobj;

                        Object durationobj = docSnap.get("duration");
                        double duration;
                        if (durationobj == null) duration = 0.0;
                        else  duration = (double) durationobj;

                        String sportobj =  docSnap.getString("sport");
                        Sport sport;
                        if (sportobj == null) sport = Running;
                        else sport = Sport.valueOf(sportobj);; //enum sotred in firebase ?");

                        Object addressobj = docSnap.get("address");
                        String address;
                        if (addressobj == null) address = "";
                        else address = (String) addressobj;


                        Activity act = new Activity(activityId, organizerId, title, (int) numberPartcipant, partcipantsId, longitude, latitude, description, date, duration, sport, address);
                        activities.add(act);
                        idx++;
                        Log.d(TAG, "activities size is " + activities.size());
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }
}
