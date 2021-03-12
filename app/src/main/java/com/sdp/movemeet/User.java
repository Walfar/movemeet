package com.sdp.movemeet;

/**
 *
 * This class represents a user.
 *
 *
 * */

public class  User {

    private final String firstName;
    private final String lastName;
    private final String email;
    private String idUser;
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


    public User(String firstName, String lastName, String email, String idUser){
        if(firstName == null || lastName == null || email == null || idUser == null){
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.idUser = idUser;
    }

    /**
     * Other construction of a new user instance.
     * @param firstName
     * @param lastName
     * @param email
     * @param imageId
     * @param description
     */

    public User(String firstName, String lastName, String email, String idUser, String phoneNumber, String imageId, String description){
        if(firstName == null || lastName == null || email == null || idUser == null || phoneNumber == null || imageId == null || description == null){
            throw new IllegalArgumentException();
        }

        if(phoneNumber.length() != 12){
            throw new IllegalArgumentException();
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.idUser = idUser;
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
     * @return the user's id
     */
    public String getIdUser() { return idUser; }

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

    /**
     *
     * @param idUser change user's id
     */
    public void setIdUser(String idUser) {
        if(idUser == null){
            throw new IllegalArgumentException();
        }
        this.idUser = idUser;
    }

    /**
     *
     * @param phoneNumber change user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null){
            throw new IllegalArgumentException();
        }
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @param description change user's description
     */
    public void setDescription(String description) {
        if(description == null){
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    /**
     *
     * @param imageId changer user's image
     */
    public void setImageId(String imageId) {
        if(imageId == null){
            throw new IllegalArgumentException();
        }
        this.imageId = imageId;
    }

    @Override
    public String toString(){
        return "FistName:" + firstName + "\nLastName:" + lastName + "\nEmail:" + email + "\nId:" + idUser +
                "\nPhoneNumber:" + phoneNumber + "\nImageId:" + imageId + "\nDescription:" + description;
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(this == o){
            return true;
        }

        if(getClass() != o.getClass()){
            return false;
        }
        User obj = (User) o;

        return firstName.equals(obj.firstName) && lastName.equals(obj.lastName) && email.equals(obj.email) &&
                idUser.equals(obj.idUser) && phoneNumber.equals(obj.phoneNumber) && imageId.equals(obj.imageId) && description.equals(obj.description);
    }
}