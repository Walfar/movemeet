package com.sdp.movemeet;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private final static String DUMMY_FIRST_NAME = "fist name";
    private final static String DUMMY_LAST_NAME = "last name";
    private final static String DUMMY_EMAIL = "firstname.lastname@gmail.com";
    private final static String DUMMY_PHONE_NUMBER = "+41123456789";
    private final static String DUMMY_IMAGE_ID = "1234";
    private final static String DUMMY_DESCRIPTION = "description";

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfFirstNameNull(){
        User user = new User(null, DUMMY_LAST_NAME, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfLastNameNull(){
        User user = new User(DUMMY_FIRST_NAME, null, DUMMY_EMAIL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfEmailNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNumberNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, null, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfImageNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, null, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfDescriptionNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNumberSize(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, "1234", DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test
    public void userGetFirstName(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL);
        assertEquals(DUMMY_FIRST_NAME, user.getFirstName());
    }

    @Test
    public void userGetLastName(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL);
        assertEquals(DUMMY_LAST_NAME, user.getLastName());
    }

    @Test
    public void userGetEmail(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL);
        assertEquals(DUMMY_EMAIL, user.getEmail());
    }

    @Test
    public void userGetPhoneNumber(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_PHONE_NUMBER, user.getPhoneNumber());
    }

    @Test
    public void userGetImageId(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_IMAGE_ID, user.getImageId());
    }

    @Test
    public void userGetDescription(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_DESCRIPTION, user.getDescription());
    }
}
