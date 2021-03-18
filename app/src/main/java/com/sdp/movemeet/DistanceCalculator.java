package com.sdp.movemeet;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.sdp.movemeet.Activity.Activity;
import java.util.ArrayList;
import java.util.Collections;

public class DistanceCalculator {
    private final Double EARTH_RADIUS = 6.3781 * Math.pow(10, 3);
    private Double userLatitude, userLongitude;
    private ArrayList<Activity> activities = null;
    private boolean sorted;

    public DistanceCalculator(Double userLatitude, Double userLongitude) {
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = new ArrayList<Activity>(activities);
        sorted = false;
    }

    public ArrayList<Activity> getAllActivities() {
        if (sorted) {
            return activities;
        } else {
            return null;
        }
    }

    public ArrayList<Activity> getTopActivities(Integer n) {
        if (sorted) {
            ArrayList<Activity> sublist = new ArrayList<Activity> (activities.subList(0, n));
            return sublist;
        } else {
            return null;
        }
    }

    /*
    public ArrayList<Activity> getActivitiesInRadius(Double radius) {
        return null;
    }
    */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortActivities() {
        if (activities.isEmpty()) { // == null
            sorted = false;
        } else {
            activities.sort((Activity a1, Activity a2) ->
            calculateDistance(userLatitude, userLongitude, a1.getLatitude(), a1.getLongitude())
            .compareTo(
            calculateDistance(userLatitude, userLongitude, a2.getLatitude(), a2.getLongitude())));
            sorted = true;
        }
    }

    public boolean isSorted() {
        return sorted;
    }

    public Double calculateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(Math.pow(Math.sin((lat2 - lat1)/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lng2 - lng1)/2),2)));
    }
}
