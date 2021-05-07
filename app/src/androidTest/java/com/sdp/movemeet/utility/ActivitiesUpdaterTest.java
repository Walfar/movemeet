package com.sdp.movemeet.utility;


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.backend.BackendActivityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
