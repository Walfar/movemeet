package com.sdp.movemeet.backend.firebase.storage;


import com.google.android.gms.tasks.Task;
import com.sdp.movemeet.backend.BackendStorage;
import com.sdp.movemeet.models.FirebaseObject;


public class StorageManager<T extends FirebaseObject> implements BackendStorage<T> {



    @Override
    public Task<?> add(T object, String path) {
        //TODO
        return null;
    }

    @Override
    public Task<?> delete(String path) {
        //TODO
        return null;
    }

    @Override
    public Task<?> get(String path) {
        //TODO
        return null;
    }

    @Override
    public Task<?> search(String field, Object value) {
        //TODO
        return null;
    }
}