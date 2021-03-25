package com.sdp.movemeet.Activity;

import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Sport;

import org.junit.*;

import java.util.ArrayList;
import java.util.Date;

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


    public static Activity createFakeActivity() {
        return new Activity(
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
                DUMMY_ADDRESS
        );
    }

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
        assertEquals(activity.getOrganizerId(), DUMMY_ORGANISATOR_ID);
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

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityOrganizerEmpty(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                null,
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

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityTitleEmpty(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                null,
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

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityParticipantsEmpty(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                0,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityDescriptionEmpty(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                null,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityDateEmpty(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                null,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityDurationEmpty(){
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
                0,
                DUMMY_SPORT,
                DUMMY_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivitySportEmpty(){
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
                null,
                DUMMY_ADDRESS);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityAddressEmpty(){
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
                null);
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

        assertEquals(2, activity.getParticipantId().size());
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

    @Test
    public void activityRemoveCorrect(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                new ArrayList<String>(),
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

        assertEquals(1, activity.getParticipantId().size());
    }

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

        assertEquals(activity.toString(), "ActivityId:" + DUMMY_ACTIVITY_ID + "\nOrganizerId" + DUMMY_ORGANISATOR_ID + "\nTitle:" + DUMMY_TITLE + "\nNumberParticipant:" + DUMMY_NUMBER_PARTICIPANT +
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

        assertEquals(true, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsNull(){
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

        assertEquals(false, activity1.equals(null));

    }

    @Test
    public void activityEqualsClass(){
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

        assertEquals(false, activity1.equals("1"));

    }




    @Test
    public void activityEqualsDifferentActivity1(){
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

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity2(){
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
                "10",
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

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity3(){
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
                "titleBis",
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity4(){
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
                1,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity5(){
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

        ArrayList<String> DUMMY_PARTICIPANTS_ID_BIS = new ArrayList<String>();
        DUMMY_PARTICIPANTS_ID_BIS.add("bobx");

        Activity activity2 = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID_BIS,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity6(){
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
                DUMMY_LONGITUDE+1,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity7(){
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
                DUMMY_LATITUDE+1,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity8(){
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
                "AH",
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity9(){
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
                new Date(2022,3,26),
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity10(){
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
                DUMMY_DURATION+1,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity11(){
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
                Sport.Badminton,
                DUMMY_ADDRESS);

        assertEquals(false, activity1.equals(activity2));

    }

    @Test
    public void activityEqualsDifferentActivity12(){
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
                "addressBis");

        assertEquals(false, activity1.equals(activity2));

    }

    @Test(expected = IllegalArgumentException.class)
    public void activityChangeDuration(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                new ArrayList<String>(),
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activity.setDuration(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activityChangeDescription(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                new ArrayList<String>(),
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activity.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activityChangeDate(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                new ArrayList<String>(),
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activity.setDate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activityChangeParticipant(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                new ArrayList<String>(),
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activity.setNumberParticipant(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activityChangeAddress(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                new ArrayList<String>(),
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS);

        activity.setAddress(null);
    }


}