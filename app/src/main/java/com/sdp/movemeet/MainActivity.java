package com.github.vvhaleshark.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.vvhaleshark.movemeet.R;
import com.github.vvhaleshark.movemeet.map.MapsActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}