package com.sdp.movemeet.Backend.Firebase.Firestore;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.Serialization.BackendSerializer;

public class FirebaseActivityManager extends FirestoreManager<Activity> {

    public FirebaseActivityManager(FirebaseFirestore db, String collection, BackendSerializer<Activity> serializer) {
        super(db, collection, serializer);
    }

}
