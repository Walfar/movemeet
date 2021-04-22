package com.sdp.movemeet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.Navigation.Navigation;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.chat.ChatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityDescriptionActivity extends AppCompatActivity {

    public final static String EXTRA_ACTIVITY_ID = "12345";
    public final static String EXTRA_ORGANISATOR_ID = "1";
    public final static String EXTRA_TITLE = "title";
    public final static int EXTRA_NUMBER_PARTICIPANT = 2;
    public final static ArrayList<String> EXTRA_PARTICIPANTS_ID = new ArrayList<String>();
    public final static double EXTRA_LONGITUDE = 2.45;
    public final static double EXTRA_LATITUDE = 3.697;
    public final static String EXTRA_DESCRIPTION = "description";
    public final static Date EXTRA_DATE = new Date(2021, 11, 10, 1, 10);
    public final static double EXTRA_DURATION = 10.4;
    public final static Sport EXTRA_SPORT = Sport.Running;
    public final static String EXTRA_ADDRESS = "address";

    FirebaseAuth fAuth;
    Button RegisterToActivityButton;
    private Activity act;
    private static final String TAG = "ActDescActivity";
    private FirebaseUser user;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;

    TextView fullName, email, phone, organizerView;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    String organizerId;
    String fullNameString;

    ImageView activityImage;
    String imagePath;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();

        if (intent != null) {
            act = (Activity) intent.getSerializableExtra("activity");
        }

        /*fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        if(user != null){
            userId = act.getOrganizerId();
            fStore = FirebaseFirestore.getInstance();
            getUserName();
        }*/


        createTitleView();
        createParticipantNumberView();
        createDescriptionView();
        createDateView();
        createAddressView();
        createSportView();
        createDurationView();
        createOrganizerView();
        loadActivityHeaderPicture();

        createDrawer();
        handleRegisterUser();
        getOrganizerName();

        //The aim is to block any direct access to this page if the user is not logged
        //Smth must be wrong since it prevents automatic connection during certain tests
        /*if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }*/
    }


    public void createDrawer(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        textView=findViewById(R.id.textView);
        toolbar=findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        View hView =  navigationView.inflateHeaderView(R.layout.header);

        fullName = hView.findViewById(R.id.text_view_profile_name);
        phone = hView.findViewById(R.id.text_view_profile_phone);
        email = hView.findViewById(R.id.text_view_profile_email);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigationView.setCheckedItem(R.id.nav_start_activity);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Navigation.goToHome(this.navigationView);
                break;
            case R.id.nav_edit_profile:
                Navigation.goToUserProfileActivity(this.navigationView);
                break;
            case R.id.nav_add_activity:
                Navigation.goToActivityUpload(this.navigationView);
                break;
            case R.id.nav_logout:
                FirebaseInteraction.logoutIfUserNonNull(fAuth, this);
                break;
            case R.id.nav_start_activity:
                break;
            case R.id.nav_chat:
                Navigation.goToChat(this.navigationView);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
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

/*
    private void getUserName() {
        DocumentReference docRef = fStore.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        fullNameString = (String) document.getData().get("fullName");
                        createOrganizerView();
                        Log.i(TAG, "fullNameString: " + fullNameString);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }*/

    private void createTitleView() {
        // activityTitle from the activity
        TextView activityTitle = (TextView) findViewById(R.id.activity_title_description);
        if (act != null) activityTitle.setText(act.getTitle());
    }

    private void createParticipantNumberView(){
        // number of participants from the activity
        TextView numberParticipantsView = (TextView) findViewById(R.id.activity_number_description);
        if (act != null) {
            numberParticipantsView.setText(act.getParticipantId().size() + "/" + act.getNumberParticipant());
        }
    }

    private void createDescriptionView(){
        // description from the activity
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

    private void createSportView(){
        TextView sportView = (TextView) findViewById(R.id.activity_sport_description);
        if(act != null){
            sportView.setText(act.getSport().toString());
        }
    }

    private void createDurationView(){
        TextView durationView = (TextView) findViewById(R.id.activity_duration_description);
        if(act != null){
            durationView.setText(String.valueOf((int) act.getDuration()));
        }
    }

    private void createOrganizerView(){
        organizerView = (TextView) findViewById(R.id.activity_organisator_description);
        if(act != null){
            organizerView.setText(act.getOrganizerId());
        }
    }

    private void createAddressView(){
        // address from the activity
        TextView addressView = (TextView) findViewById(R.id.activity_address_description);
        if (act != null) {
            addressView.setText(act.getAddress());
        }
    }

    public void onClick(View v) {
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            String userId;
            userId = fAuth.getCurrentUser().getUid();
            try{
                act.addParticipantId(userId);
                createParticipantNumberView();}
            catch(Exception e){
                Toast.makeText(ActivityDescriptionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToIndividualChat(View view) {
        // Allowing the access to the chat only if the user is registered to the activity
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
        organizerId = act.getOrganizerId();
        getUserName();
    }

    private void getUserName() {
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
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void loadActivityHeaderPicture() {
        activityImage = findViewById(R.id.activity_image_description);
        progressBar = findViewById(R.id.progress_bar_activity_description);
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();
        if (act != null) {
            imagePath = "activities/"+act.getActivityId()+"/activityImage.jpg";
            StorageReference imageRef = storageReference.child(imagePath);
            FirebaseInteraction.getImageFromFirebase(imageRef, activityImage, progressBar);
        }
    }

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
            if(resultCode == android.app.Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                progressBar.setVisibility(View.VISIBLE);
                FirebaseInteraction.uploadImageToFirebase(storageReference, imagePath, imageUri, activityImage, progressBar);
            }
        }
    }

    /*public void goToHome(View view){
        Intent i = new Intent(ActivityDescriptionActivity.this, HomeScreenActivity.class);
        i.putExtra(EXTRA_ACTIVITY_ID, "1");
        i.putExtra(EXTRA_ORGANISATOR_ID, "1");
        i.putExtra(EXTRA_ADDRESS, "1");
        i.putExtra(String.valueOf(EXTRA_DURATION), 1.0);
        i.putExtra(EXTRA_DESCRIPTION, "1");
        i.putExtra(EXTRA_TITLE, "1");
        i.putExtra(String.valueOf(EXTRA_DATE), "1");
        i.putExtra(String.valueOf(EXTRA_LATITUDE), 1.0);
        i.putExtra(String.valueOf(EXTRA_LONGITUDE), 1.0);
        i.putExtra(String.valueOf(EXTRA_NUMBER_PARTICIPANT), 1.0);
        i.putExtra(String.valueOf(EXTRA_SPORT), "1.0");

        startActivity(i);
    }*/
}