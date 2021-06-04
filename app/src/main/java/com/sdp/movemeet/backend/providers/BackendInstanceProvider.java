package com.sdp.movemeet.backend.providers;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class BackendInstanceProvider {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static FirebaseFirestore firestore;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static FirebaseStorage storage;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static FirebaseDatabase database;

    public static FirebaseFirestore getFirestoreInstance() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }

    public static FirebaseStorage getStorageInstance() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }
        return storage;
    }

    public static FirebaseDatabase getDatabaseInstance() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }
}