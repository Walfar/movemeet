package com.sdp.movemeet;

import com.sdp.movemeet.Activity.Activity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DistanceCalculatorTest {
    @Test
    public void constructorIsCorrect() {
        DistanceCalculator dc = new DistanceCalculator(0.,0.);
    }

    @Test
    public void settingAndGettingIsCorrect() {
        DistanceCalculator dc = new DistanceCalculator(0.,0.);
        ArrayList<Activity> activityArrayList = new ArrayList<Activity>();

        final String DUMMY_ACTIVITY_ID = "12345";
        final String DUMMY_ORGANISATOR_ID = "1";
        final String DUMMY_TITLE = "title";
        final int DUMMY_NUMBER_PARTICIPANT = 2;
        final ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>();
        final double DUMMY_LONGITUDE = 2.45;
        final double DUMMY_LATITUDE = 3.697;
        final String DUMMY_DESCRIPTION = "description";
        final Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
        final double DUMMY_DURATION = 10.4;
        final Sport DUMMY_SPORT = Sport.Running;
        final String DUMMY_ADDRESS = "address";

        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE + 30,
                DUMMY_LATITUDE + 30,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        Activity activity2 = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE + 0,
                DUMMY_LATITUDE + 0,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activityArrayList.add(activity);
        activityArrayList.add(activity2);

        dc.setActivities(activityArrayList);
        dc.sortActivities();

        assertNotEquals(dc.getAllActivities(), activityArrayList);

        assertEquals(dc.getTopActivities(1).get(0), activity2);
    }

    @Test
    public void distanceCalculationIsCorrect() {
        DistanceCalculator dc = new DistanceCalculator(0.,0.);
        System.out.println(dc.calculateDistance(0., 0., 2.45, 3.697));
        System.out.println(dc.calculateDistance(0., 0., 2.45 + 3, 3.697 + 3));
    }

    @Test
    public void unsorted() {
        DistanceCalculator dc = new DistanceCalculator(0.,0.);
        ArrayList<Activity> activityArrayList = new ArrayList<Activity>();

        final String DUMMY_ACTIVITY_ID = "12345";
        final String DUMMY_ORGANISATOR_ID = "1";
        final String DUMMY_TITLE = "title";
        final int DUMMY_NUMBER_PARTICIPANT = 2;
        final ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>();
        final double DUMMY_LONGITUDE = 2.45;
        final double DUMMY_LATITUDE = 3.697;
        final String DUMMY_DESCRIPTION = "description";
        final Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
        final double DUMMY_DURATION = 10.4;
        final Sport DUMMY_SPORT = Sport.Running;
        final String DUMMY_ADDRESS = "address";

        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE + 30,
                DUMMY_LATITUDE + 30,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        Activity activity2 = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE + 0,
                DUMMY_LATITUDE + 0,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activityArrayList.add(activity);
        activityArrayList.add(activity2);

        dc.setActivities(activityArrayList);

        assertEquals(null, dc.getAllActivities());

        assertEquals(null, dc.getTopActivities(1));

        assertEquals(false, dc.isSorted());

    }

    @Test
    public void empty() {
        DistanceCalculator dc = new DistanceCalculator(0.,0.);
        ArrayList<Activity> activityArrayList = new ArrayList<Activity>();

        dc.setActivities(activityArrayList);

        dc.sortActivities();

        assertEquals(false, dc.isSorted());

    }
}