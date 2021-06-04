package com.sdp.movemeet.utility;

import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Activity;

import java.util.ArrayList;

public abstract class ActivitiesUpdater {

    private static final BackendSerializer<Activity> serializer =new ActivitySerializer();
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static ArrayList<Activity> activities = new ArrayList<>();
    private static final FirestoreActivityManager firestoreActivityManager = new FirestoreActivityManager(FirestoreActivityManager.ACTIVITIES_COLLECTION, serializer);

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
     * Updates the local list of activities, by retrieving only newly added activities in db collection
     *
     * @param listener used as a callback when updating the list, e.g to update the map markers
     */
    public static void updateListActivities(OnCompleteListener listener) {
        if (listener == null) {
            Log.d(TAG, "listener is null");
            return;
        }
        Task<QuerySnapshot> allDocTask = firestoreActivityManager.getAll();
        //Count the number of elements in the collection
        allDocTask.addOnSuccessListener(queryDocumentSnapshots -> {
            // Calculate the number of newly added activities in the collection
            int size = queryDocumentSnapshots.getDocuments().size() - activities.size();

            Log.d(TAG, "diff number of activities is " + size);
            //In case there is more activities in local than db (e.g if activities were deleted from db), than we clear the list and refetch
            //TODO: not an optimized way to do, might be interesting to rethink it
            if (size < 0) {
                clearLocalActivities();
                updateListActivities(listener);
                return;
            }
            //Either way, when updating the list, we update the map as well
            else if (size == 0) allDocTask.addOnCompleteListener(listener);
            else addActivitiesOnSuccess(firestoreActivityManager.getRecentlyAddedActivities(size)).addOnCompleteListener(listener);
        });
    }

    /**
     * On success, add the activities from db to the local list of activities
     *
     * @param task task containing the requested snapshots, that would then be deserialized into activities
     */
    private static Task addActivitiesOnSuccess(Task<QuerySnapshot> task) {
        return task.addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot docSnap : queryDocumentSnapshots.getDocuments()) {
                activities.add(serializer.deserialize(docSnap.getData()));
                Log.d(TAG, "activities size is " + activities.size());
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
