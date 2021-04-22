package com.sdp.movemeet;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.utility.ActivitiesUpdater;

public class HomeUnregister extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView textView;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_unregister);


        createDrawer();

        /*if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }*/

    }

    public void createDrawer(){
        drawerLayout=findViewById(R.id.drawer_layout_unregister);
        textView=findViewById(R.id.textViewUnregister);
        toolbar=findViewById(R.id.toolbarUnregister);
    }
}