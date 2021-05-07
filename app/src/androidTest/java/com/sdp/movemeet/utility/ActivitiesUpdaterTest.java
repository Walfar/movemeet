package com.sdp.movemeet.utility;
import android.util.Log;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import com.sdp.movemeet.models.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ActivitiesUpdaterTest {
    //NB: this test works only if the DB is not empty
    @Test
    public void updatingListActivitiesUpdatesCorrectly() throws InterruptedException {
        ActivitiesUpdater.clearLocalActivities();
        ActivitiesUpdater.updateListActivities(task -> {
            //Might be interesting to compare size of db with size of local activities after update
            ArrayList<Activity> activities = ActivitiesUpdater.getActivities();
            Log.d("updater test","first assert");
            assertThat(activities.isEmpty(), is(false));
            ActivitiesUpdater.updateListActivities(task1 -> {
                assertThat(activities, is(ActivitiesUpdater.getActivities()));
                Log.d("updater test","second assert");
            });
        });
        Thread.sleep(3000);
    }
}
