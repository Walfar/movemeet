package com.sdp.movemeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        startActivity(new Intent(getApplicationContext(), MapsActivity.class)); // redirecting user to the "Maps" activity when logged in
    }


}