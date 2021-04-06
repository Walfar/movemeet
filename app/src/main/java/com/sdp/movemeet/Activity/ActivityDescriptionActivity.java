package com.sdp.movemeet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.Navigation.Navigation;
import com.sdp.movemeet.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ActivityDescriptionActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    Button RegisterToActivityButton;
    private Activity act;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    TextView fullName, email, phone;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();

        if (intent != null) {
            act = (Activity) intent.getSerializableExtra("activity");
        }

        createTitleView();
        createParticipantNumberView();
        createDescriptionView();
        createDateView();
        createAddressView();
        createSportView();
        createDurationView();
        createOrganisatorView();

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
        navigationView.setCheckedItem(R.id.nav_start_activity);

        handleRegisterUser();

        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Navigation.goToHome(this.navigationView);
                break;
            case R.id.nav_edit_profile:
                Navigation.goToUserProfileActivity(this.navigationView);
                break;
            case R.id.nav_add_activity:
                Navigation.goToActivityUpload(this.navigationView);
                break;
            case R.id.nav_logout:
                logout(this.navigationView);
                break;
            case R.id.nav_map:
                Navigation.goToMaps(this.navigationView);
                break;
            case R.id.nav_start_activity:
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
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ActivityDescriptionActivity.this);
        }
    }

    public void logout(View view) {
        if (fAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }

    private void createTitleView() {
        // activityTitle from the activity
        TextView activityTitle = (TextView) findViewById(R.id.activity_title_description);
        if (act != null) activityTitle.setText(act.getTitle());
    }

    private void createParticipantNumberView(){
        // number of participants from the activity
        TextView numberParticipantsView = (TextView) findViewById(R.id.activity_number_description);
        if (act != null) {
            numberParticipantsView.setText(act.getParticipantId().size() + "/" + act.getNumberParticipant());
        }
    }

    private void createDescriptionView(){
        // description from the activity
        TextView descriptionView = (TextView) findViewById(R.id.activity_address_description);
        if (act != null) descriptionView.setText(act.getDescription());
    }

    private void createDateView(){
        // date from the activity
        TextView dateView = (TextView) findViewById(R.id.activity_date_description);
        if (act != null) {
            String pattern = "MM/dd/yyyy HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String todayAsString = df.format(act.getDate());
            dateView.setText(todayAsString);
        }
    }

    private void createSportView(){
        TextView sportView = (TextView) findViewById(R.id.activity_sport_description);
        if(act != null){
            sportView.setText(act.getSport().toString());
        }
    }

    private void createDurationView(){
        TextView durationView = (TextView) findViewById(R.id.activity_duration_description);
        if(act != null){
            durationView.setText(String.valueOf((int) act.getDuration()));
        }
    }

    private void createOrganisatorView(){
        TextView organisatorView = (TextView) findViewById(R.id.activity_organisator_description);
        if(act != null){
            organisatorView.setText(act.getOrganizerId());
        }
    }

    private void createAddressView(){
        // address from the activity
        TextView addressView = (TextView) findViewById(R.id.activity_address_description);
        if (act != null) {
            addressView.setText(act.getAddress());
        }
    }

    public void onClick(View v) {
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            String userId;
            userId = fAuth.getCurrentUser().getUid();
            try{
                act.addParticipantId(userId);
                createParticipantNumberView();}
            catch(Exception e){
                Toast.makeText(ActivityDescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}