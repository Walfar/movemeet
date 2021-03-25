package com.sdp.movemeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sdp.movemeet.map.GPSRecordingActivity;
import com.sdp.movemeet.map.MapsActivity;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void signIn(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // redirecting the user to the "Login" activity
    }

    public void noAccount(View v) {
        startActivity(new Intent(getApplicationContext(), MapsActivity.class)); // redirecting the user to the "Login" activity
    }

    public void RecordRun(View v) {
        startActivity(new Intent(getApplicationContext(), GPSRecordingActivity.class)); // Redirect the user to the GPS recording activity
    }
}