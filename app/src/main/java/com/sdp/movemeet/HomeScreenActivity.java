package com.sdp.movemeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.sdp.movemeet.map.GPSRecordingActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;

import com.sdp.movemeet.map.MapsActivity;
import com.sdp.movemeet.utility.ActivitiesUpdater;

public class HomeScreenActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        updater.updateListActivities();
    }

    public void signIn(View v) {
        if (user != null) startActivity(new Intent(this, MainActivity.class));
        else startActivity(new Intent(this, LoginActivity.class)); // redirecting the user to the "Login" activity
    }

    public void noAccount(View v) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void RecordRun(View v) {
        startActivity(new Intent(this, GPSRecordingActivity.class)); // Redirect the user to the GPS recording activity
    }
}