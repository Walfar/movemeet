package com.sdp.movemeet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.chat.ChatActivity;
import com.sdp.movemeet.map.MapsActivity;
import com.sdp.movemeet.utility.ActivitiesUpdater;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.sdp.movemeet.MESSAGE";

    TextView fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = findViewById(R.id.text_view_profile_phone);
        fullName = findViewById(R.id.text_view_profile_name);
        email = findViewById(R.id.text_view_profile_email);

        ActivitiesUpdater updater = ActivitiesUpdater.getInstance();
        updater.updateListActivities();

        handleRegisterUser();

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

    public void goToMaps(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }


    public void logout(View view) {
        FirebaseInteraction.logoutIfUserNull(fAuth, MainActivity.this);
    }


    public void goToFirebaseDebug(View view) {
    }

    public void goToActivityUpload(View view) {
        Intent intent = new Intent(this, UploadActivityActivity.class);
        startActivity(intent);
    }

    public void startActivity(View view) {
            Intent intent = new Intent(this, ActivityDescriptionActivity.class);
            startActivity(intent);
    }

    public void goToUserProfileActivity(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}