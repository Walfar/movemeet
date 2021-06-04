package com.sdp.movemeet.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a user.
 */

public class User implements Serializable, FirebaseObject {

    private final String fullName;
    private final String email;
    private String phone;
    private String description;
    private String idUser;
    private String imageId;
    private String documentPath;
    private final ArrayList<String> createdActivitiesId;
    private final ArrayList<String> registeredActivitiesId;


    /**
     * Construct a new user
     *
     * @param fullName    : full name of the user
     * @param email       : google email of the user
     * @param phone       : phone number of the user
     * @param description : description of the user
     */
    public User(String fullName, String email, String phone, String description, ArrayList<String> createdActivitiesId, ArrayList<String> registeredActivitiesId) {
        if (fullName == null || email == null || phone == null) {
            throw new IllegalArgumentException();
        }
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.createdActivitiesId = createdActivitiesId;
        this.registeredActivitiesId = registeredActivitiesId;
    }


    /**
     * Additional construction of a new user instance.
     *
     * @param idUser       : the ID of the user
     * @param imageId      : the ID of the profile picture of the user
     * @param documentPath : the document path of the activity in Firebase Firestore
     */

    public User(String fullName,
                String email,
                String phone,
                String description,
                String idUser,
                String imageId,
                String documentPath,
                ArrayList<String> createdActivitiesId,
                ArrayList<String> registeredActivitiesId) {
        if (fullName == null || email == null || idUser == null || phone == null) {
            throw new IllegalArgumentException();
        }

        if (phone.length() != 12) {
            throw new IllegalArgumentException();
        }

        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.idUser = idUser;
        this.imageId = imageId;
        this.documentPath = documentPath;
        this.createdActivitiesId = createdActivitiesId;
        this.registeredActivitiesId = registeredActivitiesId;
    }

    /**
     * @return the user's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the user's id
     */
    public String getIdUser() {
        return idUser;
    }

    /**
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phone;
    }

    /**
     * @return the user's image id
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * @return the user's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the document path of the user
     */
    public String getDocumentPath() {
        return this.documentPath;
    }

    /**
     * @return the list of activities created by the user
     */
    public ArrayList<String> getCreatedActivitiesId() {
        return createdActivitiesId;
    }

    /**
     * @return the list of activities registered by the user
     */
    public ArrayList<String> getRegisteredActivitiesId() {
        return registeredActivitiesId;
    }

    /**
     * @param idUser change user's id
     */
    public void setIdUser(String idUser) {
        if (idUser == null) {
            throw new IllegalArgumentException();
        }
        this.idUser = idUser;
    }

    /**
     * @param phone change user's phone number
     */
    public void setPhoneNumber(String phone) {
        if (phone == null) {
            throw new IllegalArgumentException();
        }
        this.phone = phone;
    }

    /**
     * @param description change user's description
     */
    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    /**
     * @param imageId changer user's image
     */
    public void setImageId(String imageId) {
        if (imageId == null) {
            throw new IllegalArgumentException();
        }
        this.imageId = imageId;
    }

    /**
     * @param path change the document path of the user
     */
    public String setDocumentPath(String path) {
        if (documentPath == null) documentPath = path;
        return documentPath;
    }

    /**
     * @param activityId add a created activity
     */
    public void addCreatedActivityId(String activityId) {
        if (activityId == null) {
            throw new IllegalArgumentException();
        }
        if (createdActivitiesId.contains(activityId)) {
            throw new IllegalArgumentException();
        }
        this.createdActivitiesId.add(activityId);
    }

    /**
     * @param activityId remove a created activity
     */
    public void removeCreatedActivityId(String activityId) {
        if (activityId == null) {
            throw new IllegalArgumentException();
        }
        this.createdActivitiesId.remove(activityId);
    }


    /**
     * @param activityId add a registered activity
     */
    public void addRegisteredActivityId(String activityId) {
        if (activityId == null) {
            throw new IllegalArgumentException();
        }
        if (registeredActivitiesId.contains(activityId)) {
            throw new IllegalArgumentException();
        }
        this.registeredActivitiesId.add(activityId);
    }

    /**
     * @param activityId remove a registered activity
     */
    public void removeRegisteredActivityId(String activityId) {
        if (activityId == null) {
            throw new IllegalArgumentException();
        }
        this.registeredActivitiesId.remove(activityId);
    }


    @Override
    public String toString() {
        return "FullName:" + fullName + "\nEmail:" + email + "\nId:" + idUser +
                "\nPhone:" + phone + "\nImageId:" + imageId + "\nDescription:" + description +
                "\nCreatedActivitiesId" + createdActivitiesId + "\nRegisteredActivitiesId" + registeredActivitiesId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return false;
        }
        User obj = (User) o;

        return fullName.equals(obj.fullName) && email.equals(obj.email) &&
                idUser.equals(obj.idUser) && phone.equals(obj.phone) &&
                imageId.equals(obj.imageId) && description.equals(obj.description) &&
                createdActivitiesId.equals(obj.createdActivitiesId) && registeredActivitiesId.equals(obj.registeredActivitiesId);
    }
}