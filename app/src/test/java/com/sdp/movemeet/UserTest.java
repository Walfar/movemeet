package com.sdp.movemeet;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private final static String DUMMY_FIRST_NAME = "fist name";
    private final static String DUMMY_LAST_NAME = "last name";
    private final static String DUMMY_EMAIL = "firstname.lastname@gmail.com";
    private final static String DUMMY_USER_ID = "1";
    private final static String DUMMY_PHONE_NUMBER = "+41123456789";
    private final static String DUMMY_IMAGE_ID = "1234";
    private final static String DUMMY_DESCRIPTION = "description";

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfFirstNameNull(){
        User user = new User(null, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfLastNameNull(){
        User user = new User(DUMMY_FIRST_NAME, null, DUMMY_EMAIL, DUMMY_USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfEmailNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, null, DUMMY_USER_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfUserIdNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNumberNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, null, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfImageNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, null, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfDescriptionNull(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNumberSize(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, "1234", DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test
    public void userGetFirstName(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID);
        assertEquals(DUMMY_FIRST_NAME, user.getFirstName());
    }

    @Test
    public void userGetLastName(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID);
        assertEquals(DUMMY_LAST_NAME, user.getLastName());
    }

    @Test
    public void userGetEmail(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID);
        assertEquals(DUMMY_EMAIL, user.getEmail());
    }

    @Test
    public void userGetUserId(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID);
        assertEquals(DUMMY_USER_ID, user.getIdUser());
    }


    @Test
    public void userGetPhoneNumber(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_PHONE_NUMBER, user.getPhoneNumber());
    }

    @Test
    public void userGetImageId(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_IMAGE_ID, user.getImageId());
    }

    @Test
    public void userGetDescription(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_DESCRIPTION, user.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetUserIdEmpty(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setIdUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetPhoneNumberEmpty(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setPhoneNumber(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetDescriptionEmpty(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetImageIdEmpty(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setImageId(null);
    }

    @Test
    public void userSetUserId(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setIdUser("2");
        assertEquals(user.getIdUser(), "2");
    }

    @Test
    public void userSetPhoneNumber(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setPhoneNumber("000000000000");
        assertEquals(user.getPhoneNumber(), "000000000000");
    }

    @Test
    public void userSetDescription(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setDescription("Hello");
        assertEquals(user.getDescription(), "Hello");
    }

    @Test
    public void userSetImageId(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        user.setImageId("image.png");
        assertEquals(user.getImageId(), "image.png");
    }

    @Test
    public void userToStringCorrect(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(user.toString(), "FistName:" + DUMMY_FIRST_NAME + "\nLastName:" + DUMMY_LAST_NAME + "\nEmail:" + DUMMY_EMAIL + "\nId:" + DUMMY_USER_ID +
                "\nPhoneNumber:" + DUMMY_PHONE_NUMBER + "\nImageId:" + DUMMY_IMAGE_ID + "\nDescription:" + DUMMY_DESCRIPTION);
    }

    @Test
    public void userEqualCorrect(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(user1.equals(user2), true);
    }


    @Test
    public void userEqualDifferentUser1(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User("Georges", DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser2(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User(DUMMY_FIRST_NAME, "CANDEA", DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser3(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, "email", DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser4(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, "2", DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser5(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, "+41123456780", DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser6(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, "2345", DUMMY_DESCRIPTION);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser7(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        User user2 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, "BlaBla");

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualNull(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(user1.equals(null), false);
    }

    @Test
    public void userEqualSame(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(user1.equals(user1), true);
    }

    @Test
    public void userEqualDiffClass(){
        User user1 = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, DUMMY_PHONE_NUMBER, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
        assertEquals(user1.equals(1), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingFirstName(){
        User user = new User(null, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, "1234", DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingLastName(){
        User user = new User(DUMMY_FIRST_NAME, null, DUMMY_EMAIL, DUMMY_USER_ID, "1234", DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingEmail(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, null, DUMMY_USER_ID, "1234", DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingID(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, null, "1234", DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingPhone(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, null, DUMMY_IMAGE_ID, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingImage(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, "1234", null, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingDescription(){
        User user = new User(DUMMY_FIRST_NAME, DUMMY_LAST_NAME, DUMMY_EMAIL, DUMMY_USER_ID, "1234", DUMMY_IMAGE_ID, null);
    }




}
