package com.sdp.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sdp.movemeet.Backend.BackendActivityManagerDemo;
import com.sdp.movemeet.Backend.FirebaseInteraction;


public class FirebaseUsersMainActivity extends AppCompatActivity {

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

        handleRegisterUser();

    }

    /* Called when the user taps the Go button */
    @SuppressWarnings("unused")
    public void sendMessage(View view) {
        Intent intent = new Intent(this, FirebaseUsersGreetingActivity.class);
        EditText editText = findViewById(R.id.mainEditName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
        startActivity(new Intent(getApplicationContext(), FirebaseUsersLogin.class)); // sending the user to the "Login" activity
        finish();
    }


    public void goToFirebaseDebug(View view) {
        Intent intent = new Intent(this, BackendActivityManagerDemo.class);
        startActivity(intent);
    }

    public void handleRegisterUser() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            TextView[] textViewArray = {phone, fullName, email};
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, FirebaseUsersMainActivity.this);
        }

    }

    public void goToUserProfileActivity(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}