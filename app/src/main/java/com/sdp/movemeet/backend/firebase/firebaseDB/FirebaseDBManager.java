package com.sdp.movemeet.backend.firebase.firebaseDB;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.BackendSerializer;
import com.sdp.movemeet.models.FirebaseObject;

import java.util.Map;

/**
 * A class capable of interacting with the Firebase Realtime Database to handle storage
 * operations on objects of type T
 *
 * @param <T> The type of object handled by this FirebaseDBManager
 */
abstract class FirebaseDBManager<T extends FirebaseObject> implements BackendManager<T> {

    private final BackendSerializer<T> serializer;
    private final FirebaseDatabase db;

    /**
     * Creates a new FirebaseDBManager.
     *
     * @param serializer a BackendSerializer capable of de(serializing) objects of type T
     * @param <T>        the type of object handled by this FirebaseDBManager
     */
    public <T> FirebaseDBManager(BackendSerializer serializer) {
        if (serializer == null) throw new IllegalArgumentException();

        this.db = BackendInstanceProvider.getDatabaseInstance();
        this.serializer = serializer;
    }

    @Override
    public Task<?> add(T object, String path) {
        if (object == null || path == null || path.isEmpty()) throw new IllegalArgumentException();

        Map<String, Object> data = serializer.serialize(object);
        DatabaseReference newChild = db.getReference(path).push();
        return newChild.setValue(data);
    }

    @Override
    public Task<?> set(T object, String path) {
        if (object == null || path == null || path.isEmpty()) throw new IllegalArgumentException();

        Map<String, Object> data = serializer.serialize(object);
        DatabaseReference newChild = db.getReference(path);
        newChild.setValue(data);
        return newChild.setValue(data);
    }

    @Override
    public Task<?> update(String path, String field, String value, String method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Task<?> delete(String path) {
        if (path == null || path.isEmpty()) throw new IllegalArgumentException();
        return db.getReference(path).removeValue();
    }

    @Override
    public Task<?> get(String path) {
        if (path == null || path.isEmpty()) throw new IllegalArgumentException();
        return db.getReference(path).get();
    }

    @Override
    public Task<?> search(String field, Object value) {
        throw new UnsupportedOperationException();
    }
}
