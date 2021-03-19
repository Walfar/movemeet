package com.sdp.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.Backend.BackendActivityManagerDemo;
import com.sdp.movemeet.Backend.FirebaseInteraction;


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

        handleRegisterUser();

    }

    public void handleRegisterUser() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            TextView[] textViewArray = {phone, fullName, email};
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, MainActivity.this);
        }

    }

    @SuppressWarnings("unused")
    public void sendMessage(View view) {
        Intent intent = new Intent(this, GreetingActivity.class);
        EditText editText = findViewById(R.id.mainEditName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void logout(View view) {
        if (fAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }


    public void goToFirebaseDebug(View view) {
        Intent intent = new Intent(this, BackendActivityManagerDemo.class);
        startActivity(intent);
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