package com.sdp.movemeet.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.view.navigation.Navigation;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.sdp.movemeet.MESSAGE";

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The aim is to block any direct access to this page if the user is not logged
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }

        if (Navigation.profileField) new Navigation(this, R.id.nav_home).createDrawer();

    }
}