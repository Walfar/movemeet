package com.sdp.movemeet.backend.serialization;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.GPSPath;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A BackendSerializer capable of (de)serializing Activities
 */
public class ActivitySerializer implements BackendSerializer<Activity> {

    public static final String CREATED_ACTIVITY_FIELD = "createdActivity";

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

    public static final String GPS_RECORDINGS_KEY = "gpsRecordings";
    private static final String REC_TIME_KEY = "time";
    private static final String REC_DIST_KEY = "distance";
    private static final String REC_SPEED_KEY = "averageSpeed";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Activity deserialize(Map<String, Object> data) {

        Activity act = new Activity(
                (String) data.get(ACTIVITY_KEY),
                (String) data.get(ORGANIZER_KEY),
                (String) data.get(TITLE_KEY),

                ((Long) data.get(NPART_KEY)).intValue(),
                (ArrayList<String>) data.get(PARTICIPANTS_KEY),

                (double) data.get(LONGITUDE_KEY),
                (double) data.get(LATITUDE_KEY),

                (String) data.get(DESC_KEY),
                (String) data.get(DOCUMENT_PATH_KEY),
                ((Timestamp) data.get(DATE_KEY)).toDate(),
                (double) data.get(DURATION_KEY),
                Sport.valueOf((String) data.get(SPORT_KEY)),
                (String) data.get(ADDRESS_KEY),
                ((Timestamp) data.get(CREATION_KEY)).toDate()
        );

        Map<String, Map<String, Object>> gpsMaps = (Map<String, Map<String, Object>>) data.getOrDefault(GPS_RECORDINGS_KEY, null);
        if (gpsMaps != null) {
            Map<String, GPSPath> recordings = new HashMap<String, GPSPath>();
            for (String uid : gpsMaps.keySet()) {
                GPSPath rec = new GPSPath();
                rec.setTime(((Number) gpsMaps.get(uid).get(REC_TIME_KEY)).longValue());
                rec.setDistance(((Number) gpsMaps.get(uid).get(REC_DIST_KEY)).floatValue());
                rec.setAverageSpeed(((Number) gpsMaps.get(uid).get(REC_SPEED_KEY)).floatValue());
                recordings.put(uid, rec);
            }
            act.setParticipantRecordings(recordings);
        }

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

        if (activity.getParticipantRecordings() != null)
            data.put(GPS_RECORDINGS_KEY, activity.getParticipantRecordings());

        if (!activity.getActivityId().equals("12345")) {
            // Intercepting the activity path to add it to the organizer Firebase Firestore document
            BackendManager<User> userManager = new FirestoreUserManager(FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
            userManager.update(FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + activity.getOrganizerId(), CREATED_ACTIVITY_FIELD, activity.getDocumentPath(), ActivityDescriptionActivity.UPDATE_FIELD_UNION);
        }

        return data;
    }
}