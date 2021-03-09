package com.sdp.movemeet;


import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 *
 *
 *  This class represents an activity.
 *
 * */

public class Activity {

    private final String activityId;
    private final String organisator;
    private String title;
    private int numberParticipant;
    private ArrayList<User> participants;

    private double longitude;
    private double latitude;

    private String description;
    private Date date;
    private double duration;
    private final Sport sport;
    private String address;

    static  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    /**
     * Construct a new activity
     * @param activityId id of the activity
     * @param organisator user how create the activity
     * @param title of the activity
     * @param numberParticipant for the activity
     * @param participants how register for the activity
     * @param longitude of the activity
     * @param latitude of the activity
     * @param description of the activity
     * @param date date and hour of the activity
     * @param duration of the activity 1.0 -> 1 hour
     * @param sport of the activity
     * @param address of the activity
     */
    public Activity(String activityId,
                    String organisator,
                    String title,
                    int numberParticipant,
                    ArrayList<User> participants,
                    double longitude,
                    double latitude,
                    String description,
                    Date date,
                    double duration,
                    Sport sport,
                    String address){

        if(activityId == null || organisator == null || title == null || numberParticipant <= 0 || description == null || date == null || duration <= 0 || sport == null || address == null){
            throw new IllegalArgumentException();
        }

        this.activityId = activityId;
        this.organisator = organisator;
        this.title = title;
        this.numberParticipant = numberParticipant;
        this.participants = participants;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.sport = sport;
        this.address = address;
    }

    /**
     *
     * @return the activity's is
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     *
     * @return the activity's organisator
     */
    public String getOrganisator() {
        return organisator;
    }

    /**
     *
     * @return the activity's title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return the activity's number of participant
     */
    public int getNumberParticipant() {
        return numberParticipant;
    }

    /**
     *
     * @return the activity's participants
     */
    public ArrayList<User> getParticipants() {
        return participants;
    }

    /**
     *
     * @return the activity's longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @return the activity's latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @return the activity's description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return the activity's date
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @return the activity's duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     *
     * @return the activity's sport
     */
    public Sport getSport() {
        return sport;
    }

    /**
     *
     * @return the activity's address
     */
    public String getAddress() {
        return address;
    }
}