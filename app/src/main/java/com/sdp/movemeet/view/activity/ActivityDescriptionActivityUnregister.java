package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.view.home.LoginActivity;

/**
 * Activity description for unregistered user. An unregistered user can't see all information of an activity,
 * he cannot see the organiser name, the date and participants. If the user want to know more about this activity, there
 * is a button for sign up in movemeet
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
public class ActivityDescriptionActivityUnregister extends AppCompatActivity {

    Activity activity;
    private static final String TAG = "ActDescActivity";
    StorageReference storageReference;
    ImageView activityImage;
    String imagePath;
    ProgressBar progressBar;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_unregister);

        Intent intent = getIntent();

        if (intent != null) {
            activity = (Activity) intent.getSerializableExtra("activity");
        }

        uri = intent.getData();
        if (uri != null) {
            loadActivityHeaderPicture();
        }

        storageReference = FirebaseStorage.getInstance().getReference();

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
        progressBar.setVisibility(View.VISIBLE);
        if (activity != null) {
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