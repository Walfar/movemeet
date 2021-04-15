/*package com.sdp.movemeet.utility;

import android.Manifest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Sport;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ActivityCacheTest {

    @Rule
    public GrantPermissionRule mRuntimeWritePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule mRuntimeReadPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Test
    public void loadFromCacheReturnsListSaved() throws InterruptedException {
        ActivityCache cache = new ActivityCache();
        Activity act = new Activity("activity from cache 1",
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
                "address",
                new Date());
        Activity act2 = new Activity("activity from cache 2",
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
                "address",
                new Date());
        ArrayList<Activity> activities = new ArrayList<>();
        activities.add(act);
        activities.add(act2);
        cache.saveActivitiesInCache(activities);
        Thread.sleep(2000);
        assertEquals(act, cache.loadActivitiesFromCache().get(0));
        assertEquals(act2, cache.loadActivitiesFromCache().get(1));
    }
} */
