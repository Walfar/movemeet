package com.sdp.movemeet.Backend;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

abstract class FirebaseManager<T> implements BackendInteraction<T> {

    public FirebaseManager(FirebaseFirestore db) {

    }
    
    public FirebaseManager(FirebaseDatabase db) {

    }

    @Override
    public void add(T object) {

    }

    @Override
    public void delete(T object) {

    }

    @Override
    public List<T> search(String field, Object value) {
        return null;
    }
}
