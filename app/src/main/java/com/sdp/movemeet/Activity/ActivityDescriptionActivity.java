package com.sdp.movemeet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.sdp.movemeet.R;
import com.sdp.movemeet.Sport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityDescriptionActivity extends AppCompatActivity {

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

        act = (Activity) getIntent().getSerializableExtra("activity");

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



}