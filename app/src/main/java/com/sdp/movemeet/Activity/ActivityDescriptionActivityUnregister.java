package com.sdp.movemeet.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.R;

public class ActivityDescriptionActivityUnregister extends AppCompatActivity {

    FirebaseAuth fAuth;
    private Activity act;
    private static final String TAG = "ActDescActivity";
    StorageReference storageReference;
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
        createAddressView();
        createSportView();
        createDurationView();
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