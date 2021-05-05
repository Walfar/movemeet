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
    private final Double EARTH_RADIUS = 6.371 * Math.pow(10, 3);
    private Double userLatitude, userLongitude;
    private ArrayList<Pair> activityDistanceMap;
    private boolean sorted;

    public DistanceCalculator(Double userLatitude, Double userLongitude) {
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        activityDistanceMap = new ArrayList<Pair>();
    }

    public void setActivities(ArrayList<Activity> activities) {
        for (Activity activity : activities) {
            activityDistanceMap.add(new Pair(activity, 0.0));
        }

        sorted = false;
    }

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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Activity> getTopActivities(Integer n) {
        if (sorted) {
            Stream<Activity> topActivitiesStream = activityDistanceMap.subList(0, n).stream().map(Pair::getKey);
            return topActivitiesStream.collect(Collectors.toCollection(ArrayList::new));

        } else {
            return null;
        }
    }

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


    public void calculateDistances() {
        for (Pair pair : activityDistanceMap) {
            pair.setValue(calculateDistance(
                    userLatitude, userLongitude,
                    pair.getKey().getLatitude(), pair.getKey().getLongitude()
            ));
        }
    }

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

    public boolean isSorted() {
        return sorted;
    }

    public Double calculateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        // Double p = 0.017453292519943295;
        Double p = Math.PI / 180;

        //Double a =  Math.pow(Math.sin((lat2 - lat1)/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lng2 - lng1)/2),2);
        Double a = 0.5 - Math.cos((lat2 - lat1) * p) / 2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lng2 - lng1) * p)) / 2;

        return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(a));
    }

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
