package com.sdp.movemeet.Backend.Firebase.FirebaseDB;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.sdp.movemeet.Backend.BackendStorage;
import com.sdp.movemeet.Backend.Firebase.FirebaseObject;
import com.sdp.movemeet.Backend.Serialization.BackendSerializer;

abstract class FirebaseDBManager<T extends FirebaseObject> implements BackendStorage<T> {

    private final BackendSerializer<T> serializer;
    private final FirebaseDatabase db;

    public <T> FirebaseDBManager(FirebaseDatabase db, BackendSerializer serializer) {
        if (db == null || serializer == null) throw new IllegalArgumentException();

        this.db = db;
        this.serializer = serializer;
    }

    @Override
    public Task<?> add(T object) {
        return null;
    }

    @Override
    public Task<?> delete(T object) {
        return null;
    }

    @Override
    public Task<?> get(String path) {
        return null;
    }

    @Override
    public Task<?> search(String field, Object value) {
        return null;
    }
}
