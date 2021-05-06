package com.sdp.movemeet.backend.firebase.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.FirebaseObject;

import java.util.Map;

/**
 * A class capable of interacting with a FirebaseFirestore backend to perform storage operations
 * for objects of type T
 *
 * @param <T> The type of object handled by this FirestoreManager
 */
abstract class FirestoreManager<T extends FirebaseObject> implements BackendManager<T> {

    private final BackendSerializer<T> serializer;
    private final FirebaseFirestore db;
    private final String collection;

    /**
     * Creates a new FirestoreManager
     *
     * @param db         the instance of FirebaseFirestore to interact with.
     * @param collection the Firestore collection in which to perform operations
     * @param serializer a BackendSerializer capable of (de)serializing objects of type T
     */
    public FirestoreManager(FirebaseFirestore db, String collection, BackendSerializer<T> serializer) {
        if (db == null || collection == null || serializer == null)
            throw new IllegalArgumentException();

        this.db = db;
        this.serializer = serializer;
        this.collection = collection;
    }

    /**
     * Adds an object to the FirebaseFirestore backend. Because of the structure of Firestore,
     * the path parameter is ignored; a new path is automatically generated, or the old path is used
     * if the instance has already been uploaded to the backend.
     *
     * @param object the instance of T to add.
     * @param path   the path of the instance in the backend.
     * @return a Task<Void>, the success of which indicates the success of the operation.
     */
    @Override
    public Task<Void> add(T object, String path) {
        if (object == null || path == null || path.isEmpty()) throw new IllegalArgumentException();

        String documentPath = db.collection(collection).document().getPath();
        object.setDocumentPath(documentPath);
        Map<String, Object> data = serializer.serialize(object);

        return db.document(object.getDocumentPath()).set(data);
    }

    @Override
    public Task<Void> delete(String path) {
        if (path == null || path.isEmpty()) throw new IllegalArgumentException();

        return db.document(path).delete();

    }

    @Override
    public Task<QuerySnapshot> search(String field, Object value) {
        if (field == null || field.isEmpty() || value == null) throw new IllegalArgumentException();

        return db.collection(collection).whereEqualTo(field, value).get();
    }

    @Override
    public Task<DocumentSnapshot> get(String path) {
        if (path == null || path.isEmpty()) throw new IllegalArgumentException();

        return db.document(path).get();
    }
}
