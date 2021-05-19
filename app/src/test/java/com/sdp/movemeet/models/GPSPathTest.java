package com.sdp.movemeet.models;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class GPSPathTest {

    @Test
    public void constructorThrowsIllegalArgumentExceptionOnNullPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            GPSPath path = new GPSPath(null, 1);
        });
    }

    @Test
    public void constructorThrowsIllegalArgumentExceptionOnNegativeTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            GPSPath path = new GPSPath(new ArrayList<LatLng>(), -1);
        });
    }

    @Test
    public void computeAverageSpeedThrowsIllegalArgumentExceptionOnZeroOrNegativeTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            GPSPath.computeAverageSpeed(10.0f, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GPSPath.computeAverageSpeed(10.0f, -1);
        });
    }

    @Test
    public void computeAverageSpeedReturnsZeroOnZeroOrNegativeDistance() {
        assertThat(GPSPath.computeAverageSpeed(0.0f, 10), is(0.0f));

        assertThat(GPSPath.computeAverageSpeed(-10.0f, 10), is(0.0f));
    }

    @Test
    public void computeTotalDistanceThrowsIllegalArgumentExceptionOnNullPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            GPSPath.computeTotalDistance(null);
        });
    }

    @Test
    public void computeTotalDistanceReturnsZeroOnPathWithFewerThanTwoElements() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();

        assertThat(GPSPath.computeTotalDistance(list), is(0.0f));

        list.add(new LatLng(0, 0));

        assertThat(GPSPath.computeTotalDistance(list), is(0.0f));

        list.add(new LatLng(1, 1));

        assertThat(GPSPath.computeTotalDistance(list), not(is(0.0f)));
    }

    @Test
    public void GettersWorkAsExpected() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(0,0));
        list.add(new LatLng(1, 1));

        long time = 10;

        GPSPath path = new GPSPath(list, time);
        assert(path.getTime() == 10);
        assert(path.getPath().equals(list));

        float dist = path.getDistance();
        assert(dist > 0);

        float avgSpeed = path.getAverageSpeed();
        assert(avgSpeed > 0);
    }

    @Test
    public void setTimeThrowsIllegalArgumentExceptionOnNegativeParameter() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(0,0));
        list.add(new LatLng(1, 1));

        long time = 10;

        GPSPath path = new GPSPath(list, time);

        assertThrows(IllegalArgumentException.class, () -> {
            path.setTime(-1);
        });
    }

    @Test
    public void setPathThrowsIllegalArgumentExceptionOnNullPath() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(0,0));
        list.add(new LatLng(1, 1));

        long time = 10;

        GPSPath path = new GPSPath(list, time);

        assertThrows(IllegalArgumentException.class, () -> {
            path.setPath(null);
        });
    }

    @Test
    public void setPathAlsoTriggersAvgSpeedRecomputationOnValidTime() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(0,0));
        list.add(new LatLng(1, 1));

        long time = 10;

        GPSPath path = new GPSPath(list, time);
        float dist = path.getDistance();

        list.add(new LatLng(2, 2));

        path.setPath(list);
        assert(dist != path.getDistance());
    }

    @Test
    public void setTimeAlsoTriggersAvgSpeedRecomputationOnValidDistance() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(0,0));
        list.add(new LatLng(1, 1));

        long time = 100000;

        GPSPath path = new GPSPath(list, time);
        float avgSpeed = path.getAverageSpeed();

        path.setTime(200000);
        assert(avgSpeed != path.getAverageSpeed());
    }
}
