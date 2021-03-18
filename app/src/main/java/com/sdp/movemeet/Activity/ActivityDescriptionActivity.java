package com.sdp.movemeet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.sdp.movemeet.FirebaseUsersMainActivity;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;
import com.sdp.movemeet.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityDescriptionActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    Button RegisterToActivityButton;
    private Activity act = new Activity("activityId",
            "organizerId",
            "Snowboard",
            5,
            new ArrayList<>(),
            004,
            005,
            "BlaBlaBla Description de l'activité",
            new Date(),
            90,
            Sport.Running,
            "address");

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
    }

    private void createTitleView() {
        // activityTitle from the activity
        TextView activityTitle = (TextView) findViewById(R.id.activityTitle);
        activityTitle.setText(act.getTitle());
    }

    private void createParticipantNumberView(){
        // number of participants from the activity
        TextView numberParticipants = (TextView) findViewById(R.id.numberParticipant);
        numberParticipants.setText("Participants : " + act.getParticipantId().size() + "/" + act.getNumberParticipant());
    }

    private void createDescriptionView(){
        // description from the activity
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(act.getDescription());
    }

    private void createDateView(){
        // date from the activity
        TextView date = (TextView) findViewById(R.id.date);
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(act.getDate());
        date.setText(todayAsString);
    }

    private void createAddressView(){
        // address from the activity
        TextView address = (TextView) findViewById(R.id.address);
        address.setText(act.getAddress());
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
                Toast.makeText(ActivityDescriptionActivity.this, "Already Register", Toast.LENGTH_SHORT).show();
            }
        }
    }

}