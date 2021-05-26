package com.sdp.movemeet.backend.firebase.firebaseDB;

import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.backend.serialization.BackendSerializer;

public class FirebaseDBActivityManager extends com.sdp.movemeet.backend.firebase.firebaseDB.FirebaseDBManager<Activity> {

    public <T> FirebaseDBActivityManager(BackendSerializer serializer) {
        super(serializer);
    }


}
