package com.sdp.movemeet.backend.providers;

import androidx.annotation.VisibleForTesting;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class BackendInstanceProvider {

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static final BackendInstanceProvider instance = new BackendInstanceProvider();

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public FirebaseFirestore firestore;
    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public FirebaseStorage storage;
    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public FirebaseDatabase database;

    private BackendInstanceProvider() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
    }



    public static FirebaseFirestore getFirestoreInstance() {
        return instance.firestore;
    }

    public static FirebaseStorage getStorageInstance() {
        return instance.storage;
    }

    public static FirebaseDatabase getDatabaseInstance() {
        return instance.database;
    }
}
