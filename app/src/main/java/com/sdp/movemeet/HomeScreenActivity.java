package com.sdp.movemeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


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

import java.util.ArrayList;
import java.util.Date;

import static com.sdp.movemeet.Backend.BackendActivityManager.ACTIVITIES_COLLECTION;
import static com.sdp.movemeet.Sport.Running;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        updater.updateListActivities();
    }

    public void signIn(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // redirecting the user to the "Login" activity
    }

    public void noAccount(View v) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class); // redirecting the user to the "Login" activity
        startActivity(intent);
    }

    public void RecordRun(View v) {
        startActivity(new Intent(getApplicationContext(), GPSRecordingActivity.class)); // Redirect the user to the GPS recording activity
    }
}