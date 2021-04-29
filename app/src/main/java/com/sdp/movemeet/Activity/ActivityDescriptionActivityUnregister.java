package com.sdp.movemeet.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ActivityDescriptionActivityUnregister extends AppCompatActivity {

    FirebaseAuth fAuth;
    private Activity act;
    private static final String TAG = "ActDescActivity";
    StorageReference storageReference;
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

        createTitleView();
        createParticipantNumberView();
        createDescriptionView();
        createDateView();
        createAddressView();
        createSportView();
        createDurationView();
        loadActivityHeaderPicture();
    }

    public void goToLogin(View v) {
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // sending the user to the "Login" activity
            finish();
        }
    }

    private void createTitleView() {
        // activityTitle from the activity
        TextView activityTitle = (TextView) findViewById(R.id.activity_title_description);
        if (act != null) activityTitle.setText(act.getTitle());
    }

    private void createParticipantNumberView() {
        // number of participants from the activity
        TextView numberParticipantsView = (TextView) findViewById(R.id.activity_number_description);
        if (act != null) {
            numberParticipantsView.setText(act.getParticipantId().size() + "/" + act.getNumberParticipant());
        }
    }

    private void createDescriptionView() {
        // description from the activity
        TextView descriptionView = (TextView) findViewById(R.id.activity_description_description);
        if (act != null) descriptionView.setText(act.getDescription());
    }

    private void createDateView() {
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

    private void createAddressView() {
        // address from the activity
        TextView addressView = (TextView) findViewById(R.id.activity_address_description);
        if (act != null) {
            addressView.setText(act.getAddress());
        }
    }

    private void loadActivityHeaderPicture() {
        activityImage = findViewById(R.id.activity_image_description);
        progressBar = findViewById(R.id.progress_bar_activity_description);
        progressBar.setVisibility(View.VISIBLE);
        storageReference = FirebaseStorage.getInstance().getReference();
        if (act != null) {
            imagePath = "activities/" + act.getActivityId() + "/activityImage.jpg";
            StorageReference imageRef = storageReference.child(imagePath);
            FirebaseInteraction.getImageFromFirebase(imageRef, activityImage, progressBar);
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