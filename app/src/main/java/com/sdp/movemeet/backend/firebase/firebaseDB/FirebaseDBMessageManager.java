package com.sdp.movemeet.backend.firebase.firebaseDB;

import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Message;

public class FirebaseDBMessageManager extends FirebaseDBManager<Message> {

    /**
     * Creates a new FirebaseDBManager.
     *
     * @param db         the instance of FirebaseDatabase to interact with.
     * @param serializer a BackendSerializer capable of de(serializing) objects of type T
     */
    public <T> FirebaseDBMessageManager(FirebaseDatabase db, BackendSerializer serializer) {
        super(db, serializer);
    }
}
