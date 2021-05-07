package com.sdp.movemeet.backend.firebase.firebaseDB;

import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.backend.serialization.BackendSerializer;

public class FirebaseDBActivityManager extends FirebaseDBManager<Activity> {

    public <T> FirebaseDBActivityManager(FirebaseDatabase db, BackendSerializer serializer) {
        super(db, serializer);
    }


}
