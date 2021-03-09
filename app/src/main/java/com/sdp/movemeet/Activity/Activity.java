package com.sdp.movemeet.Activity;

import android.location.Location;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Activity {

    private final String hostID;
    private final int sportType;
    private final int maxParticipants;
    public int participants;
    private final Date time;
    private DocumentReference backendRef;

    public Activity(String hostID, int sportType, Date time, int maxParticipants, int participants) {
        this.hostID = hostID;
        this.sportType = sportType;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
        this.time = time;
    }

    public String getHostID() {
        return hostID;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getSportType() {
        return sportType;
    }

    public Date getTime() {
        return time;
    }

    public DocumentReference getBackendRef() {
        return this.backendRef;
    }

    /**
     * Sets the backend reference property if it was previously null, does nothing otherwise.
     *
     * @param newRef the new value used to reference this activity in its collection in the backend
     * @return the DocumentReference used to reference this activity in its collection in the backend
     */
    public DocumentReference setBackendRef(DocumentReference newRef) throws IllegalArgumentException {
        if (backendRef == null) backendRef = newRef;
        return backendRef;
    }
}
