package com.sdp.movemeet.Backend;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FirebaseInteraction {

    public static void retrieveDataFromFirebase(FirebaseFirestore fStore, String userId, TextView[] textViewArray, Activity activity) {
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                textViewArray[0].setText(documentSnapshot.getString("phone"));
                textViewArray[1].setText(documentSnapshot.getString("fullName"));
                textViewArray[2].setText(documentSnapshot.getString("email"));
            }
        });
    }

//    public static void retrieveDataFromFirebase(FirebaseFirestore fStore, String userId, TextView phone, TextView fullName, TextView email, Activity activity) {
//        DocumentReference documentReference = fStore.collection("users").document(userId);
//        documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                phone.setText(documentSnapshot.getString("phone"));
//                fullName.setText(documentSnapshot.getString("fullName"));
//                email.setText(documentSnapshot.getString("email"));
//            }
//        });
//    }

}
