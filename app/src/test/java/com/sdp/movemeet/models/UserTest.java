package com.sdp.movemeet.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private final static String DUMMY_FULL_NAME = "full name";
    private final static String DUMMY_EMAIL = "firstname.lastname@gmail.com";
    private final static String DUMMY_PHONE_NUMBER = "+41123456789";
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_USER_ID = "1";
    private final static String DUMMY_IMAGE_ID = "1234";
    private final static String DUMMY_DOCUMENT_PATH = "documentPath";
    private final static ArrayList<String> DUMMY_CREATE_ACTIVITIES_ID = new ArrayList<String>();
    private final static ArrayList<String> DUMMY_REGISTERED_ACTIVITIES_ID = new ArrayList<String>();


    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfFullNameNull() {
        User user = new User(null, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfEmailNull() {
        User user = new User(DUMMY_FULL_NAME, null, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, null, DUMMY_DESCRIPTION, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfUserIdNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, null, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfPhoneNumberNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, null, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfFullNameNullLongConstructor() {
        User user = new User(null, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsIfEmailNullLongConstructor() {
        User user = new User(DUMMY_FULL_NAME, null, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test
    public void userGetFullName() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_FULL_NAME, user.getFullName());
    }

    @Test
    public void userGetEmail() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_EMAIL, user.getEmail());
    }

    @Test
    public void userGetUserId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_USER_ID, user.getIdUser());
    }


    @Test
    public void userGetPhoneNumber() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_PHONE_NUMBER, user.getPhoneNumber());
    }

    @Test
    public void userGetImageId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_IMAGE_ID, user.getImageId());
    }

    @Test
    public void userGetDescription() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_DESCRIPTION, user.getDescription());
    }

    @Test
    public void userGetCreatedActivitiesId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_CREATE_ACTIVITIES_ID, user.getCreateActivitiesId());
    }

    @Test
    public void userGetRegisteredActivitiesId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(DUMMY_REGISTERED_ACTIVITIES_ID, user.getRegisteredActivitiesId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetUserIdEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setIdUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetPhoneNumberEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setPhoneNumber(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetDescriptionEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setDescription(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userSetImageIdEmpty() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setImageId(null);
    }

    @Test
    public void userSetUserId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setIdUser("2");
        assertEquals(user.getIdUser(), "2");
    }

    @Test
    public void userSetPhoneNumber() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setPhoneNumber("000000000000");
        assertEquals(user.getPhoneNumber(), "000000000000");
    }

    @Test
    public void userSetDescription() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setDescription("Hello");
        assertEquals(user.getDescription(), "Hello");
    }

    @Test
    public void userSetImageId() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.setImageId("image.png");
        assertEquals(user.getImageId(), "image.png");
    }

    @Test
    public void userToStringCorrect() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(user.toString(), "FullName:" + DUMMY_FULL_NAME + "\nEmail:" + DUMMY_EMAIL + "\nId:" + DUMMY_USER_ID + "\nPhone:"
                + DUMMY_PHONE_NUMBER + "\nImageId:" + DUMMY_IMAGE_ID + "\nDescription:" + DUMMY_DESCRIPTION +
                "\nCreatedActivitiesId" + DUMMY_CREATE_ACTIVITIES_ID + "\nRegisteredActivitiesId" + DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userAddCreatedActivityNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.addCreatedActivityId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userAddRegisteredActivityNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.addRegisteredActivityId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userAddSameCreatedActivity() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        String activity = "caro";
        user.addCreatedActivityId(activity);
        user.addCreatedActivityId(activity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userAddSameRegisteredActivity() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        String activity = "caro";
        user.addRegisteredActivityId(activity);
        user.addRegisteredActivityId(activity);
    }

    @Test
    public void userAddCreatedActivityCorrect() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        String activity1 = "1";
        String activity2 = "bob";
        user.addCreatedActivityId(activity1);
        user.addCreatedActivityId(activity2);

        assertEquals(2, user.getCreateActivitiesId().size());
    }

    @Test
    public void userAddRegisteredActivityCorrect() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        String activity1 = "1";
        String activity2 = "bob";
        user.addRegisteredActivityId(activity1);
        user.addRegisteredActivityId(activity2);

        assertEquals(2, user.getRegisteredActivitiesId().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void userRemoveCreatedActivityNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.removeCreatedActivityId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userRemoveRegisteredActivityNull() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        user.removeRegisteredActivityId(null);
    }

    @Test
    public void userRemoveCreatedActivityCorrect() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        String activity1 = "1";
        String activity2 = "bob";
        user.addCreatedActivityId(activity1);
        user.addCreatedActivityId(activity2);
        user.removeCreatedActivityId(activity1);

        assertEquals(1, user.getCreateActivitiesId().size());
        user = null;
    }

    @Test
    public void userRemoveRegisteredActivityCorrect() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        String activity1 = "1";
        String activity2 = "bob";
        user.addRegisteredActivityId(activity1);
        user.addRegisteredActivityId(activity2);
        user.removeRegisteredActivityId(activity1);

        assertEquals(1, user.getRegisteredActivitiesId().size());
        user = null;
    }


    @Test
    public void userEqualCorrect() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(user1.equals(user2), true);
    }


    @Test
    public void userEqualDifferentUser1() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        User user2 = new User("Georges", DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser2() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        User user2 = new User(DUMMY_FULL_NAME, "CANDEA", DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser3() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, "231233564321", DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser4() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, "description_2", DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser5() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, "id", DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualDifferentUser6() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        User user2 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, "imageId", DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);

        assertEquals(user1.equals(user2), false);
    }

    @Test
    public void userEqualNull() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(user1.equals(null), false);
    }

    @Test
    public void userEqualSame() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(user1.equals(user1), true);
    }

    @Test
    public void userEqualDiffClass() {
        User user1 = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
        assertEquals(user1.equals(1), false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingFullName() {
        User user = new User(null, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }


    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingEmail() {
        User user = new User(DUMMY_FULL_NAME, null, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingID() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, DUMMY_PHONE_NUMBER, DUMMY_DESCRIPTION, null, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userConstructorFailsMissingPhone() {
        User user = new User(DUMMY_FULL_NAME, DUMMY_EMAIL, null, DUMMY_DESCRIPTION, DUMMY_USER_ID, DUMMY_IMAGE_ID, DUMMY_DOCUMENT_PATH, DUMMY_CREATE_ACTIVITIES_ID, DUMMY_REGISTERED_ACTIVITIES_ID);
    }


}