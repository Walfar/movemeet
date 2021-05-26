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
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.map.GPSRecordingActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/***
 * Activity for show the description of an activity. Informations about an activity are : sport, date and time, time estimate, organizer,
 * a list of participants, a picture, address, and description. A user can register to an activity, and access to the chat.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
public class ActivityDescriptionActivity extends AppCompatActivity {

    private static final String TAG = "ActDescActivity";
    public static final String DESCRIPTION_ACTIVITY_KEY = "activitykey";
    public static final String RECORDING_EXTRA_NAME = "gpsreckey";

    @VisibleForTesting(otherwise=VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    public static final String ACTIVITY_IMAGE_NAME = "activityImage.jpg";
    private static final int REQUEST_IMAGE = 1000;

    private TextView organizerView, numberParticipantsView, participantNamesView;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;
    private StorageReference storageReference;
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
            fStorage = BackendInstanceProvider.getStorageInstance();
            storageReference = fStorage.getReference();
            fStore = BackendInstanceProvider.getFirestoreInstance();
            userManager = new FirestoreUserManager(fStore, FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
            activityManager = new FirestoreActivityManager(fStore, FirestoreActivityManager.ACTIVITIES_COLLECTION, new ActivitySerializer());
        }

        if(enableNav) new Navigation(this, R.id.nav_home).createDrawer();

        Intent intent = getIntent();

        if (intent != null) {
            activity = (Activity) intent.getSerializableExtra("activity");
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
        createParticipantNumberView();
        getParticipantNames();
        loadActivityHeaderPicture();

        findViewById(R.id.activityGPSRecDescription).setEnabled(activity.getSport() == Sport.Running);
    }

    private void getParticipantNames() {
        ArrayList<String> participantIds = activity.getParticipantId();
        Log.i(TAG, "activity.getParticipantId(): " + activity.getParticipantId());
        Log.i(TAG, "activity.getDocumentPath(): " + activity.getDocumentPath());
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
        TextView activityTitle = (TextView) findViewById(R.id.activity_title_description);
        activityTitle.setText(activity.getTitle());
    }

    /**
     * Number of participants of the activity
     */
    private void createParticipantNumberView() {
        numberParticipantsView = (TextView) findViewById(R.id.activity_number_description);
        participantNamesView = (TextView) findViewById(R.id.activity_participants_description);
        numberParticipantsView.setText(activity.getParticipantId().size() + "/" + activity.getNumberParticipant());
        participantNamesView.setText(" participants");
    }

    /**
     * Description of the activity
     */
    private void createDescriptionView() {
        TextView descriptionView = (TextView) findViewById(R.id.activity_description_description);
        descriptionView.setText(activity.getDescription());
    }

    /**
     * Date fof the activity
     */
    private void createDateView() {
        TextView dateView = (TextView) findViewById(R.id.activity_date_description);
        String pattern = "MM/dd/yyyy HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(activity.getDate());
        dateView.setText(todayAsString);
    }

    /**
     * Sport of the activity
     */
    private void createSportView() {
        TextView sportView = (TextView) findViewById(R.id.activity_sport_description);
        sportView.setText(activity.getSport().toString());
    }

    /**
     * Duration of the activity
     */
    private void createDurationView() {
        TextView durationView = (TextView) findViewById(R.id.activity_duration_description);
        durationView.setText(String.valueOf((int) activity.getDuration()));
    }

    /**
     * Organizer of the activity
     */
    private void createOrganizerView() {
        organizerView = (TextView) findViewById(R.id.activity_organisator_description);
        organizerView.setText(activity.getOrganizerId());
    }

    /**
     * Address of the activity
     */
    private void createAddressView() {
        TextView addressView = (TextView) findViewById(R.id.activity_address_description);
        addressView.setText(activity.getAddress());
    }

    /**
     * Syncing registered participant to Firebase Firestore (field array "participantId")
     */
    public void registerToActivity(View v) {
        userId = fAuth.getCurrentUser().getUid();
        if (userId != null) {
            try {
                activity.addParticipantId(userId);
                createParticipantNumberView();
                activityManager.update(activity.getDocumentPath(), "participantId", userId).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d(TAG, "Participant registered in Firebase Firestore!");
                        getParticipantNames();
                        // TODO: (By Victor) here --> get activity from the MainMapFragment and update it!
                        //  (in order to sync the Firebase Firestore new updates with the local sport activities and their views)
                        //  (because if we register, exit ActivityDescriptionActivity and then re-enter ActivityDescriptionActivity,
                        //  then the Firebase Firestore backend works and is updated, but the local activity is not updated according
                        //  to this version of the backend)
                        //  Probable solution:
                        //  Implement a method in the OnResume of MainMapFragment to clear the list of activities
                        //  and then update them from Firebase Firestore (but this is not very optimal, because it takes time)
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
        }
    }

    /**
     * Allowing the access to the chat of the activity only if the user is registered to the activity
     */
    public void goToIndividualChat(View view) {
        userId = fAuth.getCurrentUser().getUid();
        if (activity.getParticipantId().contains(userId)) {
            Intent intent = new Intent(ActivityDescriptionActivity.this, ChatActivity.class);
            String activityDocumentPath = activity.getDocumentPath();
            activityDocumentPath = activityDocumentPath.replace("activities/", "");
            intent.putExtra("ACTIVITY_CHAT_ID", activityDocumentPath);
            String activityTitle = activity.getTitle();
            intent.putExtra("ACTIVITY_TITLE", activityTitle);
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
        //finish();
    }

    /**
     * Fetch the name of the organizer from Firebase Firestore
     */
    private void getOrganizerName() {
        organizerId = activity.getOrganizerId();
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + "/" + organizerId);
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
        Task<DocumentSnapshot> document = (Task<DocumentSnapshot>) userManager.get(FirestoreUserManager.USERS_COLLECTION + "/" + participantId);
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
        imagePath = activity.getDocumentPath() + "/" + ACTIVITY_IMAGE_NAME;
        Image image = new Image(null, activityImage);
        image.setDocumentPath(imagePath);
        ImageHandler.loadImage(image, progressBar);
    }

    /**
     * Launch the Gallery to select a header picture for the activity
     */
    public void changeActivityPicture(View view) {
        userId = fAuth.getCurrentUser().getUid();
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
                ImageHandler.uploadImage(image, progressBar);
            }
        }
    }

}