package com.sdp.movemeet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.squareup.picasso.Picasso;
import com.sdp.movemeet.Navigation.Navigation;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_FULL_NAME = "fullName";
    public static final String EXTRA_MESSAGE_EMAIL = "email";
    public static final String EXTRA_MESSAGE_PHONE = "phone";
    public static final String EXTRA_MESSAGE_DESCRIPTION = "description";

    ImageView profileImage;
    TextView fullName, email, phone, description;
    ProgressBar progressBar;

    String userId;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.image_view_profile_image);
        fullName = findViewById(R.id.text_view_activity_profile_name);
        email = findViewById(R.id.text_view_activity_profile_email);
        phone = findViewById(R.id.text_view_activity_profile_phone);
        description = findViewById(R.id.text_view_activity_profile_description);
        progressBar = findViewById(R.id.progress_bar_profile);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            displayRegisteredUserData();

            storageReference = FirebaseStorage.getInstance().getReference();
            loadRegisteredUserProfilePicture();
        }

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
        navigationView.setCheckedItem(R.id.nav_edit_profile);

        handleRegisterUser();

        //The aim is to block any direct access to this page if the user is not logged
        //Smth must be wrong since it prevents automatic connection during certain tests
        /*if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }*/

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intentHome = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intentHome);
                break;
            case R.id.nav_edit_profile:
                break;
            case R.id.nav_add_activity:
                Intent intentAddActivity = new Intent(ProfileActivity.this, UploadActivityActivity.class);
                startActivity(intentAddActivity);
                break;
            case R.id.nav_logout:
                logout(this.navigationView);
                break;
            case R.id.nav_map:
                Navigation.goToMaps(this.navigationView);
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
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ProfileActivity.this);
        }
    }

    public void logout(View view) {
        if (fAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }

    public void displayRegisteredUserData() {
        fStore = FirebaseFirestore.getInstance();
        TextView[] textViewArray = {fullName, email, phone, description};
        textViewArray = FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ProfileActivity.this);
        fullName = textViewArray[0];
        email = textViewArray[1];
        phone = textViewArray[2];
        description = textViewArray[3];
    }




    private void loadRegisteredUserProfilePicture() {
        StorageReference profileRef = storageReference.child("users/" + userId + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
    }


    public void goToEditProfileActivity(View view) {
        Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
        i.putExtra(EXTRA_MESSAGE_FULL_NAME, fullName.getText().toString()); // key, value
        i.putExtra(EXTRA_MESSAGE_EMAIL, email.getText().toString());
        i.putExtra(EXTRA_MESSAGE_PHONE, phone.getText().toString());
        i.putExtra(EXTRA_MESSAGE_DESCRIPTION, description.getText().toString());
        startActivity(i);
    }

}