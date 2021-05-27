package com.sdp.movemeet.models;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class ActivityTest {
    private final static String DUMMY_ACTIVITY_ID = "12345";
    private final static String DUMMY_ORGANISATOR_ID = "1";
    private final static String DUMMY_TITLE = "title";
    private final static int DUMMY_NUMBER_PARTICIPANT = 2;
    private final static ArrayList<String> DUMMY_PARTICIPANTS_ID = new ArrayList<String>();
    private final static double DUMMY_LONGITUDE = 2.45;
    private final static double DUMMY_LATITUDE = 3.697;
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_DOCUMENT_PATH = "documentPath";
    private final static Date DUMMY_DATE = new Date(2021, 11, 10, 1, 10);
    private final static Date DUMMY_CREATION_DATE = new Date();
    private final static double DUMMY_DURATION = 10.4;
    private final static Sport DUMMY_SPORT = Sport.Running;
    private final static String DUMMY_ADDRESS = "address";

    private Activity activity;

//    @Before
//    public void setUp() {
//        // Creating a fake activity
//        activity = ActivityTest.createFakeActivity();
//    }


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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE
        );
    }

    @Test
    public void activityConstructorCorrect(){

        activity = ActivityTest.createFakeActivity();

        assertEquals(activity.getActivityId(), DUMMY_ACTIVITY_ID);
        assertEquals(activity.getOrganizerId(), DUMMY_ORGANISATOR_ID);
        assertEquals(activity.getTitle(), DUMMY_TITLE);
        assertEquals(activity.getNumberParticipant(), DUMMY_NUMBER_PARTICIPANT);
        assertEquals(activity.getParticipantId(), DUMMY_PARTICIPANTS_ID);
        assertEquals(activity.getLongitude(), DUMMY_LONGITUDE, 0.1);
        assertEquals(activity.getLatitude(), DUMMY_LATITUDE, 0.1);
        assertEquals(activity.getDescription(), DUMMY_DESCRIPTION);
        assertEquals(activity.getDocumentPath(), DUMMY_DOCUMENT_PATH);
        assertEquals(activity.getDate(), DUMMY_DATE);
        assertEquals(activity.getDuration(), DUMMY_DURATION, 0.1);
        assertEquals(activity.getSport(), DUMMY_SPORT);
        assertEquals(activity.getAddress(), DUMMY_ADDRESS);
        assertEquals(activity.getCreatedAt(), DUMMY_CREATION_DATE);

        activity = null;
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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);
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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityTitleEmpty(){

        activity = ActivityTest.createFakeActivity();
        activity.setTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityParticipantsEmpty(){

        activity = ActivityTest.createFakeActivity();
        activity.setNumberParticipant(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityDescriptionEmpty(){

        activity = ActivityTest.createFakeActivity();
        activity.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityDateEmpty(){

        activity = ActivityTest.createFakeActivity();
        activity.setDate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityDurationEmpty(){

        activity = ActivityTest.createFakeActivity();
        activity.setDuration(0);
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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                null,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public  void activityConstructorActivityAddressEmpty(){

        activity = ActivityTest.createFakeActivity();
        activity.setAddress(null);
    }

    @Test
    public void activitySetCorrect() {

        activity = ActivityTest.createFakeActivity();

        activity.setTitle("Hello");
        activity.setNumberParticipant(3);
        activity.setLongitude(853.4);
        activity.setLatitude(3456.6);
        activity.setDescription("Running");
        activity.setDocumentPath("documentPath");
        Date newDate = new Date(2021, 4, 2, 2, 20);
        activity.setDate(newDate);
        activity.setDuration(20.4);
        activity.setAddress("EPFL");
        Date createAt = new Date(2022, 11, 10, 1, 10);
        activity.setCreatedAt(createAt);

        activity.setParticipantRecordings(new HashMap<String, GPSPath>());

        assertEquals(activity.getCreatedAt(), createAt);
        assertEquals(activity.getTitle(), "Hello");
        assertEquals(activity.getNumberParticipant(), 3);
        assertEquals(activity.getLongitude(), 853.4, 0.1);
        assertEquals(activity.getLatitude(), 3456.6, 0.1);
        assertEquals(activity.getDescription(), "Running");
        assertEquals(activity.getDocumentPath(), "documentPath");
        assertEquals(activity.getDate(), newDate);
        assertEquals(activity.getDuration(), 20.4, 0.1);
        assertEquals(activity.getAddress(), "EPFL");

        assertNotNull(activity.getParticipantRecordings());

        activity = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void activitySetFailIfNull() {

        activity = ActivityTest.createFakeActivity();

        activity.setTitle(null);
        activity.setNumberParticipant(3);
        activity.setLongitude(853.4);
        activity.setLatitude(3456.6);
        activity.setDescription("Running");
        activity.setDescription("documentPath");
        Date newDate = new Date(2021, 4, 2, 2, 20);
        activity.setDate(newDate);
        activity.setDuration(20.4);
        activity.setAddress("EPFL");

    }

    @Test(expected = IllegalArgumentException.class)
    public void activityAddParticipantNull(){

        activity = ActivityTest.createFakeActivity();

        activity.addParticipantId(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void activityAddSameParticipantNull(){

        activity = ActivityTest.createFakeActivity();

        String user = "Caro";

        activity.addParticipantId(user);
        activity.addParticipantId(user);

    }

    @Test
    public void activityAddCorrect(){

        activity = ActivityTest.createFakeActivity();

        String user1 = "BOB";
        String user2 = "bob";
        activity.addParticipantId(user1);
        activity.addParticipantId(user2);

        assertEquals(2, activity.getParticipantId().size());
        activity = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void activityRemoveParticipantNull(){

        activity = ActivityTest.createFakeActivity();

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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

        String user1 = "BOB";
        String user2 = "bob";

        activity.addParticipantId(user1);
        activity.addParticipantId(user2);

        activity.removeParticipantId(user1);

        assertEquals(1, activity.getParticipantId().size());
        activity = null;
    }

    @Test
    public void activityToStringCorrect(){

        activity = ActivityTest.createFakeActivity();

        assertEquals(activity.toString(), "ActivityId:" + DUMMY_ACTIVITY_ID + "\nOrganizerId" + DUMMY_ORGANISATOR_ID + "\nTitle:" + DUMMY_TITLE + "\nNumberParticipant:" + DUMMY_NUMBER_PARTICIPANT +
                "\nParticipantId:" + DUMMY_PARTICIPANTS_ID + "\nLongitude:" + DUMMY_LONGITUDE + "\nLatitude:" + DUMMY_LATITUDE + "\nDescription:" + DUMMY_DESCRIPTION + //"\nDocumentPath:" + DUMMY_DOCUMENT_PATH +
                "\nDate:" + DUMMY_DATE + "\nDuration:" + DUMMY_DURATION + "\nSport:" + DUMMY_SPORT + "\nAddress:" + DUMMY_ADDRESS + "\nCreated at:" + DUMMY_CREATION_DATE);
        activity = null;

    }

    @Test
    public void activityEqualsCorrect(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();

        assertEquals(true, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;

    }

    @Test
    public void activityEqualsNull(){

        Activity activity1 = ActivityTest.createFakeActivity();

        assertEquals(false, activity1.equals(null));

        activity1 = null;

    }

    @Test
    public void activityEqualsClass(){

        Activity activity1 = ActivityTest.createFakeActivity();

        assertEquals(false, activity1.equals("1"));

        activity1 = null;

    }




    @Test
    public void activityEqualsDifferentActivity1(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = new Activity(
                "1",
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;

    }

    @Test
    public void activityEqualsDifferentActivity2(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = new Activity(
                DUMMY_ACTIVITY_ID,
                "10",
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity3(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setTitle("titleBis");


        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity4(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setNumberParticipant(1);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity5(){

        Activity activity1 = ActivityTest.createFakeActivity();

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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity6(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setLongitude(DUMMY_LONGITUDE+1);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity7(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setLatitude(DUMMY_LATITUDE+1);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity8(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setDescription("AH");

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity9(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setDate(new Date(2022,3,26));

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity10(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setDuration(DUMMY_DURATION+1);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;

    }

    @Test
    public void activityEqualsDifferentActivity11(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                DUMMY_PARTICIPANTS_ID,
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                Sport.Badminton,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


    }

    @Test
    public void activityEqualsDifferentActivity12(){

        Activity activity1 = ActivityTest.createFakeActivity();

        Activity activity2 = ActivityTest.createFakeActivity();
        activity2.setAddress("addressBis");

        assertEquals(false, activity1.equals(activity2));

        activity1 = null;
        activity2 = null;


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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

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
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

        activity.setAddress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void activityLimitParticipants(){
        Activity activity = new Activity(
                DUMMY_ACTIVITY_ID,
                DUMMY_ORGANISATOR_ID,
                DUMMY_TITLE,
                DUMMY_NUMBER_PARTICIPANT,
                new ArrayList<String>(),
                DUMMY_LONGITUDE,
                DUMMY_LATITUDE,
                DUMMY_DESCRIPTION,
                DUMMY_DOCUMENT_PATH,
                DUMMY_DATE,
                DUMMY_DURATION,
                DUMMY_SPORT,
                DUMMY_ADDRESS,
                DUMMY_CREATION_DATE);

        String user1 = "Alice";
        String user2 = "Bob";
        String user3 = "Caroline";

        activity.addParticipantId(user1);
        activity.addParticipantId(user2);
        activity.addParticipantId(user3);
    }

    @Test
    public void equalsReturnsTrueOnSameObject() {
        Activity act = createFakeActivity();

        assert(act.equals(act));
    }

    @Test
    public void addParticipantIDThrowsIllegalArgumentExceptionOnAlreadyAddedParticipant() {
        Activity act = createFakeActivity();
        act.setNumberParticipant(3);
        String id = "id";
        act.addParticipantId(id);
        assertThrows(IllegalArgumentException.class, () -> {
            act.addParticipantId(id);
        });
    }

    @Test
    public void testSerializability()  {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(0,0));
        list.add(new LatLng(1, 1));
        long time = 100000;
        GPSPath path = new GPSPath(list, time);

        HashMap<String, GPSPath> recordings = new HashMap<String, GPSPath>();
        recordings.put("id", path);
        Activity act = createFakeActivity();
        act.setParticipantRecordings(recordings);

        Activity activity;

        boolean exceptionThrown = false;
        try {
            byte[] data = SerializationUtils.serialize(act);
            activity = SerializationUtils.deserialize(data);
            assertNotNull(activity);
            assert(activity.equals(act));
        } catch (Exception e) {
            exceptionThrown = true;
        }

        assert (exceptionThrown == false);


    }
}