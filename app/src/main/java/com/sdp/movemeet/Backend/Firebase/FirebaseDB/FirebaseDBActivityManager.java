package com.sdp.movemeet.Backend.Firebase.FirebaseDB;

import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.Serialization.BackendSerializer;

public class FirebaseDBActivityManager extends FirebaseDBManager<Activity> {

    public <T> FirebaseDBActivityManager(FirebaseDatabase db, BackendSerializer serializer) {
        super(db, serializer);
    }


}
