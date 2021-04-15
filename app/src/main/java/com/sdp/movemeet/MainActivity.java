package com.sdp.movemeet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.sdp.movemeet.Activity.ActivityDescriptionActivity;

import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.Navigation.Navigation;
import com.sdp.movemeet.utility.ActivitiesUpdater;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.sdp.movemeet.MESSAGE";

    TextView fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        updater.updateListActivities();

        //createDrawer();
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        textView=findViewById(R.id.textView);
        toolbar=findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        View hView =  navigationView.inflateHeaderView(R.layout.header);

        fullName = hView.findViewById(R.id.text_view_profile_name);
        phone = hView.findViewById(R.id.text_view_profile_phone);
        email = hView.findViewById(R.id.text_view_profile_email);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(R.id.nav_home);

        handleRegisterUser();

        //The aim is to block any direct access to this page if the user is not logged
        //Smth must be wrong since it prevents automatic connection during certain tests
        /*if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }*/

    }

    /*public void createDrawer(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        textView=findViewById(R.id.textView);
        toolbar=findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        View hView =  navigationView.inflateHeaderView(R.layout.header);

        fullName = hView.findViewById(R.id.text_view_profile_name);
        phone = hView.findViewById(R.id.text_view_profile_phone);
        email = hView.findViewById(R.id.text_view_profile_email);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(R.id.nav_home);
    }*/

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_edit_profile:
                Navigation.goToUserProfileActivity(this.navigationView);
                break;
            case R.id.nav_add_activity:
                Navigation.goToActivityUpload(this.navigationView);
                break;
            case R.id.nav_logout:
                FirebaseInteraction.logoutIfUserNull(fAuth, this);
                break;
            case R.id.nav_start_activity:
                Navigation.startActivity(this.navigationView);
                break;
            case R.id.nav_chat:
                Navigation.goToChat(this.navigationView);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }

    public void handleRegisterUser() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            TextView[] textViewArray = {fullName, email, phone};
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, MainActivity.this);
        }
    }

}