package com.sdp.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.movemeet.Backend.BackendActivityManagerDemo;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.sdp.movemeet.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Called when the user taps the Go button */
    @SuppressWarnings("unused")
    public void sendMessage(View view) {
        Intent intent = new Intent(this, GreetingActivity.class);
        EditText editText = findViewById(R.id.mainEditName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToFirebaseDebug(View view) {
        Intent intent = new Intent(this, BackendActivityManagerDemo.class);
        startActivity(intent);
    }
}