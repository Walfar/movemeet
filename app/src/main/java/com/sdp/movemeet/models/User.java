package com.sdp.movemeet.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a user.
 */

public class User extends FirebaseObject implements Serializable {

    private final String fullName;
    private final String email;
    private String phone;
    private String description;
    private String idUser;
    private String imageId;
    private String documentPath;
    private ArrayList<String> registeredActivities;


    /**
     * Construct a new user
     *
     * @param fullName    : full name of the user
     * @param email       : google email of the user
     * @param phone       : phone number of the user
     * @param description : description of the user
     */
    public User(String fullName, String email, String phone, String description, ArrayList<String> registeredActivities) {
        if (fullName == null || email == null || phone == null) {
            throw new IllegalArgumentException();
        }
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.description = description;
        this.registeredActivities = registeredActivities;
    }


    /**
     * Additional construction of a new user instance.
     *
     * @param idUser       : the ID of the user
     * @param imageId      : the ID of the profile picture of the user
     * @param documentPath : the document path of the activity in Firebase Firestore
     */

    public User(String fullName, String email, String phone, String description, String idUser, String imageId, String documentPath, ArrayList<String> registeredActivities) {
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
        this.registeredActivities = registeredActivities;
        super.documentPath = documentPath;

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
     *
     * @return the user's list of registered activities
     */
    public ArrayList<String> getRegisteredActivities() {
        return registeredActivities;
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
     *
     * @param activityId when a user register to an activity, add this activity to the list of registered activities
     */
    public void addActivitiesToRegistered(String activityId){
        if(activityId == null){
            throw new IllegalArgumentException();
        }
        if(registeredActivities.contains(activityId)){
            throw new IllegalArgumentException("Already registered");
        }

        this.registeredActivities.add(activityId);
    }

    /**
     *
     * @param activityId remove an activity
     */
    public void removeActivity(String activityId){
        if(activityId == null){
            throw new IllegalArgumentException();
        }

        this.registeredActivities.remove(activityId);
    }


    public String getDocumentPath() {
        return super.getDocumentPath();
    }

    public String setDocumentPath(String path) {
        return super.setDocumentPath(path);
    }

    @Override
    public String toString() {
        return "FullName:" + fullName + "\nEmail:" + email + "\nId:" + idUser +
                "\nPhone:" + phone + "\nImageId:" + imageId + "\nDescription:" + description + "\nregisteredActivities" + registeredActivities;
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
                idUser.equals(obj.idUser) && phone.equals(obj.phone) && imageId.equals(obj.imageId) && description.equals(obj.description) && registeredActivities.equals(obj.registeredActivities);
    }
}