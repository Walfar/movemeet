package com.sdp.movemeet.backend.firebase.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Activity;

/**
 * A class capable of handling Activity storage operations with a Firebase Firestore backend
 */
public class FirestoreActivityManager extends FirestoreManager<Activity> {

    // The name of the Firestore collection containing Activities
    public final static String ACTIVITIES_COLLECTION = "activities";

    private final FirebaseFirestore db;
    private final String collection;

    public FirestoreActivityManager(String collection, BackendSerializer<Activity> serializer) {
        super(collection, serializer);
        this.db = BackendInstanceProvider.getFirestoreInstance();;
        this.collection = collection;
    }

    @Override
    public Task<QuerySnapshot> search(String field, Object value) {
        // TODO: return 50 closest activities and execute query on those
        return super.search(field, value);
    }

    /**
     * Returns a list of recently added activities, with the specified size
     * @param size number of recently added activities we want to get
     * @return a querySnapshot for size number of recently added activities
     */
    public Task<QuerySnapshot> getRecentlyAddedActivities(int size) {
        return db.collection(collection).orderBy("createdAt", Query.Direction.DESCENDING).limit(size).get();
    }

    /**
     * When the given activity is updated in the db, the event listener should trigger the given callback
     * @param activity activity that is listened on
     * @param eventListener used to trigger a callback when activity changes in db
     */
    public void setActivitiesUpdateListener(Activity activity, EventListener<DocumentSnapshot> eventListener) {
        db.collection(collection).document(activity.getDocumentPath()).addSnapshotListener(eventListener);
    }

    /**
     * Gets all documents in the corresponding collection
     * @return a querySnapshot for all documents in the collection
     */
    public Task<QuerySnapshot> getAll() {
        return db.collection(collection).get();
    }


}