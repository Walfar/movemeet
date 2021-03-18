package com.sdp.movemeet.Activity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.sdp.movemeet.Sport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivitySerializer {

    public static final String ACTIVITY_KEY = "activityId";
    public static final String ORGANIZER_KEY = "organizerId";
    public static final String TITLE_KEY = "title";

    public static final String NPART_KEY = "numberParticipant";
    public static final String PARTICIPANTS_KEY = "participantsId";
    public static final String LONGITUDE_KEY = "longitude";
    public static final String LATITUDE_KEY = "latitude";

    public static final String DESC_KEY = "description";
    public static final String DATE_KEY = "date";
    public static final String DURATION_KEY = "duration";
    public static final String SPORT_KEY = "sport";
    public static final String ADDRESS_KEY = "address";


    private ActivitySerializer() {

    }

    public static Activity deserialize(Map<String, Object> data) {
        return new Activity(
                (String) data.get(ACTIVITY_KEY),
                (String) data.get(ORGANIZER_KEY),
                (String) data.get(TITLE_KEY),

                (int) data.get(NPART_KEY),
                (ArrayList<String>) data.get(PARTICIPANTS_KEY),

                (double) data.get(LONGITUDE_KEY),
                (double )data.get(LATITUDE_KEY),

                (String) data.get(DESC_KEY),
                (Date) data.get(DATE_KEY),
                (double) data.get(DURATION_KEY),
                (Sport) data.get(SPORT_KEY),
                (String) data.get(ADDRESS_KEY)
        );
    }

    public static Map<String, Object> serialize(Activity activity) {
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

        return data;
    }
}
