//package com.sdp.movemeet.backend.firebase.storage;
//
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.StorageReference;
//import com.sdp.movemeet.backend.BackendManager;
//import com.sdp.movemeet.backend.serialization.BackendSerializer;
//import com.sdp.movemeet.models.FirebaseObject;
//
//
//public class StorageManager<T extends FirebaseObject> implements BackendManager<T> {
//
//    private final StorageReference storageReference;
//
//
//    /**
//     * Creates a new StorageManager
//     *
//     * @param db         the instance of FirebaseStorage to interact with.
//     * @param collection the Firestore collection in which to perform operations
//     * @param serializer a BackendSerializer capable of (de)serializing objects of type T
//     */
//    public FirestoreManager(FirebaseFirestore db, String collection, BackendSerializer<T> serializer) {
//        if (db == null || collection == null || serializer == null)
//            throw new IllegalArgumentException();
//
//        this.db = db;
//        this.serializer = serializer;
//        this.collection = collection;
//    }
//
//
//    @Override
//    public Task<?> add(T object, String path) {
//        //TODO
//        return null;
//    }
//
//    @Override
//    public Task<?> delete(String path) {
//        //TODO
//        return null;
//    }
//
//    @Override
//    public Task<?> get(String path) {
//        //TODO
//        return null;
//    }
//
//    @Override
//    public Task<?> search(String field, Object value) {
//        //TODO
//        return null;
//    }
//}