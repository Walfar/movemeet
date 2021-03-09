package com.sdp.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FirebaseUsersGreetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_users_greeting);

        // Get the Intent that started this Activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(FirebaseUsersMainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView greetingMessage = findViewById(R.id.greetingMessage);
        greetingMessage.setText(getString(R.string.greeting_message, message));
    }
}