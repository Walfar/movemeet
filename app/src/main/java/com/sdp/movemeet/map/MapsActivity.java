package com.sdp.movemeet.map;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sdp.movemeet.R;
import com.sdp.movemeet.utility.ActivitiesUpdater;

public class MapsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapsFragment mapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        if (mapsFragment == null) {
            mapsFragment = mapsFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.activity_maps, mapsFragment).commit();
        }
    }

}
