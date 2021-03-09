package com.sdp.movemeet.Activity;

import com.google.firebase.firestore.DocumentReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ActivityTest {

    public static Activity fakeActivity;
    public static Date fakeTime;

    public static int fakeSport;
    public static int fakeParticipants;
    public static int fakeMax;

    @Mock
    DocumentReference docRef;

    @Mock
    DocumentReference docRef_2;

    @Before
    public void genActivity() {
        Random rand = new Random();

        fakeSport = rand.nextInt();
        fakeParticipants = rand.nextInt();
        fakeMax = rand.nextInt();
        fakeTime = new Date();
        fakeActivity = new Activity("testID",
                fakeSport,
                fakeTime,
                fakeMax,
                fakeParticipants);

    }

    @Test
    public void getSportTypeReturnsCorrectValue() {
        assertThat(fakeActivity.getSportType(), is(fakeSport));
    }

    @Test
    public void getHostIDReturnsCorrectValue() {
        assert (fakeActivity.getHostID().equals("testID"));
    }

    @Test
    public void getMaxParticipantsReturnsCorrectValue() {
        assert (fakeActivity.getMaxParticipants() == fakeMax);
    }

    @Test
    public void getTimeReturnsCorrectValue() {
        assert (fakeActivity.getTime().equals(fakeTime));
    }

    @Test
    public void getAndSetBackendRefWork() {
        assert (fakeActivity.getBackendRef() == null);
        fakeActivity.setBackendRef(docRef);
        fakeActivity.setBackendRef(docRef_2);
        assert (fakeActivity.getBackendRef() == docRef);
    }

}
