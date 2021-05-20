package com.sdp.movemeet.view.activity;

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
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.view.navigation.Navigation;
import com.sdp.movemeet.R;

/***
 *  This class create the list of activities, on witch the user is registered.
 */


public class ActivityListActivity extends AppCompatActivity {

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(enableNav) new Navigation(this, R.id.nav_list_activities).createDrawer();
    }
}
