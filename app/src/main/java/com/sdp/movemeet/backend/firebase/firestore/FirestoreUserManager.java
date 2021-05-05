package com.sdp.movemeet.backend.firebase.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.User;

/**
 * A class capable of handling User storage operations with a FirebaseFirestore backend
 */
public class FirestoreUserManager extends FirestoreManager<User> {

    // The name of the Firestore collection containing Activities
    public final static String USERS_COLLECTION = "users";

    private final FirebaseFirestore db;
    private final String collection;

    public FirestoreUserManager(FirebaseFirestore db, String collection, BackendSerializer<User> serializer) {
        super(db, collection, serializer);
        this.db = db;
        this.collection = collection;
    }

}

