package com.sdp.movemeet;

import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.PrimitiveIterator;

import static org.junit.Assert.assertEquals;

public class ActivityTest {
    private final static String DUMMY_ACTIVITY_ID = "12345";
    private final static String DUMMY_ORGANISATOR = "name";
    private final static String DUMMY_TITLE = "title";
    private final static int DUMMY_NUMBER_PARTICIPANT = 2;
    private final static User user = new User("firstName", "lastName", "name.lastName@gmail.com");
    private final static ArrayList<User> DUMMY_PARTICIPANTS = new ArrayList<User>();
    private final static double DUMMY_LONGITUDE = 2.45;
    private final static double DUMMY_LATITUDE = 3.697;
    private final static String DUMMY_DESCRIPTION = "description";
    private final static Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
    private final static double DUMMY_DURATION = 10.4;
    private final static Sport DUMMY_SPORT = Sport.Running;
    private final static String DUMMY_ADDRESS = "address";


    @Test
    public void activityConstructorCorrect(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(activity.getActivityId(), DUMMY_ACTIVITY_ID);
        assertEquals(activity.getOrganisator(), DUMMY_ORGANISATOR);
        assertEquals(activity.getTitle(), DUMMY_TITLE);
        assertEquals(activity.getNumberParticipant(), DUMMY_NUMBER_PARTICIPANT);
        assertEquals(activity.getLongitude(), DUMMY_LONGITUDE, 0.1);
        assertEquals(activity.getLatitude(), DUMMY_LATITUDE, 0.1);
        assertEquals(activity.getDescription(), DUMMY_DESCRIPTION);
        assertEquals(activity.getDate(), DUMMY_DATE);
        assertEquals(activity.getDuration(), DUMMY_DURATION, 0.1);
        assertEquals(activity.getSport(), DUMMY_SPORT);
        assertEquals(activity.getAddress(), DUMMY_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityIDEmpty(){
        Activity activity = new Activity(
                null,
                DUMMY_ORGANISATOR,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
    }




}