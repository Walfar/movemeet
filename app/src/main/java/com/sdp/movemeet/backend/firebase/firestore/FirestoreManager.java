package com.sdp.movemeet.backend.firebase.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.backend.BackendStorage;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.FirebaseObject;
import com.sdp.movemeet.backend.serialization.BackendSerializer;

import java.util.Map;
import java.util.concurrent.Executor;

abstract class FirestoreManager<T extends FirebaseObject> implements BackendStorage<T> {

    private final BackendSerializer<T> serializer;
    private final FirebaseFirestore db;
    private final String collection;

    public FirestoreManager(FirebaseFirestore db, String collection, BackendSerializer<T> serializer) {
        if (db == null || collection == null || serializer == null) throw new IllegalArgumentException();

        this.db = db;
        this.serializer = serializer;
        this.collection = collection;
    }

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

        return db
                .document(path)
                .delete();
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
