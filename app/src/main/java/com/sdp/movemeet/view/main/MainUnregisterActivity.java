package com.sdp.movemeet.view.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.movemeet.R;

public class MainUnregisterActivity extends AppCompatActivity {
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unregister);

        createDrawer();
    }

    public void createDrawer() {
        textView = findViewById(R.id.textViewUnregister);
    }
}