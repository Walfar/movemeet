package com.sdp.movemeet.modelsTest;

import com.sdp.movemeet.models.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private final static String DUMMY_FULL_NAME = "full name";
    private final static String DUMMY_EMAIL = "firstname.lastname@gmail.com";
    private final static String DUMMY_PHONE_NUMBER = "+41123456789";
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_USER_ID = "1";
    private final static String DUMMY_IMAGE_ID = "1234";
    private final static String DUMMY_DOCUMENT_PATH = "documentPath";

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfFullNameNull() {
        User user = new User(null, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfEmailNull() {
        User user = new User(DUMMY_FULL_NAME, null, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, null, DUMMY_DESCRIPTION);
    }


    // DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfUserIdNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, null, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNumberNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, null, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfFullNameNullLongConstructor() {
        User user = new User(null, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfEmailNullLongConstructor() {
        User user = new User(DUMMY_FULL_NAME, null, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }


    @Test
    public void userGetFullName() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_FULL_NAME, user.getFullName());
    }

    @Test
    public void userGetEmail() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_EMAIL, user.getEmail());
    }

    @Test
    public void userGetUserId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(DUMMY_USER_ID, user.getIdUser());
    }


    @Test
    public void userGetPhoneNumber() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION);
        assertEquals(DUMMY_PHONE_NUMBER, user.getPhoneNumber());
    }

    @Test
    public void userGetImageId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(DUMMY_IMAGE_ID, user.getImageId());
    }

    @Test
    public void userGetDescription() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(DUMMY_DESCRIPTION, user.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetUserIdEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setIdUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetPhoneNumberEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setPhoneNumber(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetDescriptionEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetImageIdEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setImageId(null);
    }

    @Test
    public void userSetUserId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setIdUser("2");
        assertEquals(user.getIdUser(), "2");
    }

    @Test
    public void userSetPhoneNumber() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setPhoneNumber("000000000000");
        assertEquals(user.getPhoneNumber(), "000000000000");
    }

    @Test
    public void userSetDescription() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setDescription("Hello");
        assertEquals(user.getDescription(), "Hello");
    }

    @Test
    public void userSetImageId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        user.setImageId("image.png");
        assertEquals(user.getImageId(), "image.png");
    }

    @Test
    public void userToStringCorrect() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(user.toString(), "FullName:" + DUMMY_FULL_NAME + "\nEmail:" + DUMMY_EMAIL + "\nId:" + DUMMY_USER_ID + "\nPhone:" + DUMMY_PHONE_NUMBER + "\nImageId:" + DUMMY_IMAGE_ID + "\nDescription:" + DUMMY_DESCRIPTION);
    }


    @Test
    public void userEqualCorrect() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(user1.equals(user2), true);
    }


    @Test
    public void userEqualDifferentUser1() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        User user2 = new User("Georges", DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser2() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        User user2 = new User(DUMMY_FULL_NAME, "CANDEA", DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser3() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, "231233564321", DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser4() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, "description_2", DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser5() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, "id", DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser6() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, "imageId", DUMMY_DOCUMENT_PATH);

        assertEquals(user1.equals(user2), false);
    }


    @Test
    public void userEqualNull() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(user1.equals(null), false);
    }

    @Test
    public void userEqualSame() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(user1.equals(user1), true);
    }

    @Test
    public void userEqualDiffClass() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
        assertEquals(user1.equals(1), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingFullName() {
        User user = new User(null, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }


    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingEmail() {
        User user = new User(DUMMY_FULL_NAME, null, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingID() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, null, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingPhone() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, null, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH);
    }


}