package com.sdp.movemeet.backend.providers;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Singleton class which provides a single backend storage service instance for use inside
 * other classes. The currently available instances are FirebaseFirestore, FirebaseDatabase,
 * and FirebaseStorage.
 */
public class BackendInstanceProvider {

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static final BackendInstanceProvider instance = new BackendInstanceProvider();

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static FirebaseFirestore firestore;
    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static FirebaseStorage storage;
    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static FirebaseDatabase database;

    private BackendInstanceProvider() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    /**
     * @return a singleton FirebaseFirestore instance
     */
    public static FirebaseFirestore getFirestoreInstance() {
        return instance.firestore;
    }

    /**
     * @return a singleton FirebaseStorage instance
     */
    public static FirebaseStorage getStorageInstance() {
        return instance.storage;
    }

    /**
     * @return a singleton FirebaseDatabase instance
     */
    public static FirebaseDatabase getDatabaseInstance() {
        return instance.database;
    }
}
