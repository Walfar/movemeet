package com.sdp.movemeet.view.workout;

import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.navigation.Navigation;

public class WorkoutActivity extends AppCompatActivity {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        if (enableNav) new Navigation(this, R.id.nav_workouts).createDrawer();

    }
}