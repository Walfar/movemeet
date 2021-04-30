package com.sdp.movemeet.backend.firebase.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.backend.serialization.BackendSerializer;

public class FirestoreActivityManager extends FirestoreManager<Activity> {

    public final static String ACTIVITIES_COLLECTION = "activities";

    private final FirebaseFirestore db;
    private final String collection;


    public FirestoreActivityManager(FirebaseFirestore db, String collection, BackendSerializer<Activity> serializer) {
        super(db, collection, serializer);
        this.db = db;
        this.collection = collection;
    }

    @Override
    public Task<QuerySnapshot> search(String field, Object value) {
        // TODO: return 50 closest activities and execute query on those
        return super.search(field, value);
    }

}
