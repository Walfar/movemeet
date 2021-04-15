package com.sdp.movemeet.utility;


import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;
import com.sdp.movemeet.Sport;

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

    @Test
    public void activitiesUpdatesOnAdd() {

        Activity act = new Activity("activity",
                "me",
                "title",
                10,
                new ArrayList<String>(),
                0,
                0,
                "desc",
                new Date(),
                10,
                Sport.Running,
                "address");

        bam.uploadActivity(act,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        List<Activity> activities = updater.getActivities();
                        Activity act_in_collection = activities.get(activities.size()-1);
                        assertEquals(act.getActivityId(), act_in_collection.getActivityId());
                        assertEquals(act.getAddress(), act_in_collection.getAddress());
                        //assertEquals(act.getDate(), act_in_collection.getDate());
                        assertEquals(act.getDescription(), act_in_collection.getDescription());
                        //assertEquals(act.getLatitude(), act_in_collection.getLatitude());
                        //assertEquals(act.getLongitude(), act_in_collection.getLongitude());
                        assertEquals(act.getNumberParticipant(), act_in_collection.getNumberParticipant());
                        assertEquals(act.getParticipantId(), act_in_collection.getParticipantId());
                        assertEquals(act.getTitle(), act_in_collection.getTitle());
                        assertEquals(act.getOrganizerId(), act_in_collection.getOrganizerId());
                        assertEquals(act.getSport(), act_in_collection.getSport());
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

}
