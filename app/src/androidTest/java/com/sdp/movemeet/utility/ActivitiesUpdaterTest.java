package com.sdp.movemeet.utility;


import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.map.MainMapFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ActivitiesUpdaterTest {

    public static FirebaseFirestore db;
    public static ActivitiesUpdater updater;
    public static BackendActivityManager bam;


    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
        updater = ActivitiesUpdater.getInstance();
        bam = new BackendActivityManager(db, "activities");
    }


    @Test
    public void instanceIsNeverNull() {
        assertNotNull(updater);
    }
}
