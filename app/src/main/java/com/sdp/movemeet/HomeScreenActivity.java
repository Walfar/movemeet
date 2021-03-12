package com.sdp.movemeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }

    public void signIn(View v) {
        startActivity(new Intent(getApplicationContext(), FirebaseUsersLogin.class)); // redirecting the user to the "Login" activity
    }

    public void noAccount(View v) {
        startActivity(new Intent(getApplicationContext(), FirebaseUsersLogin.class)); // redirecting the user to the "Login" activity
    }
}