package com.sdp.movemeet.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an activity.
 */

public class Activity implements Serializable, FirebaseObject {

    private final String activityId;
    private final String organizerId;
    private String title;
    private int numberParticipant;
    private final ArrayList<String> participantsId;

    private double longitude;
    private double latitude;

    private String description;
    private String documentPath;
    private Date date;
    private double duration;
    private final Sport sport;
    private String address;
    private Date createdAt;

    private Map<String, GPSPath> participantRecordings;

    /**
     * Construct a new activity
     *
     * @param activityId        id of the activity
     * @param organizerId       user how create the activity
     * @param title             of the activity
     * @param numberParticipant for the activity
     * @param participantsId    how register for the activity
     * @param longitude         of the activity
     * @param latitude          of the activity
     * @param description       of the activity
     * @param documentPath      of the activity in Firebase Firestore
     * @param date              date and hour of the activity
     * @param duration          of the activity 1.0 -> 1 hour
     * @param sport             of the activity
     * @param address           of the activity
     */
    public Activity(String activityId,
                    String organizerId,
                    String title,
                    int numberParticipant,
                    ArrayList<String> participantsId,
                    double longitude,
                    double latitude,
                    String description,
                    String documentPath,
                    Date date,
                    double duration,
                    Sport sport,
                    String address,
                    Date createdAt) {

        if (activityId == null || organizerId == null || title == null || numberParticipant <= 0)
            throw new IllegalArgumentException();

        if (date == null || sport == null || address == null || duration <= 0 || description == null)
            throw new IllegalArgumentException();


        this.activityId = activityId;
        this.organizerId = organizerId;
        this.title = title;
        this.numberParticipant = numberParticipant;
        this.participantsId = participantsId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.documentPath = documentPath;
        this.date = date;
        this.duration = duration;
        this.sport = sport;
        this.address = address;
        this.createdAt = createdAt;

        this.participantRecordings = new HashMap<String, GPSPath>();
    }

    /**
     * @return the activity's is
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * @return the activity's organizer
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * @return the activity's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the activity's number of participant
     */
    public int getNumberParticipant() {
        return numberParticipant;
    }

    /**
     * @return the activity's participants
     */
    public ArrayList<String> getParticipantId() {
        return participantsId;
    }

    /**
     * @return the activity's longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return the activity's latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return the activity's description
     */
    public String getDescription() {
        return description;
    }


    /**
     * @return the activity's document path
     */
    public String getDocumentPath() {
        return documentPath; // this.documentPath
    }

    /**
     * @return the activity's date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the activity's duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * @return the activity's sport
     */
    public Sport getSport() {
        return sport;
    }

    /**
     * @return the activity's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param title change activity's title
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException();
        }
        this.title = title;
    }

    /**
     * @param numberParticipant change activity's number participant
     */
    public void setNumberParticipant(int numberParticipant) {
        if (numberParticipant <= 0) {
            throw new IllegalArgumentException();
        }
        this.numberParticipant = numberParticipant;
    }

    /**
     * @param longitude change activity's longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @param latitude change activity's latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param description change activity's description
     */
    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    /**
     * @param path change activity's document path
     */
    public String setDocumentPath(String path) {
        if (documentPath == null) documentPath = path;
        return documentPath;
    }

    /**
     * @param date change activity's date
     */
    public void setDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException();
        }
        this.date = date;
    }

    /**
     * @param duration change activity's duration
     */
    public void setDuration(double duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException();
        }
        this.duration = duration;
    }

    /**
     * @param address change activity's address
     */
    public void setAddress(String address) {
        if (address == null) {
            throw new IllegalArgumentException();
        }
        this.address = address;
    }

    /**
     * @param participant add a participant
     */
    public void addParticipantId(String participant) {
        if (participant == null) {
            throw new IllegalArgumentException();
        }
        if (participantsId.contains(participant)) {
            throw new IllegalArgumentException("Already registered");
        }
        if (participantsId.size() >= this.numberParticipant) {
            throw new IllegalArgumentException("The limit of participants has already been reached");
        }
        this.participantsId.add(participant);
    }

    /**
     * @param participant remove a participant
     */
    public void removeParticipantId(String participant) {
        if (participant == null) {
            throw new IllegalArgumentException();
        }

        this.participantsId.remove(participant);
    }

    /**
     * @param createdAt date of creation
     */
    public void setCreatedAt(Date createdAt) {
        if (createdAt != null) this.createdAt = createdAt;
    }

    /**
     * @return date of creation
     */
    public Date getCreatedAt() {
        return createdAt;
    }


    /**
     * Retrieves this activity's GPS recording data for all users
     *
     * @return an array of GPSPaths containing data recorded for this activity.
     */
    public Map<String, GPSPath> getParticipantRecordings() {
        return this.participantRecordings;
    }

    /**
     * Sets this activity's GPS recording data for all participants
     *
     * @param newRecordings an array of GPSPath containing information to attach to this activity.
     */
    public void setParticipantRecordings(Map<String, GPSPath> newRecordings) {
        this.participantRecordings = newRecordings;
    }


    @Override
    public String toString() {
        return "ActivityId:" + activityId + "\nOrganizerId" + organizerId + "\nTitle:" + title + "\nNumberParticipant:" + numberParticipant +
                "\nParticipantId:" + participantsId + "\nLongitude:" + longitude + "\nLatitude:" + latitude + "\nDescription:" + description +
                "\nDate:" + date + "\nDuration:" + duration + "\nSport:" + sport + "\nAddress:" + address + "\nCreated at:" + createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (this == o)
            return true;

        if (getClass() != o.getClass())
            return false;

        Activity obj = (Activity) o;

        return activityId.equals(obj.activityId) && organizerId.equals(obj.organizerId) && title.equals(obj.title) &&
                numberParticipant == obj.numberParticipant && participantsId.equals(obj.participantsId) && longitude == obj.longitude &&
                latitude == obj.latitude && description.equals(obj.description) && date.equals(obj.date) && duration == obj.duration &&
                sport.equals(obj.sport) && address.equals(obj.address) && createdAt.equals(obj.createdAt);

    }
}