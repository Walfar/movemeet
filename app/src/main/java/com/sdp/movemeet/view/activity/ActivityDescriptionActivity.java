package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.chat.ChatActivity;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/***
 * Activity for show the description of an activity. Informations about an activity are : sport, date and time, time estimate, organiser,
 * a list of participants, a picture, address, and description. A user can register to an activity, and access to the chat.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
public class ActivityDescriptionActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    private static final String TAG = "ActDescActivity";
    FirestoreActivityManager firestoreManager;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    TextView fullName, email, phone, organizerView, numberParticipantsView, participantNamesView;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    String organizerId;
    ArrayList<String> participantNames = new ArrayList<>();
    StringBuilder participantNamesString = new StringBuilder();

    ImageView activityImage;
    String imagePath;
    ProgressBar progressBar;
    Uri uri;

    BackendManager<Activity> activityManager;
    BackendManager<User> userManager;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();

        if (intent != null) {
            activity = (Activity) intent.getSerializableExtra("activity");
            uri = intent.getData();
            if (uri != null) {
                loadActivityHeaderPicture();
            }
        }

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        userManager = new FirestoreUserManager(fStore, FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        activityManager = new FirestoreActivityManager(fStore, FirestoreActivityManager.ACTIVITIES_COLLECTION, new ActivitySerializer());


        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        Navigation nav = new Navigation(this, R.id.nav_start_activity);
        nav.createDrawer();

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
    }

    private void getParticipantNames() {
        ArrayList<String> participantIds = activity.getParticipantId();
        participantNames = new ArrayList<>();
        participantNamesString = new StringBuilder();
        for (int i = 0; i < participantIds.size(); i++) {
            String currentParticipantId = participantIds.get(i);
            getCurrentParticipantName(currentParticipantId);
        }
    }

    public void logout(View view) {
        if (userId != null) {
            fAuth.signOut(); // this will do the logout of the user from Firebase
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
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
        if (userId != null) {
            activity.addParticipantId(userId);
            createParticipantNumberView();
            // check if this is good
            activityManager.add(activity, activity.getDocumentPath()).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d(TAG, "Participant registered in Firebase Firestore!");
                    getParticipantNames();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "An error occurred! Participant may be already registered in Firebase Firestore!");
                }
            });
        }
    }

    /**
     * Allowing the access to the chat of the activity only if the user is registered to the activity
     */
    public void goToIndividualChat(View view) {
        if (activity.getParticipantId().contains(userId)) {
            Intent intent = new Intent(ActivityDescriptionActivity.this, ChatActivity.class);
            String activityChatId = activity.getActivityId() + " - chatId";
            intent.putExtra("ACTIVITY_CHAT_ID", activityChatId);
            String activityTitle = activity.getTitle();
            intent.putExtra("ACTIVITY_TITLE", activityTitle);
            startActivity(intent);
        } else {
            Toast.makeText(ActivityDescriptionActivity.this, "Please register if you want to access the chat!", Toast.LENGTH_SHORT).show();
        }
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
                        String organiserFullName = (String) document.getData().get("fullName");
                        organizerView.setText(organiserFullName);
                        Log.i(TAG, "organiser name: " + organiserFullName);
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
                        participantNames.add(participantName);
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
        progressBar.setVisibility(View.VISIBLE);

        imagePath = "activities/" + activity.getActivityId() + "/activityImage.jpg";
        StorageReference imageRef = storageReference.child(imagePath);
        imageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Get image: SUCCESS");
                    FirebaseInteraction.getImageFromFirebase(imageRef, activityImage, progressBar);
                } else {
                    Log.d(TAG, "Activity have no image");
                    activityImage.setImageAlpha(R.drawable.run_woman);
                }
            }
        });
    }

    /**
     * Launch the Gallery to select a header picture for the activity
     */
    public void changeActivityPicture(View view) {
        if (userId.equals(organizerId)) {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        } else {
            Toast.makeText(ActivityDescriptionActivity.this, "Only the organizer can change the header picture!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                progressBar.setVisibility(View.VISIBLE);
                FirebaseInteraction.uploadImageToFirebase(storageReference, imagePath, imageUri, activityImage, progressBar);
            }
        }
    }
}