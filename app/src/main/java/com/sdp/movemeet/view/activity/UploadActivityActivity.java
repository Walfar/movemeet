package com.sdp.movemeet.view.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.serialization.ActivitySerializer;
import com.sdp.movemeet.models.Activity;
import com.sdp.movemeet.models.Sport;
import com.sdp.movemeet.view.home.LoginActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.navigation.Navigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class UploadActivityActivity extends AppCompatActivity {

    public static final String CREATED_ACTIVITY_FIELD = "createdActivity";

    private FirebaseAuth fAuth;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public BackendManager<Activity> activityBackendManager;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean enableNav = true;

    private Spinner spinner;
    private Calendar calendar;
    private EditText startTimeText;
    private EditText dateText;
    private int year, month, day;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText nParticipantsEditText;

    private EditText durationText;
    private int hours = 0;
    private int minutes = 0;

    private EditText addressText;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double latitude = 0;
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double longitude = 0;

    public boolean validLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_activity);

        // Setup Firebase services
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        if (enableNav) new Navigation(this, R.id.nav_add_activity).createDrawer();

        activityBackendManager = new FirestoreActivityManager(
                FirestoreActivityManager.ACTIVITIES_COLLECTION,
                new ActivitySerializer());

        //The aim is to block any direct access to this page if the user is not logged

        // Setup activity creation form inputs
        setupSportSpinner(this);

        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        nParticipantsEditText = findViewById(R.id.editTextNParticipants);

        setupDateInput(this);

        startTimeText = findViewById(R.id.editTextStartTime);

        durationText = findViewById(R.id.editTextTime);

        addressText = findViewById(R.id.editTextLocation);
        validLocation = false;

        //Try to get intent from map, in the case where the user creates an activity on click
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getParcelableExtra("bundle");
            if (bundle != null) {
                retrieveAddress(bundle.getParcelable("position"));
            }
        }

    }


    // ------------ FUNCTIONS FOR UI INPUT ------------------

    // Sport selection
    private void setupSportSpinner(Context context) {
        spinner = findViewById(R.id.spinnerSportType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, Sport.values());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    // Date selection
    private void showDate(int year, int month, int day) {
        dateText.setText(day + "." + month + "." + year);
    }

    private void setupDateInput(Context context) {
        dateText = findViewById(R.id.editTextDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
    }

    private final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month + 1);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            showDate(year, month + 1, dayOfMonth);
        }
    };

    /**
     * Displays the Date Picker so the user can choose a date
     *
     * @param view the View to display the Date Picker in
     */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }


    // Start + end times selection

    private final TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            startTimeText.setText(hourOfDay + ":" + ((minute < 10) ? "0" + minute : minute));
        }
    };

    /**
     * Displays a Time Picker so the user can select the start time
     *
     * @param view the View to display the Time Picker in
     */
    @SuppressWarnings("deprecation")
    public void setStartTime(View view) {
        showDialog(222);
    }


    // End time picker
    private final TimePickerDialog.OnTimeSetListener durationListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showDuration(hourOfDay, minute);
        }
    };

    private void showDuration(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        calendar.set(Calendar.SECOND, hours);
        calendar.set(Calendar.MILLISECOND, minutes);
        durationText.setText(hours + ":" + ((minutes < 10) ? "0" + minutes : minutes));
    }

    /**
     * Displays a Time Picker so the user can select the end time
     *
     * @param view the View to display the Time Picker in
     */
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
                    durationListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);

        } else if (id == 222) {
            return new TimePickerDialog(this,
                    startTimeListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
        }
        return null;
    }

    /**
     * Returns the address location if set by user, or null otherwise
     *
     * @return a LatLng containing the address's coordinates, or null if none could be inferred
     */
    public LatLng getAddressLocation() {
        if (addressText.getText().toString().equals("")) return null;
        return new LatLng(latitude, longitude);
    }

    // Helper methods for address geocoding
    private void tryLocatingAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            } else {
                throw new IOException();
            }
        } catch (IOException e) {

        }
    }


    /**
     * Attempts to resolve a LatLng into an Address. If successful, stores the result
     * in this class's address EditText
     *
     * @param pos the LatLng to convert to an address.
     */
    public void retrieveAddress(LatLng pos) {

        latitude = pos.latitude;
        longitude = pos.longitude;

        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addresses = geocoder.getFromLocation(pos.latitude, pos.longitude, 1);
            if (addresses.size() > 0) {
                addressText.setText(addresses.get(0).getAddressLine(0));
                validLocation = true;
            } else {
                throw new IOException();
            }
        } catch (IOException e) {

        }

    }


    /**
     * Retrieve all activity information from the forms on the screen
     * and parse them. If a valid Activity can be created, creates it, else null
     *
     * @return an Activity if the information is valid, else null
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public Activity validateActivity() {
        String organizerId = fAuth.getCurrentUser().getUid();

        Sport sport = Sport.valueOf(spinner.getSelectedItem().toString());

        String title = titleEditText.getText().toString();
        if (title.isEmpty()) title = sport.name();

        String nptext = nParticipantsEditText.getText().toString();
        if (nptext.isEmpty() || Integer.parseInt(nptext) <= 0) {
            Toast.makeText(this, "Please enter a positive number of participants", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        int nParticipants = Integer.parseInt(nptext);

        // Initializing the list of participants with the organizerId
        ArrayList<String> participantsId = new ArrayList<>();
        participantsId.add(organizerId);

        String address = addressText.getText().toString();
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter a valid location", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        if (!validLocation) tryLocatingAddress(this, address);

        String description = descriptionEditText.getText().toString();

        if (startTimeText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a start time", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        if (durationText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a duration", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        double duration = (double) (hours) + (double) (minutes) / 60;
        Date date = calendar.getTime();

        Activity activity = new Activity(
                organizerId + " || " + date, organizerId, title, nParticipants,
                participantsId, longitude, latitude, description, null, date, duration,
                sport, address, new Date()
        );

        return activity;
    }

    /**
     * Creates a new Activity from the information in the forms and uploads if it the Activity
     * is valid.
     *
     * @param view the View whose forms to retrieve information in
     */
    public void confirmActivityUpload(View view) {
        Activity toUpload = validateActivity();

        if (toUpload == null) return;

        ((Task<Void>) activityBackendManager.add(toUpload, null)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Activity successfully uploaded!",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("upload activity TAG", "failed");
                Toast.makeText(getApplicationContext(), "Failed to upload activity",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}