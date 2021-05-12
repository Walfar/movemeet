package com.sdp.movemeet.backend.serialization;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.models.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivitySerializer implements BackendSerializer<Activity> {

    // The key used to access the activityId attribute of a serialized Activity
    public static final String ACTIVITY_KEY = "activityId";
    // The key used to access the organizerId attribute of a serialized Activity
    public static final String ORGANIZER_KEY = "organizerId";
    // The key used to access the title attribute of a serialized Activity
    public static final String TITLE_KEY = "title";

    // The key used to access the numberParticipant attribute of a serialized Activity
    public static final String NPART_KEY = "numberParticipant";
    // The key used to access the participantsId attribute of a serialized Activity
    public static final String PARTICIPANTS_KEY = "participantId";
    // The key used to access the longitude attribute of a serialized Activity
    public static final String LONGITUDE_KEY = "longitude";
    // The key used to access the latitude attribute of a serialized Activity
    public static final String LATITUDE_KEY = "latitude";

    // The key used to access the description attribute of a serialized Activity
    public static final String DESC_KEY = "description";
    // The key used to access the date attribute of a serialized Activity
    public static final String DATE_KEY = "date";
    // The key used to access the duration attribute of a serialized Activity
    public static final String DURATION_KEY = "duration";
    // The key used to access the sport attribute of a serialized Activity
    public static final String SPORT_KEY = "sport";
    // The key used to access the address attribute of a serialized Activity
    public static final String ADDRESS_KEY = "address";
    // The key used to access the createdAt attribute of a serialized Activity
    public static final String CREATION_KEY = "createdAt";

    // The key used to access the documentPath attribute of a serialized Activity
    public static final String DOCUMENT_PATH_KEY = "documentPath";

    public Activity deserialize(Map<String, Object> data) {

        Activity act = new Activity(
                (String) data.get(ACTIVITY_KEY),
                (String) data.get(ORGANIZER_KEY),
                (String) data.get(TITLE_KEY),

                ((Long) data.get(NPART_KEY)).intValue(),
                (ArrayList<String>) data.get(PARTICIPANTS_KEY),

                (double) data.get(LONGITUDE_KEY),
                (double )data.get(LATITUDE_KEY),

                (String) data.get(DESC_KEY),
                (String) data.get(DOCUMENT_PATH_KEY),
                ((Timestamp) data.get(DATE_KEY)).toDate(),
                (double) data.get(DURATION_KEY),
                Sport.valueOf((String) data.get(SPORT_KEY)),
                (String) data.get(ADDRESS_KEY),
                ((Timestamp) data.get(CREATION_KEY)).toDate()
        );

        return act;
    }

    public Map<String, Object> serialize(Activity activity) {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(ACTIVITY_KEY, activity.getActivityId());
        data.put(ORGANIZER_KEY, activity.getOrganizerId());
        data.put(TITLE_KEY, activity.getTitle());

        data.put(NPART_KEY, activity.getNumberParticipant());
        data.put(PARTICIPANTS_KEY, activity.getParticipantId());

        data.put(LONGITUDE_KEY, activity.getLongitude());
        data.put(LATITUDE_KEY, activity.getLatitude());

        data.put(DESC_KEY, activity.getDescription());
        data.put(DATE_KEY, activity.getDate());
        data.put(DURATION_KEY, activity.getDuration());
        data.put(SPORT_KEY, activity.getSport());
        data.put(ADDRESS_KEY, activity.getAddress());
        data.put(CREATION_KEY, activity.getCreatedAt());

        data.put(DOCUMENT_PATH_KEY, activity.getDocumentPath());

        return data;
    }
}