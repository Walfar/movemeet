package com.sdp.movemeet.ui.workout;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.sdp.movemeet.R;
import com.sdp.movemeet.ui.workout.SectionsPagerAdapter;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Items List");

        // add download here
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Downloading");

                // System.out.println()

                switch (viewPager.getCurrentItem()) {
                    case 0: // text-based workouts
                        Snackbar.make(view, "All text-based workouts downloaded!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 1: // image-based workouts
                        Snackbar.make(view, "All image-based workouts downloaded!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    case 2: // PDF-based workouts
                        Snackbar.make(view, "All PDF-based workouts downloaded!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                }
            }
        });
    }
}