package com.sdp.movemeet.utility;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.sdp.movemeet.models.Activity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DistanceCalculator {
    // private final Double EARTH_RADIUS = 6.3781 * Math.pow(10, 3);

    // This constant is more ~canonical~
    private final static Double EARTH_RADIUS = 6.371 * Math.pow(10, 3);
    private Double userLatitude, userLongitude;
    private ArrayList<Pair> activityDistanceMap;
    private boolean sorted;

    /**
     * Called in MainMapFragment to display the closest activities to the user.
     * @param userLatitude the latitude of the current location of the user
     * @param userLongitude the longitude of the current location of the user
     */
    public DistanceCalculator(Double userLatitude, Double userLongitude) {
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        activityDistanceMap = new ArrayList<Pair>();
    }

    /**
     * Called in MainMapFragment to update the list of the closest activities.
     * @param activities the list of all activities available to the user
     */
    public void setActivities(ArrayList<Activity> activities) {
        activityDistanceMap = new ArrayList<Pair>();

        for (Activity activity : activities) {
            activityDistanceMap.add(new Pair(activity, 0.0));
        }

        sorted = false;
    }

    /**
     * Called in MainMapFragment to return all activities.
     */
    public ArrayList<Activity> getAllActivities() {
        if (sorted) {
            ArrayList<Activity> activities = new ArrayList<Activity>();
            for (Pair pair : activityDistanceMap) {
                activities.add(pair.getKey());
            }

            return activities;
        } else {
            return null;
        }
    }

    /**
     * Called in MainMapFragment to return the list of N closest activities.
     * @param n the number of activities to return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Activity> getTopActivities(Integer n) {
        if (sorted) {
            if (n >= activityDistanceMap.size()) {
                return getAllActivities();
            } else {
                Stream<Activity> topActivitiesStream = activityDistanceMap.subList(0, n).stream().map(Pair::getKey);
                return topActivitiesStream.collect(Collectors.toCollection(ArrayList::new));
            }
        } else {
            return null;
        }
    }

    /**
     * Called in MainMapFragment to return the list of activities that fall in a given radius.
     * @param radius the radius (in km) around the current location of the user
     */
    public ArrayList<Activity> getActivitiesInRadius(Double radius) {
        if (sorted) {
            ArrayList<Activity> topActivities = new ArrayList<Activity>();

            int i = 0;
            //System.out.println(activityDistanceMap.get(i).getValue());
            while (activityDistanceMap.get(i).getValue() <= radius) {
                topActivities.add(activityDistanceMap.get(i).getKey());
                i++;
            }
            return topActivities;
        } else {
            return null;
        }
    }

    /**
     * Called in MainMapFragment to compute the distances between the user and the activities.
     */
    public void calculateDistances() {
        for (Pair pair : activityDistanceMap) {
            pair.setValue(calculateDistance(
                    userLatitude, userLongitude,
                    pair.getKey().getLatitude(), pair.getKey().getLongitude()
            ));
        }
    }

    /**
     * Called in MainMapFragment to sort the list of provided activities by distance to the user.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort() {
        activityDistanceMap.sort(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        sorted = true;
    }

    /**
     * Called locally to verify that the list of activities is sorted by distance to the user.
     */
    public boolean isSorted() {
        return sorted;
    }

    /**
     * Called locally to compute the distance between two geographical points.
     * @param lat1 the latitude of the first point (the user)
     * @param lng1 the longitude of the first point (the user)
     * @param lat2 the latitude of the second point (the activity)
     * @param lng2 the longitude of the second point (the activity)
     */
    public static Double calculateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        Double p = Math.PI / 180;

        //Double a =  Math.pow(Math.sin((lat2 - lat1)/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lng2 - lng1)/2),2);
        Double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lng2 - lng1) * p)) / 2;

        return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(a));
    }

    /**
     * Represents a mapping of activities (keys) to their distances (values).
     * Allows for sorting activities by their distances relative to the user.
     */
    public class Pair {
        private Activity key;
        private Double value;

        public Pair(Activity key, Double value) {
            this.key = key;
            this.value = value;
        }

        public Activity getKey() {
            return this.key;
        }

        public Double getValue() {
            return this.value;
        }

        // public void setKey(Activity key){ this.key = key; }
        public void setValue(Double value) {
            this.value = value;
        }
    }
}