package com.sdp.movemeet.models;

import com.google.android.gms.maps.model.LatLng;
import com.sdp.movemeet.utility.DistanceCalculator;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a recorded GPS path; includes a list of recorded
 * locations along with some calculated stats for said list
 */
public class GPSPath implements Serializable {

    private List<LatLng> path;
    private long time;
    private transient float averageSpeed;
    private transient float distance;

    /**
     * Returns a new GPSPath, having
     * computed stats for the recorded GPS points.
     * @param path
     */
    public GPSPath(List<LatLng> path, long time) {
        if (path == null) throw new IllegalArgumentException();
        if (time < 0) throw new IllegalArgumentException();

        this.path = path;
        this.time = time;
        this.distance = -1;
        this.averageSpeed = -1;
    }

    /**
     * Returns the path saved in this GPSRecording as a list of coordinates
     * @return a List<LatLng> containing all points in this path, in order
     */
    public List<LatLng> getPath() {
        return this.path;
    }

    /**
     * @return the time taken to travel over this path, in milliseconds
     */
    public long getTime() {
        return this.time;
    }

    /**
     * @return the total distance covered by this path, in meters
     */
    public float getDistance() {
        if (this.distance < 0) this.distance = computeTotalDistance(this.path);
        return this.distance;
    }

    /**
     * @return the average speed over this path, in km/h
     */
    public float getAverageSpeed() {
        if (this.averageSpeed < 0) this.averageSpeed = computeAverageSpeed(getDistance(), this.time);
        return averageSpeed;
    }

    /**
     * Replaces this GPSPath's list of coordinates with a new one.
     * Also triggers a recalculation of the average speed and total distance.
     * @param newPath the new path to store in this GPSPath
     */
    public void setPath(List<LatLng> newPath) {
        if (path == null) throw new IllegalArgumentException();
        this.path = newPath;

        this.distance = computeTotalDistance(this.path);
        if (this.time >= 0) this.averageSpeed = computeAverageSpeed(this.distance, this.time);
    }

    /**
     * Replaces this GPSPath's time attribute with a new one.
     * Also triggers a recalculation of the average speed.
     * @param newTime
     */
    public void setTime(long newTime) {
        if (newTime < 0) throw new IllegalArgumentException();
        this.time = newTime;

        if (this.distance >= 0) this.averageSpeed = computeAverageSpeed(this.distance, this.time);
    }

    /**
     * Computes the average speed of an object having travelled a certain distance over a certain
     * time.
     * @param distance The distance travelled, in meters
     * @param time The time it took to travel, in milliseconds
     * @return the average speed (in km/h)
     */
    public static float computeAverageSpeed(float distance, long time) {
        if (time <= 0) throw new IllegalArgumentException();
        if (distance <= 0) return 0;

        return (distance / 1000) / (time / 3600);
    }

    /**
     * Computes the total distance of a path, i.e. the total sum of
     * the pairwise distances of the points.
     * @param path a List of coordinates to compute the distance for
     * @return the total distance of the path, in meters.
     */
    public static float computeTotalDistance(List<LatLng> path) {
        if (path == null) throw new IllegalArgumentException();
        if (path.size() < 2) return 0;

        float dist = 0;

        for (int i = 1; i < path.size(); i++) {
            LatLng first = path.get(i - 1);
            LatLng second = path.get(i);

            dist += DistanceCalculator.calculateDistance(first.latitude, first.longitude, second.latitude, second.longitude);
        }

        return dist;
    }


}
