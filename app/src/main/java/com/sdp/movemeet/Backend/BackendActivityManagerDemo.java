package com.sdp.movemeet.Backend;

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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.Navigation.Navigation;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.map.MapsActivity;

import java.util.ArrayList;
import java.util.Date;

public class BackendActivityManagerDemo extends AppCompatActivity {

    private FirebaseFirestore db;
    private BackendActivityManager bam;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    TextView fullName, email, phone;
    FirebaseFirestore fStore;
    String userId;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backend_manager_demo);

        db = FirebaseFirestore.getInstance();
        bam = new BackendActivityManager(db, "debug");


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
        navigationView.setCheckedItem(R.id.nav_firebase_debug);

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
            case R.id.nav_firebase_debug:
                break;
            case R.id.nav_start_activity:
                Navigation.startActivity(this.navigationView);
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
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, BackendActivityManagerDemo.this);
        }
    }

    public void logout(View view) {
        if (fAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }

    public void uploadActivity(View view) {
        EditText editText = findViewById(R.id.editHostUpload);
        String host = editText.getText().toString();
        if (host.isEmpty()) host = "NO_HOST_GIVEN";

        Activity act = new Activity("activity",
                host,
                "title",
        10,
        new ArrayList<String>(),
        0,
        0,
        "desc",
        new Date(),
        10,
        Sport.Running,
        "address");

        TextView res = findViewById(R.id.searchResult);

        bam.uploadActivity(act,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        res.setText("Successfully uploaded activity");
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        res.setText("Could not upload activity");
                    }
                });
    }

    public void searchActivity(View view) {
        EditText editText = findViewById(R.id.editHostSearch);
        String host = editText.getText().toString();

        Query q = bam.getActivitiesCollectionReference()
                .whereEqualTo("organizerId", host)
                //.orderBy("time")
                .limit(1);

        TextView res = findViewById(R.id.searchResult);
        res.setText("No result found");

        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot docSnap: queryDocumentSnapshots.getDocuments()) {
                    res.setText(docSnap.get("organizerId") + "\n" + docSnap.get("date"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                res.setText("Failed to retrieve data");
            }
        });
    }

    public void deleteActivity(View view) {
        EditText editText = findViewById(R.id.editHostDelete);
        String host = editText.getText().toString();

        Query q = bam.getActivitiesCollectionReference()
                .whereEqualTo("organizerId", host)
                //.orderBy("time")
                .limit(1);

        TextView res = findViewById(R.id.searchResult);

        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                    Activity act = new Activity("",
                            queryDocumentSnapshots.getDocuments().get(0).get("organizerId").toString(),
                            "title",
                            10,
                            new ArrayList<String>(),
                            0,
                            0,
                            "desc",
                            new Date(),
                            10,
                            Sport.Running,
                            "address");
                    act.setBackendRef(queryDocumentSnapshots.getDocuments().get(0).getReference());

                    bam.deleteActivity(act, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    res.setText("Successfully deleted activity");
                                }
                            },
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    res.setText("Failed to delete activity");
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                res.setText("Failed to retrieve activity to be deleted");
            }
        });
    }
}