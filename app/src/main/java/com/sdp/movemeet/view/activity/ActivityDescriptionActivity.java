package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.GPSPath;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.map.GPSRecordingActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.sdp.movemeet.utility.ActivityPictureCache.loadFromCache;

/***
 * Activity for show the description of an activity. Informations about an activity are : sport, date and time, time estimate, organizer,
 * a list of participants, a picture, address, and description. A user can register to an activity, and access to the chat.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
public class ActivityDescriptionActivity extends AppCompatActivity {

    private static final String TAG = "ActDescActivity";
    public static final String PARTICIPANT_ID_FIELD = "participantId";
    public static final String REGISTERED_ACTIVITY_FIELD = "registeredActivity";
    public static final String UPDATE_FIELD_UNION = "union";
    public static final String UPDATE_FIELD_REMOVE = "remove";
    public static final String DESCRIPTION_ACTIVITY_KEY = "activitykey";
    public static final String RECORDING_EXTRA_NAME = "gpsreckey";
    public static final String DISTANCE_UNIT = "km";
    public static final String SPEED_UNIT = "km/h";
    public static final String ACTIVITY_CHAT_ID = "ActivityChatId";
    public static final String ACTIVITY_TITLE = "ActivityTitle";
    private static final int REQUEST_IMAGE = 1000;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;
    private TextView organizerView, numberParticipantsView, participantNamesView;
    private FirebaseAuth fAuth;
    private String userId, organizerId, imagePath;
    private StringBuilder participantNamesString = new StringBuilder();

    private ImageView activityImage;
    private ProgressBar progressBar;

    private BackendManager<Activity> activityManager;
    private BackendManager<User> userManager;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            userId = fAuth.getCurrentUser().getUid();

            userManager = new FirestoreUserManager(FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
            activityManager = new FirestoreActivityManager(FirestoreActivityManager.ACTIVITIES_COLLECTION, new ActivitySerializer());

        }

        if(enableNav) new Navigation(this, R.id.nav_home).createDrawer();

        Intent intent = getIntent();

        if (intent != null) {
            activity = (Activity) intent.getSerializableExtra(DESCRIPTION_ACTIVITY_KEY);
        }

        if (activity != null) {
            displayDescriptionActivityData();
        }
    }


    public void displayDescriptionActivityData() {
        createTitleView();
        createDescriptionView();
        createDateView();
        createAddressView();
        createSportView();
        createDurationView();
        createOrganizerView();
        getOrganizerName();
        createParticipantNumberView(activity);
        getParticipantNames(activity);
        loadActivityHeaderPicture();
        setButton(activity);
    }

    /**
     * Modify the visibility of buttons in the layout
     */
    private void setButton(Activity activity) {
        View recButton = findViewById(R.id.activityGPSRecDescription);
        if (activity.getParticipantId().contains(userId)) {
            findViewById(R.id.activityRegisterDescription).setVisibility(View.GONE);
            findViewById(R.id.activityChatDescription).setVisibility(View.VISIBLE);
            findViewById(R.id.activityUnregisterDescription).setVisibility(View.VISIBLE);
            if (activity.getSport() == Sport.Running || activity.getSport() == Sport.Trekking) {
                recButton.setVisibility(View.VISIBLE);
                recButton.setEnabled(true);
                if (userId != null && activity.getParticipantRecordings().containsKey(userId)) {
                    displayParticipantStats();
                } else {
                    findViewById(R.id.activity_description_stats_layout).setVisibility(View.GONE);
                    findViewById(R.id.activity_description_stats_data_layout).setVisibility(View.GONE);
                    recButton.setVisibility(View.GONE);
                }
            } else {
                recButton.setVisibility(View.GONE);
                recButton.setEnabled(false);
                findViewById(R.id.activity_description_stats_layout).setVisibility(View.GONE);
                findViewById(R.id.activity_description_stats_data_layout).setVisibility(View.GONE);
            }
        } else {
            recButton.setVisibility(View.GONE);
            findViewById(R.id.activityUnregisterDescription).setVisibility(View.GONE);
            recButton.setEnabled(false);
            findViewById(R.id.activity_description_stats_layout).setVisibility(View.GONE);
            findViewById(R.id.activity_description_stats_data_layout).setVisibility(View.GONE);
            if (activity.getParticipantId().size() < activity.getNumberParticipant()) {
                findViewById(R.id.activityRegisterDescription).setVisibility(View.VISIBLE);
                findViewById(R.id.activityGPSRecDescription).setVisibility(View.GONE);
                findViewById(R.id.activityChatDescription).setVisibility(View.GONE);
            } else {
                findViewById(R.id.activityRegisterDescription).setVisibility(View.VISIBLE);
                findViewById(R.id.activityRegisterDescription).setEnabled(false);
                ((TextView) findViewById(R.id.activityRegisterDescription)).setText("No more free places");
                findViewById(R.id.activityGPSRecDescription).setVisibility(View.GONE);
                findViewById(R.id.activityChatDescription).setVisibility(View.GONE);
            }
        }
    }

    private void getParticipantNames(Activity activity) {
        ArrayList<String> participantIds = activity.getParticipantId();
        participantNamesString = new StringBuilder();
        for (int i = 0; i < participantIds.size(); i++) {
            String currentParticipantId = participantIds.get(i);
            Log.i(TAG, "current currentParticipantId: " + currentParticipantId);
            getCurrentParticipantName(currentParticipantId);
        }
    }


    /**
     * Title of the activity
     */
    private void createTitleView() {
        TextView activityTitle = findViewById(R.id.activity_title_description);
        activityTitle.setText(activity.getTitle());
    }

    /**
     * Number of participants of the activity
     */
    private void createParticipantNumberView(Activity activity) {
        numberParticipantsView = findViewById(R.id.activity_number_description);
        participantNamesView = findViewById(R.id.activity_participants_description);
        numberParticipantsView.setText(activity.getParticipantId().size() + ImageHandler.PATH_SEPARATOR + activity.getNumberParticipant());
        participantNamesView.setText(" participants");
    }

    /**
     * Description of the activity
     */
    private void createDescriptionView() {
        TextView descriptionView = findViewById(R.id.activity_description_description);
        if (activity.getDescription() == null || activity.getDescription().isEmpty()) {
            findViewById(R.id.activity_description_description).setVisibility(View.GONE);
            findViewById(R.id.activity_description_text).setVisibility(View.GONE);
        } else {
            descriptionView.setText(activity.getDescription());
        }
    }

    /**
     * Date fof the activity
     */
    private void createDateView() {
        TextView dateView = findViewById(R.id.activity_date_description);
        String pattern = "MM/dd/yyyy HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(activity.getDate());
        dateView.setText(todayAsString);
    }

    /**
     * Sport of the activity
     */
    private void createSportView() {
        TextView sportView = findViewById(R.id.activity_sport_description);
        sportView.setText(activity.getSport().toString());
    }

    /**
     * Duration of the activity
     */
    private void createDurationView() {
        TextView durationView = findViewById(R.id.activity_duration_description);
        durationView.setText(String.valueOf((int) activity.getDuration()));
    }

    /**
     * Organizer of the activity
     */
    private void createOrganizerView() {
        organizerView = findViewById(R.id.activity_organisator_description);
        organizerView.setText(activity.getOrganizerId());
    }

    /**
     * Address of the activity
     */
    private void createAddressView() {
        TextView addressView = findViewById(R.id.activity_address_description);
        addressView.setText(activity.getAddress());
    }

    /**
     * Registering user to the activity document Firebase Firestore (field array "participantId")
     */
    public void registerToActivity(View v) {
        registerToActivityImplementation(activity, userId);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void registerToActivityImplementation(Activity activity, String userId) {
        if (!activity.getParticipantId().contains(userId)) {
            try {
                activity.addParticipantId(userId);
                createParticipantNumberView(activity);
                // Adding the activity path to the array field "registeredActivity" of the Firebase
                // Firestore user document
                userManager.update(FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId, REGISTERED_ACTIVITY_FIELD, activity.getDocumentPath(), UPDATE_FIELD_UNION);
                // Adding the user ID to the array field "participantId" of the Firebase Firestore
                // activity document
                activityManager.update(activity.getDocumentPath(), PARTICIPANT_ID_FIELD, userId, UPDATE_FIELD_UNION).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, "Participant registered in Firebase Firestore!");
                        getParticipantNames(activity);
                        setButton(activity);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "An error occurred! Participant may be already registered in Firebase Firestore! Exception: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                Toast.makeText(ActivityDescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "An error occurred! Participant may be already registered in Firebase Firestore!");
            }
        } else {
            Toast.makeText(ActivityDescriptionActivity.this, "Already registered!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Unregistering user from the activity document on Firebase Firestore (field array "participantId")
     */
    public void unregisterFromActivity(View v) {
        unregisterFromActivityImplementation(activity, userId, organizerId);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public void unregisterFromActivityImplementation(Activity activity, String userId, String organizerId) {
        if (activity.getParticipantId().contains(userId)) {
            if (!userId.equals(organizerId)) {
                try {
                    activity.removeParticipantId(userId);
                    createParticipantNumberView(activity);
                    // Removing the activity path from the array field "registeredActivity" of the
                    // Firebase Firestore user document
                    userManager.update(FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + userId, REGISTERED_ACTIVITY_FIELD, activity.getDocumentPath(), UPDATE_FIELD_REMOVE);
                    // Removing the user ID from the array field "participantId" of the Firebase
                    // Firestore activity document
                    activityManager.update(activity.getDocumentPath(), PARTICIPANT_ID_FIELD, userId, UPDATE_FIELD_REMOVE).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Log.d(TAG, "Participant unregistered from Firebase Firestore!");
                            getParticipantNames(activity);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "An error occurred! Participant may be already unregistered from Firebase Firestore! Exception: " + e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(ActivityDescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "An error occurred! Participant may be already unregistered from Firebase Firestore!");
                }
            } else {
                Toast.makeText(ActivityDescriptionActivity.this, "The organizer cannot unregister from his activity!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ActivityDescriptionActivity.this, "Not registered yet!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Allowing the access to the chat of the activity only if the user is registered to the activity
     */
    public void goToIndividualChat(View view) {
        if (activity.getParticipantId().contains(userId)) {
            Intent intent = new Intent(ActivityDescriptionActivity.this, ChatActivity.class);
            String activityDocumentPath = activity.getDocumentPath();
            activityDocumentPath = activityDocumentPath.replace(FirestoreActivityManager.ACTIVITIES_COLLECTION + ImageHandler.PATH_SEPARATOR, "");
            intent.putExtra(ACTIVITY_CHAT_ID, activityDocumentPath);
            String activityTitle = activity.getTitle();
            intent.putExtra(ACTIVITY_TITLE, activityTitle);
            startActivity(intent);
        } else {
            Toast.makeText(ActivityDescriptionActivity.this, "Please register if you want to access the chat!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Goes to the GPS recording activity
     */
    public void goToGPSRecording(View view) {
        Intent intent = new Intent(ActivityDescriptionActivity.this, GPSRecordingActivity.class);
        intent.putExtra(RECORDING_EXTRA_NAME, activity);
        startActivity(intent);
    }

    /**
     * Display the participant's GPS recording stats
     */
    public void displayParticipantStats() {
        GPSPath stats = activity.getParticipantRecordings().get(userId);
        TextView distText = findViewById(R.id.activity_description_dist_data);
        distText.setText(new DecimalFormat("#.##").format(stats.getDistance()) + DISTANCE_UNIT);

        TextView avgSpeedText = findViewById(R.id.activity_description_avgSpeed_data);
        avgSpeedText.setText(new DecimalFormat("#.##").format(stats.getAverageSpeed()) + SPEED_UNIT);

        TextView timeText = findViewById(R.id.activity_description_time_data);

        Date date = new Date(stats.getTime());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        timeText.setText(formatter.format(date));
    }

    /**
     * Fetch the name of the organizer from Firebase Firestore
     */
    private void getOrganizerName() {
        organizerId = activity.getOrganizerId();
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + organizerId);
        document.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String organizerFullName = (String) document.getData().get("fullName");
                        organizerView.setText(organizerFullName);
                        Log.i(TAG, "Organizer name: " + organizerFullName);
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "Get failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Fetch the name of a participant from Firebase Firestore using his userId
     */
    private void getCurrentParticipantName(String participantId) {
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + ImageHandler.PATH_SEPARATOR + participantId);
        document.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String participantName = (String) document.getData().get("fullName");
                        participantNamesString.append(participantName).append(", ");
                        participantNamesView.setText(" participants" + " (" + participantNamesString + ")");
                        Log.i(TAG, "current participantName: " + participantName);
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "Get failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Load the dedicated picture of the activity
     */
    private void loadActivityHeaderPicture() {
        activityImage = findViewById(R.id.activity_image_description);
        progressBar = findViewById(R.id.progress_bar_activity_description);
        imagePath = activity.getDocumentPath() + ImageHandler.PATH_SEPARATOR + ImageHandler.ACTIVITY_IMAGE_NAME;
        Image image = new Image(null, activityImage);
        image.setDocumentPath(imagePath);
        ImageHandler.loadImage(image, this);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }


    /**
     * Launch the Gallery to select a header picture for the activity
     */
    public void changeActivityPicture(View view) {
        if (userId.equals(organizerId)) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, REQUEST_IMAGE);
        } else {
            Toast.makeText(ActivityDescriptionActivity.this, "Only the organizer can change the header picture!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                Image image = new Image(imageUri, activityImage);
                image.setDocumentPath(imagePath);
                ImageHandler.uploadImage(image, this);
            }
        }
    }

}