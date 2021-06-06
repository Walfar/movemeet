package com.sdp.movemeet.view.workout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.movemeet.R;

public class TextWorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_workout);

        TextView detailTv = findViewById(R.id.textView);

        Intent intent = getIntent();
        String content = intent.getStringExtra("contentTv");

        detailTv.setText(content);

    }
}