package com.sdp.movemeet;

/**
 *
 * This class represents a user.
 *
 *
 * */

public class User {

    private final String firstName;
    private final String lastName;
    private final String email;
    private String phoneNumber;
    private String imageId;
    private String description;

    /**
     * Construct a new user
     * @param firstName : name of the user
     * @param lastName : last name of the user
     * @param email : google email of the user
     *
     *              All this parameters cannot be null
     */


    public User(String firstName, String lastName, String email){
        if(firstName == null || lastName == null || email == null){
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Other construction of a new user instance.
     * @param firstName
     * @param lastName
     * @param email
     * @param imageId
     * @param description
     */

    public User(String firstName, String lastName, String email, String phoneNumber, String imageId, String description){
        if(firstName == null || lastName == null || email == null || phoneNumber == null || imageId == null || description == null){
            throw new IllegalArgumentException();
        }

        if(phoneNumber.length() != 12){
            throw new IllegalArgumentException();
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageId = imageId;
        this.description = description;

    }

    /**
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @return the user's image id
     */
    public String getImageId() {
        return imageId;
    }

    /**
     *
     * @return the user's description
     */
    public String getDescription() {
        return description;
    }
}