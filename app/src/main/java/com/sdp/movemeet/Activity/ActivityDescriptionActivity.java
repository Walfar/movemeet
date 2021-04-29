package com.sdp.movemeet.Activity;

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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.Navigation.Navigation;
import com.sdp.movemeet.R;
import com.sdp.movemeet.chat.ChatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityDescriptionActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    private Activity act;
    private static final String TAG = "ActDescActivity";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    TextView fullName, email, phone, organizerView, numberParticipantsView;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    String organizerId;
    String fullNameString;
    ArrayList<String> participantNames = new ArrayList<>();
    StringBuilder participantNamesString = new StringBuilder();

    ImageView activityImage;
    String imagePath;
    ProgressBar progressBar;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();

        if (intent != null) {
            act = (Activity) intent.getSerializableExtra("activity");
        }

        uri = intent.getData();
        if(uri != null){
            loadActivityHeaderPicture();
        }

        createTitleView();
        createParticipantNumberView();
        createDescriptionView();
        createDateView();
        createAddressView();
        createSportView();
        createDurationView();
        createOrganizerView();

        createDrawer();
        handleRegisterUser();
        getOrganizerName();
        getParticipantNames();

        //The aim is to block any direct access to this page if the user is not logged
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }

    private void getParticipantNames() {
        if (act != null) {
            ArrayList<String> participantIds = act.getParticipantId();
            participantNames = new ArrayList<>();
            participantNamesString = new StringBuilder();
            for (int i = 0; i < act.getParticipantId().size(); i++) {
                String currentParticipantId = participantIds.get(i);
                getCurrentParticipantName(currentParticipantId);
            }
        }
    }

    public void createDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        View hView = navigationView.inflateHeaderView(R.layout.header);

        fullName = hView.findViewById(R.id.text_view_profile_name);
        phone = hView.findViewById(R.id.text_view_profile_phone);
        email = hView.findViewById(R.id.text_view_profile_email);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(R.id.nav_add_activity);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Navigation.goToHome(this.navigationView);
                finish();
                break;
            case R.id.nav_edit_profile:
                Navigation.goToUserProfileActivity(this.navigationView);
                finish();
                break;
            case R.id.nav_add_activity:
                Navigation.goToActivityUpload(this.navigationView);
                finish();
                break;
            case R.id.nav_logout:
                FirebaseInteraction.logoutIfUserNonNull(fAuth, this);
                finish();
                break;
            case R.id.nav_start_activity:
                break;
            case R.id.nav_chat:
                Navigation.goToChat(this.navigationView);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void handleRegisterUser() {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            TextView[] textViewArray = {fullName, email, phone};
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ActivityDescriptionActivity.this);
        }
    }

    public void logout(View view) {
        if (fAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }


    private void createTitleView() {
        // Title of the activity
        TextView activityTitle = (TextView) findViewById(R.id.activity_title_description);
        if (act != null) activityTitle.setText(act.getTitle());
    }

    private void createParticipantNumberView() {
        // Number of participants of the activity
        numberParticipantsView = (TextView) findViewById(R.id.activity_number_description);
        if (act != null) {
            numberParticipantsView.setText(act.getParticipantId().size() + "/" + act.getNumberParticipant());
        }
    }

    private void createDescriptionView() {
        // Description of the activity
        TextView descriptionView = (TextView) findViewById(R.id.activity_description_description);
        if (act != null) descriptionView.setText(act.getDescription());
    }

    private void createDateView(){
        // date from the activity
        TextView dateView = (TextView) findViewById(R.id.activity_date_description);
        if (act != null) {
            String pattern = "MM/dd/yyyy HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);
            String todayAsString = df.format(act.getDate());
            dateView.setText(todayAsString);
        }
    }

    private void createSportView() {
        // Type of sport of the activity
        TextView sportView = (TextView) findViewById(R.id.activity_sport_description);
        if (act != null) {
            sportView.setText(act.getSport().toString());
        }
    }

    private void createDurationView() {
        TextView durationView = (TextView) findViewById(R.id.activity_duration_description);
        if (act != null) {
            durationView.setText(String.valueOf((int) act.getDuration()));
        }
    }

    private void createOrganizerView() {
        // Organizer of the activity
        organizerView = (TextView) findViewById(R.id.activity_organisator_description);
        if (act != null) {
            organizerView.setText(act.getOrganizerId());
        }
    }

    private void createAddressView() {
        // Address of the activity
        TextView addressView = (TextView) findViewById(R.id.activity_address_description);
        if (act != null) {
            addressView.setText(act.getAddress());
        }
    }

    public void registerToActivity(View v) {
        // Syncing registered participant to Firebase Firestore (field array "participantId")
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            String userId;
            userId = fAuth.getCurrentUser().getUid();
            try {
                act.addParticipantId(userId);
                createParticipantNumberView();
                fStore = FirebaseFirestore.getInstance();
                DocumentReference actRef = fStore.collection("activities").document(act.getDocumentPath());
                actRef.update("participantId", FieldValue.arrayUnion(userId));
                Log.d(TAG, "Participant registered in Firebase Firestore!");
                getParticipantNames();
            } catch (Exception e) {
                Toast.makeText(ActivityDescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "An error occurred! Participant may be already registered in Firebase Firestore!");
            }
        }
    }

    public void goToIndividualChat(View view) {
        // Allowing the access to the chat of the activity only if the user is registered to the activity
        if (act.getParticipantId().contains(userId)) {
            Intent intent = new Intent(ActivityDescriptionActivity.this, ChatActivity.class);
            String activityChatId = act.getActivityId() + " - chatId";
            intent.putExtra("ACTIVITY_CHAT_ID", activityChatId);
            String activityTitle = act.getTitle();
            intent.putExtra("ACTIVITY_TITLE", activityTitle);
            startActivity(intent);
        } else {
            Toast.makeText(ActivityDescriptionActivity.this, "Please register if you want to access the chat!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrganizerName() {
        // Fetch the name of the organizer from Firebase Firestore
        if (act != null) {
            organizerId = act.getOrganizerId();
            DocumentReference docRef = fStore.collection("users").document(organizerId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            fullNameString = (String) document.getData().get("fullName");
                            organizerView.setText(fullNameString);
                            Log.i(TAG, "fullNameString: " + fullNameString);
                        } else {
                            Log.d(TAG, "No such document!");
                        }
                    } else {
                        Log.d(TAG, "Get failed with: ", task.getException());
                    }
                }
            });
        }
    }

    private void getCurrentParticipantName(String participantId) {
        // Fetch the name of a participant from Firebase Firestore using his userId
        DocumentReference docRef = fStore.collection("users").document(participantId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String participantName = (String) document.getData().get("fullName");
                        participantNames.add(participantName);
                        // Add participant name to the
                        participantNamesString.append(participantName).append(", ");
                        numberParticipantsView.setText(act.getParticipantId().size() + "/" + act.getNumberParticipant() + " (" + participantNamesString + ")");
                        Log.i(TAG, "current participantName: " + participantName);
                        //createParticipantNumberView();
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "Get failed with: ", task.getException());
                }
            }
        });
    }

    private void loadActivityHeaderPicture() {
        // Load the dedicated picture of the activity
        activityImage = findViewById(R.id.activity_image_description);
        progressBar = findViewById(R.id.progress_bar_activity_description);
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();
        if (act != null) {
            imagePath = "activities/" + act.getActivityId() + "/activityImage.jpg";
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
    }

    public void changeActivityPicture(View view) {
        // Launch the Gallery to select a header picture for the activity
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