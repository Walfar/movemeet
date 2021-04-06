package com.sdp.movemeet.Backend;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sdp.movemeet.EditProfileActivity;
import com.sdp.movemeet.ProfileActivity;

import java.util.HashMap;
import java.util.Map;

public class FirebaseInteraction {

    public static TextView[] retrieveDataFromFirebase(FirebaseFirestore fStore, String userId, TextView[] textViewArray, Activity activity) {
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot == null){
                    //nothing
                } else if (textViewArray.length == 4) {
                    textViewArray[0].setText(documentSnapshot.getString("fullName"));
                    textViewArray[1].setText(documentSnapshot.getString("email"));
                    textViewArray[2].setText(documentSnapshot.getString("phone"));
                    textViewArray[3].setText(documentSnapshot.getString("description"));
                } else {
                    textViewArray[0].setText(documentSnapshot.getString("fullName"));
                    textViewArray[1].setText(documentSnapshot.getString("email"));
                    textViewArray[2].setText(documentSnapshot.getString("phone"));
                }
            }
        });

        return textViewArray;
    }

    public static Map<String,Object> updateDataInFirebase(TextView profileFullName, TextView profileEmail, TextView profilePhone, TextView profileDescription) {
        Map<String,Object> edited = new HashMap<>();
        edited.put("fullName", profileFullName.getText().toString());
        edited.put("email", profileEmail.getText().toString());
        edited.put("phone", profilePhone.getText().toString());
        edited.put("description", profileDescription.getText().toString());

        return edited;
    }

}
