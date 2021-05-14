package com.sdp.movemeet.backend;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sdp.movemeet.models.Activity;

/**
 * A class tasked with communicating with the Firebase backend
 * to both store and retrieve movemeet Activities.
 */

public class BackendActivityManager {

    public static String ACTIVITIES_COLLECTION = "activities";

    private final FirebaseFirestore db;
    private final String collection;

    /**
     *
     */
    public BackendActivityManager(FirebaseFirestore db, String collection) {
        this.db = db;
        this.collection = collection;
    }

    public void uploadActivity(Activity activity, OnSuccessListener<Void> onSuccess, OnFailureListener onFailure) {
        if (activity == null) throw new IllegalArgumentException();

        DocumentReference docRef;
        if (activity.getBackendRef() == null) {
            docRef = getActivitiesCollectionReference().document();
            activity.setBackendRef(docRef);
            //activity.setDocumentPath(docRef.getId());
        } else {
            docRef = activity.getBackendRef();
            //activity.setDocumentPath(docRef.getId());
        }
        activity.setDocumentPath(docRef.getId());

        docRef.set(activity, SetOptions.merge())
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    /**
     * Get the collection reference for activities to perform
     * queries on it
     *
     * @return a CollectionReference
     */
    public CollectionReference getActivitiesCollectionReference() {
        return db.collection(this.collection);
    }

    public void deleteActivity(Activity activity, OnSuccessListener<Void> onSuccess_inner, OnFailureListener onFailure) {
        if (activity == null) throw new IllegalArgumentException();
        if (activity.getBackendRef() == null) throw new IllegalArgumentException();

        activity.getBackendRef().delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        activity.setBackendRef(null);
                        onSuccess_inner.onSuccess(aVoid);
                    }
                })
                .addOnFailureListener(onFailure);
    }

}
