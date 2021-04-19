package com.sdp.movemeet.Backend.Firebase.Firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.Serialization.BackendSerializer;

public class FirebaseActivityManager extends FirestoreManager<Activity> {

    private final FirebaseFirestore db;
    private final String collection;


    public FirebaseActivityManager(FirebaseFirestore db, String collection, BackendSerializer<Activity> serializer) {
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
