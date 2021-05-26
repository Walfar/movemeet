package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Image;
import com.sdp.movemeet.utility.ImageHandler;
import com.sdp.movemeet.view.home.LoginActivity;

/**
 * Activity description for unregistered user. An unregistered user can't see all information of an activity,
 * he cannot see the organizer name, the date and participants. If the user want to know more about this activity, there
 * is a button for sign up in movemeet
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
public class ActivityDescriptionActivityUnregister extends AppCompatActivity {

    public static final String ACTIVITY_IMAGE_NAME = "activityImage.jpg";

    private Activity activity;
    private static final String TAG = "ActDescActivity";
    private FirebaseStorage fStorage;
    private StorageReference storageReference;
    private ImageView activityImage;
    private String imagePath;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_unregister);

        Intent intent = getIntent();

        if (intent != null) {
            activity = (Activity) intent.getSerializableExtra(ActivityDescriptionActivity.DESCRIPTION_ACTIVITY_KEY);
            loadActivityHeaderPicture();
        }

        fStorage = BackendInstanceProvider.getStorageInstance();
        storageReference = fStorage.getReference();

        if (activity != null) {
            displayDescriptionActivityData();
        }
    }

    public void displayDescriptionActivityData() {
        createTitleView();
        createParticipantNumberView();
        createDescriptionView();
        createAddressView();
        createSportView();
        createDurationView();
    }

    public void goToLogin(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
        finish();
    }

    /**
     * Title from the activity
     */
    private void createTitleView() {
        TextView activityTitle = (TextView) findViewById(R.id.activity_title_description);
        activityTitle.setText(activity.getTitle());
    }

    /**
     * Number of participants from the activity
     */
    private void createParticipantNumberView() {
        TextView numberParticipantsView = (TextView) findViewById(R.id.activity_number_description);
        numberParticipantsView.setText(activity.getParticipantId().size() + "/" + activity.getNumberParticipant());
    }

    /**
     * Description from the activity
     */
    private void createDescriptionView() {
        TextView descriptionView = (TextView) findViewById(R.id.activity_description_description);
        descriptionView.setText(activity.getDescription());
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
     * Address of the activity
     */
    private void createAddressView() {
        TextView addressView = (TextView) findViewById(R.id.activity_address_description);
        addressView.setText(activity.getAddress());
    }

    /**
     * Image of the activity
     */
    private void loadActivityHeaderPicture() {
        activityImage = findViewById(R.id.activity_image_description);
        progressBar = findViewById(R.id.progress_bar_activity_description);
        imagePath = activity.getDocumentPath() + "/" + ACTIVITY_IMAGE_NAME;
        Image image = new Image(null, activityImage);
        image.setDocumentPath(imagePath);
        ImageHandler.loadImage(image, progressBar);
    }

}