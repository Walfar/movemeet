package com.sdp.movemeet.utility;

import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Activity;

import java.util.ArrayList;

/**
 * This utility class is used to fetch the activities from the database into local
 */
public abstract class ActivitiesUpdater {

    public static BackendSerializer<Activity> serializer =new ActivitySerializer();
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static ArrayList<Activity> activities = new ArrayList<>();
    private static FirestoreActivityManager firestoreActivityManager = new FirestoreActivityManager(FirestoreActivityManager.ACTIVITIES_COLLECTION, serializer);

    private static final String TAG = "Activities updater TAG";

    /**
     * Method that returns the local list of activities (empty by default). We first have to update the list before getting it, to
     * make sure that we always have all the activities from the database in the local list
     *
     * @return local list of activities
     */
    public static ArrayList<Activity> getActivities() {
        return activities;
    }

    /**
     * Updates the local list of activities, by retrieving only newly added activities in db collection. When complete, something
     * should happen (use onCompleteListener). If an activity is changed in the db, this change should also happen in local (use eventListener)
     *
     * @param onCompletelistener used as a callback when the activities are fetched from db
     * @param eventListener used as a callback when an activitiy is updated in the db
     */
    public static void updateListActivities(OnCompleteListener onCompletelistener, EventListener<DocumentSnapshot> eventListener) {
        if (onCompletelistener == null || eventListener == null) {
            Log.d(TAG, "listeners shouldn't be null");
            return;
        }
        Task<QuerySnapshot> allDocTask = firestoreActivityManager.getAll();
        //Count the number of elements in the collection
        allDocTask.addOnSuccessListener(queryDocumentSnapshots -> {
            // Calculate the number of newly added activities in the collection
            int size = queryDocumentSnapshots.getDocuments().size() - activities.size();
            //In case there is more activities in local than db (e.g if activities were deleted from db), than we clear the list and refetch
            if (size < 0) {
                clearLocalActivities();
                updateListActivities(onCompletelistener, eventListener);
                return;
            }
            else if (size == 0) allDocTask.addOnCompleteListener(onCompletelistener);
            //Either way, when updating the list, we update the map as well
            else addActivitiesOnSuccess(firestoreActivityManager.getRecentlyAddedActivities(size), eventListener).addOnCompleteListener(onCompletelistener);;
        });
    }

    /**
     * On success, add the activities from db to the local list of activities
     *
     * @param task task containing the requested snapshots, that would then be deserialized into activities
     */
    private static Task addActivitiesOnSuccess(Task<QuerySnapshot> task, EventListener<DocumentSnapshot> eventListener) {
        return task.addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot docSnap : queryDocumentSnapshots.getDocuments()) {
                Activity act = serializer.deserialize(docSnap.getData());
                activities.add(act);
                //for the given activity, we make sure that it will always be updated on map
                firestoreActivityManager.setActivitiesUpdateListener(act, eventListener);
            }
        }).addOnFailureListener(e -> Log.d(TAG, e.toString()));
    }

    /**
     * Clear the local list of activities
     */
    public static void clearLocalActivities() {
        activities.clear();
    }
}