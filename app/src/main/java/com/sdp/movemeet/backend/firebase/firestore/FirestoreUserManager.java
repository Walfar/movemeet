package com.sdp.movemeet.backend.firebase.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.User;

/**
 * A class capable of handling User storage operations with a Firebase Firestore backend
 */
public class FirestoreUserManager extends FirestoreManager<User> {

    // The name of the Firestore collection containing Activities
    public final static String USERS_COLLECTION = "users";

    private final static String PATH_SEPARATOR = "/";

    private final String collection;

    /**
     * Creates a new FirestoreManager capable of performing backend storage operations
     * on the User class.
     * @param collection the Firestore collection in which to operate
     * @param serializer a BackendSerializer capable of (de)serializing Users
     */
    public FirestoreUserManager(String collection, BackendSerializer<User> serializer) {
        super(collection, serializer);
        this.collection = collection;
    }

    /**
     * Retrieve a User from the backend using their uid
     * @param uid the uid of the User
     * @return a Task<DocumentSnapshot> whose result can be deserialized into a User object
     */
    public Task<DocumentSnapshot> getUserFromUid(String uid) {
        String path = USERS_COLLECTION + PATH_SEPARATOR + uid;
        return super.get(path);
    }
}

