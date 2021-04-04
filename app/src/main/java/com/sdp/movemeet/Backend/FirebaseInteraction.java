package com.sdp.movemeet.Backend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sdp.movemeet.EditProfileActivity;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.ProfileActivity;

import java.util.HashMap;
import java.util.Map;

public class FirebaseInteraction {

    public static TextView[] retrieveDataFromFirebase(FirebaseFirestore fStore, String userId, TextView[] textViewArray, Activity activity) {
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (textViewArray.length > 3) {
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


    public static void checkIfUserSignedIn(FirebaseAuth fAuth, Activity activity) {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user == null) {
            // If not signed in, then launch the LoginActivity
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Context mContext = activity.getApplicationContext();
            mContext.startActivity(intent);
            activity.finish();
        }
    }

    public static void logoutIfUserNull(FirebaseAuth fAuth, Activity activity) {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            // Logging out the user from Firebase
            FirebaseAuth.getInstance().signOut();
            // Launching the LoginActivity
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Context mContext = activity.getApplicationContext();
            mContext.startActivity(intent);
            activity.finish();
        }
    }


}
