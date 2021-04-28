package com.sdp.movemeet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.sdp.movemeet.map.GPSRecordingActivity;

import com.sdp.movemeet.utility.ActivitiesUpdater;
import com.sdp.movemeet.utility.LocationFetcher;

import static com.sdp.movemeet.utility.LocationFetcher.REQUEST_CODE;


public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
       }

        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        // Always clear activities first, to prevent duplicates if multiple intents are created
        updater.clearLocalActivities();
        updater.fetchListActivities();
    }


    /**
     * Called when the user clicks on the "sign in" button. If logged, brings to the map nav, else brings to log in screen.
     * @param v view for the sign in button
     */
    public void signIn(View v) {
        if (isUserLogged()) startActivity(new Intent(this, MainActivity.class));
        else startActivity(new Intent(this, LoginActivity.class)); // redirecting the user to the "Login" activity
    }

    /**
     * Called when the user clicks on the "no account" button. If logged, brings to the map nav, else brings to the map without nav
     * @param v view for the no account button
     */
    public void noAccount(View v) {
        if (isUserLogged()) startActivity(new Intent(this, MainActivity.class));
        else startActivity(new Intent(this, MainUnregister.class));
    }

    /**
     * Called when the user clicks on the "record run" button. Brings to the GPS recording screen.
     * @param v view for the record run button
     */
    public void RecordRun(View v) {
        startActivity(new Intent(this, GPSRecordingActivity.class)); // Redirect the user to the GPS recording activity
    }

    /**
     * Checks if the user is already logged
     * @return true if the user is logged, false otherwise
     */
    private boolean isUserLogged() {
        return (FirebaseAuth.getInstance().getCurrentUser() != null);
    }

}