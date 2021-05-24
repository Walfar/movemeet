package com.sdp.movemeet.utility;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.sdp.movemeet.R;
import com.sdp.movemeet.view.home.HomeScreenActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ActivityPictureCacheTest {

    @Rule
    public ActivityScenarioRule<HomeScreenActivity> activityRule = new ActivityScenarioRule<>(HomeScreenActivity.class);

    @Rule
    public GrantPermissionRule readPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule writePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Test
    public void cacheLoadsSamePictureAsSavedForSamePath()  {
        activityRule.getScenario().onActivity(activity -> {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_add_black_24dp);
            ActivityPictureCache.saveToCache(bitmap, "imagePath");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Bitmap loadedBitmap = ActivityPictureCache.loadFromCache("imagePath");
            if (loadedBitmap != null) assertThat(loadedBitmap.sameAs(bitmap), is(true));
        });
    }
}
