package com.sdp.movemeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Backend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.Activity.Activity;
import com.sdp.movemeet.Backend.BackendActivityManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UploadActivityActivity extends AppCompatActivity {

    private Spinner spinner;

    private Calendar calendar;
    private EditText startTimeText;
    private EditText dateText;
    private int year, month, day;



    private EditText durationText;
    private int hours = 0;
    private int minutes = 0;

    private Geocoder geocoder;
    private EditText addressText;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_activitiy);

        setupSportSpinner(this);

        setupDateInput(this);

        setupStartTimeInput(this);

        setupDurationInput(this);

        setupAddressInput(this);
    }

    private void setupSportSpinner(Context context) {
        spinner = (Spinner) findViewById(R.id.spinnerSportType);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, Sport.values());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }





    // Helper methods for date picker

    private void setupDateInput(Context context) {
        dateText = findViewById(R.id.editTextDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month+1);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            showDate(year, month+1, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day) {
        dateText.setText(day + "." + month + "." + year);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }



    // Helper methods for start time picker
    private void setupStartTimeInput(Context context) {
        startTimeText = findViewById(R.id.editTextStartTime);
    }

    private TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            showStartTime(hourOfDay, minute);
        }
    };

    private void showStartTime(int hours, int minutes) {
        startTimeText.setText(hours + ":" + ((minutes < 10)?"0" + minutes:minutes));
    }

    @SuppressWarnings("deprecation")
    public void setStartTime(View view) {
        showDialog(222);
    }



    // Helper methods for duration picker
    private void setupDurationInput(Context context) {
        durationText = findViewById(R.id.editTextTime);
    }

    private TimePickerDialog.OnTimeSetListener durationListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showDuration(hourOfDay, minute);
        }
    };

    private void showDuration(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        durationText.setText(hours + ":" + ((minutes < 10)?"0" + minutes:minutes));
    }

    @SuppressWarnings("deprecation")
    public void setDuration(View view) {
        showDialog(444);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    dateListener, year, month, day);
        } else if (id == 444) {
            return new TimePickerDialog(this,
                    durationListener, hours, minutes, false);
        } else if (id == 222) {
            return new TimePickerDialog(this,
                    startTimeListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );
        }
        return null;
    }





    // Helper methods for address geocoding

    private void setupAddressInput(Context context) {
        geocoder = new Geocoder(context);

        addressText = findViewById(R.id.editTextLocation);
        addressText.setOnEditorActionListener(addressListener);
    }

    private TextView.OnEditorActionListener addressListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch(actionId) {
                case EditorInfo.IME_ACTION_DONE:
                case EditorInfo.IME_ACTION_NEXT:
                case EditorInfo.IME_ACTION_PREVIOUS:
                    String txt = addressText.getText().toString();
                    try {
                        List<Address> address = geocoder.getFromLocationName(txt, 1);
                        if (address.size() > 0) {
                            latitude = address.get(0).getLatitude();
                            longitude = address.get(0).getLongitude();
                            Toast.makeText(getApplicationContext(), "Found address!", Toast.LENGTH_SHORT).show();
                        } else {
                            throw new IOException();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Error: Could not find address!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        }
    };

    public void confirmActivityUpload(View view) {
        EditText text = findViewById(R.id.editTextTitle);
        String title = text.getText().toString();

        text = findViewById(R.id.editTextNParticipants);
        int nParticipants = Integer.parseInt(text.getText().toString());

        text = findViewById(R.id.editTextLocation);
        String address = text.getText().toString();

        text = findViewById(R.id.editTextDescription);
        String description = text.getText().toString();

        Sport sport = Sport.valueOf(spinner.getSelectedItem().toString());

        Date date = calendar.getTime();

        double duration = hours + minutes/60;

        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Activity toUpload = new Activity(
                "activity",
                organizerId,
                title,
                nParticipants,
                new ArrayList<String>(),
                longitude,
                latitude,
                description,
                date,
                duration,
                sport,
                address
        );

        BackendActivityManager bam = new BackendActivityManager(FirebaseFirestore.getInstance(),
                BackendActivityManager.ACTIVITIES_COLLECTION);

        bam.uploadActivity(toUpload, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Activity successfully uploaded!",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload activity",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
