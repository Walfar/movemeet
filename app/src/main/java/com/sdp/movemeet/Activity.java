package com.sdp.movemeet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.List;
/**
 *
 *
 *  This class represents an activity.
 *
 * */

public class Activity {

    private final String activityId;
    private final String organisatorId;
    private String title;
    private int numberParticipant;
    private ArrayList<String> participantsId;

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
     * @param organisatorId user how create the activity
     * @param title of the activity
     * @param numberParticipant for the activity
     * @param participantsId how register for the activity
     * @param longitude of the activity
     * @param latitude of the activity
     * @param description of the activity
     * @param date date and hour of the activity
     * @param duration of the activity 1.0 -> 1 hour
     * @param sport of the activity
     * @param address of the activity
     */
    public Activity(String activityId,
                    String organisatorId,
                    String title,
                    int numberParticipant,
                    ArrayList<String> participantsId,
                    double longitude,
                    double latitude,
                    String description,
                    Date date,
                    double duration,
                    Sport sport,
                    String address){

        if(activityId == null || organisatorId == null || title == null || numberParticipant <= 0 )
            throw new IllegalArgumentException();

        if(description == null || date == null || duration <= 0 || sport == null || address == null)
            throw new IllegalArgumentException();



        this.activityId = activityId;
        this.organisatorId = organisatorId;
        this.title = title;
        this.numberParticipant = numberParticipant;
        this.participantsId = participantsId;
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
    public String getOrganisatorId() {
        return organisatorId;
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
    public ArrayList<String> getParticipantId() {
        return participantsId;
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

    /**
     *
     * @param title change activity's title
     */
    public void setTitle(String title) {
        if(title == null){
            throw new IllegalArgumentException();
        }
        this.title = title;
    }

    /**
     *
     * @param numberParticipant change activity's number participant
     */
    public void setNumberParticipant(int numberParticipant) {
        if(numberParticipant <= 0){
            throw new IllegalArgumentException();
        }
        this.numberParticipant = numberParticipant;
    }

    /**
     *
     * @param longitude change activity's longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @param latitude change activity's latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @param description change activity's description
     */
    public void setDescription(String description) {
        if(description == null){
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    /**
     *
     * @param date change activity's date
     */
    public void setDate(Date date) {
        if(date == null){
            throw new IllegalArgumentException();
        }
        this.date = date;
    }

    /**
     *
     * @param duration change activity's duration
     */
    public void setDuration(double duration) {
        if(duration <= 0){
            throw new IllegalArgumentException();
        }
        this.duration = duration;
    }

    /**
     *
     * @param address change activity's address
     */
    public void setAddress(String address) {
        if(address == null){
            throw new IllegalArgumentException();
        }
        this.address = address;
    }

    /**
     *
     * @param participant add a participant
     */
    public void addParticipantId(String participant){
        if(participant == null){
            throw new IllegalArgumentException();
        }
        if(participantsId.contains(participant)){
            throw new IllegalArgumentException();
        }
        this.participantsId.add(participant);
    }

    /**
     *
     * @param participant remove a participant
     */
    public void removeParticipantId(String participant){
        if(participant == null){
            throw new IllegalArgumentException();
        }

        this.participantsId.remove(participant);
    }

    @Override
    public String toString(){
        return "ActivityId:" + activityId + "\nOrganisatorId" + organisatorId + "\nTitle:" + title + "\nNumberParticipant:" + numberParticipant +
                "\nParticipantId:" + participantsId + "\nLongitude:" + longitude + "\nLatitude:" + latitude + "\nDescription:" + description +
                "\nDate:" + date + "\nDuration:" + duration + "\nSport:" + sport + "\nAddress:" + address;
    }

    @Override
    public boolean equals(Object o){
        if(o == null)
            return false;

        if(this == o)
            return true;

        if(getClass() != o.getClass())
            return false;

        Activity obj = (Activity) o;

        return activityId.equals(obj.activityId) && organisatorId.equals(obj.organisatorId) && title.equals(obj.title) &&
                numberParticipant == obj.numberParticipant && participantsId.equals(obj.participantsId) && longitude == obj.longitude &&
                latitude == obj.latitude && description.equals(obj.description) && date.equals(obj.date) && duration == obj.duration &&
                sport.equals(obj.sport) && address.equals(obj.address);

    }
}