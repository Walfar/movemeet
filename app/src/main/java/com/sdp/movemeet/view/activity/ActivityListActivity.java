package com.sdp.movemeet.view.activity;

import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.sdp.movemeet.view.navigation.Navigation;
import com.sdp.movemeet.R;

/***
 *  This class create the list of activities, on witch the user is registered.
 */


public class ActivityListActivity extends AppCompatActivity {

    /*
    TextView fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;*/

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if(enableNav) new Navigation(this, R.id.nav_list_activities).createDrawer();
    }
}
