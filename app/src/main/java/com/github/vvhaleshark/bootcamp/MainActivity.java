package com.github.vvhaleshark.bootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.github.vvhaleshark.bootcamp.MESSAGE";

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
}