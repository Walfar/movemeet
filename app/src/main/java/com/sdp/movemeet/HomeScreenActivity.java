package com.sdp.movemeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.map.MapsActivity;
import com.sdp.movemeet.map.MapsFragment;
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
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // redirecting the user to the "Login" activity
    }

    public void noAccount(View v) {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
    }


}