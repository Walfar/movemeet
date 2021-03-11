package com.sdp.movemeet;

import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.PrimitiveIterator;

import static org.junit.Assert.assertEquals;

public class ActivityTest {
    private final static String DUMMY_ACTIVITY_ID = "12345";
    private final static String DUMMY_ORGANISATOR_ID = "1";
    private final static String DUMMY_TITLE = "title";
    private final static int DUMMY_NUMBER_PARTICIPANT = 2;
    private final static ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>();
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
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(activity.getActivityId(), DUMMY_ACTIVITY_ID);
        assertEquals(activity.getOrganisatorId(), DUMMY_ORGANISATOR_ID);
        assertEquals(activity.getTitle(), DUMMY_TITLE);
        assertEquals(activity.getNumberParticipant(), DUMMY_NUMBER_PARTICIPANT);
        assertEquals(activity.getParticipantId(), DUMMY_PARTICIPANTS_ID);
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
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
    }

    @Test
    public void activitySetCorrect() {
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activity.setTitle("Hello");
        activity.setNumberParticipant(3);
        activity.setLongitude(853.4);
        activity.setLatitude(3456.6);
        activity.setDescription("Running");
        Date newDate = new Date(2021, 4, 2, 2, 20);
        activity.setDate(newDate);
        activity.setDuration(20.4);
        activity.setAddress("EPFL");

        assertEquals(activity.getTitle(), "Hello");
        assertEquals(activity.getNumberParticipant(), 3);
        assertEquals(activity.getLongitude(), 853.4, 0.1);
        assertEquals(activity.getLatitude(), 3456.6, 0.1);
        assertEquals(activity.getDescription(), "Running");
        assertEquals(activity.getDate(), newDate);
        assertEquals(activity.getDuration(), 20.4, 0.1);
        assertEquals(activity.getAddress(), "EPFL");

    }

    @Test(expected = IllegalArgumentException.class)
    public void activitySetFailIfNull() {
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activity.setTitle(null);
        activity.setNumberParticipant(3);
        activity.setLongitude(853.4);
        activity.setLatitude(3456.6);
        activity.setDescription("Running");
        Date newDate = new Date(2021, 4, 2, 2, 20);
        activity.setDate(newDate);
        activity.setDuration(20.4);
        activity.setAddress("EPFL");

    }

    @Test(expected = IllegalArgumentException.class)
    public void activityAddParticipantNull(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
        activity.addParticipantId(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void activityAddSameParticipantNull(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
        String user = "Bob";
        activity.addParticipantId(user);
        activity.addParticipantId(user);

    }

    @Test
    public void activityAddCorrect(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
        String user1 = "BOB";
        String user2 = "bob";
        activity.addParticipantId(user1);
        activity.addParticipantId(user2);

        assertEquals(activity.getParticipantId().size(), 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activityRemoveParticipantNull(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
        activity.removeParticipantId(null);

    }

    /*@Test
    public void activityRemoveCorrect(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        String user1 = "BOB";
        String user2 = "bob";

        activity.addParticipantId(user1);
        activity.addParticipantId(user2);

        activity.removeParticipantId(user1);

        assertEquals(activity.getParticipantId().size(), 2);
    }*/

    @Test
    public void activityToStringCorrect(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(activity.toString(), "ActivityId:" + DUMMY_ACTIVITY_ID + "\nOrganisatorId" + DUMMY_ORGANISATOR_ID + "\nTitle:" + DUMMY_TITLE + "\nNumberParticipant:" + DUMMY_NUMBER_PARTICIPANT +
                "\nParticipantId:" + DUMMY_PARTICIPANTS_ID + "\nLongitude:" + DUMMY_LONGITUDE + "\nLatitude:" + DUMMY_LATITUDE + "\nDescription:" + DUMMY_DESCRIPTION +
                "\nDate:" + DUMMY_DATE + "\nDuration:" + DUMMY_DURATION + "\nSport:" + DUMMY_SPORT + "\nAddress:" + DUMMY_ADDRESS);

    }

    @Test
    public void activityEqualsCorrect(){
        Activity activity1 = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
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
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(activity1.equals(activity2), true);

    }


    @Test
    public void activityEqualsDifferentActivity(){
        Activity activity1 = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        Activity activity2 = new Activity(
                "1",
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(activity1.equals(activity2), false);

    }





}