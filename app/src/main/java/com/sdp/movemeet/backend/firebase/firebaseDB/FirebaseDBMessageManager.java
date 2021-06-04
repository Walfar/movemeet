package com.sdp.movemeet.backend.firebase.firebaseDB;

import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.Message;

public class FirebaseDBMessageManager extends FirebaseDBManager<Message> {

    /**
     * Creates a new FirebaseDBManager.
     *
     * @param serializer a BackendSerializer capable of de(serializing) objects of type T
     */
    public <T> FirebaseDBMessageManager(BackendSerializer serializer) {
        super(serializer);
    }
}
