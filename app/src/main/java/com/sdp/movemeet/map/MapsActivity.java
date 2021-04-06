package com.sdp.movemeet.map;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.sdp.movemeet.R;

public class MapsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MainMapFragment mapsFragment = (MainMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        if (mapsFragment == null) {
            mapsFragment = MainMapFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.activity_maps, mapsFragment).commit();
        }
    }

}
