package com.sdp.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUsersLogin extends AppCompatActivity {

    EditText mEmail, mPassword;
    TextView mCreateBtn, mDetailsGlanceBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    TextView fullName, emailTextView, phone;
    FirebaseFirestore fStore;
    String userId, email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_users_login);

        mEmail = findViewById(R.id.edit_text_email_register);
        mPassword = findViewById(R.id.edit_text_password_register);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mCreateBtn = findViewById(R.id.text_view_create_account);
        mDetailsGlanceBtn = findViewById(R.id.text_view_details_glance);

    }


    public void createAccountOnClick(View view) {
        startActivity(new Intent(getApplicationContext(), FirebaseUsersRegister.class)); // redirecting the user to the "Register" activity
    }


    public void detailsGlanceOnClick(View view) {
        // When the user clicks on the "text_view_details_glance button" button, we first validate his data
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) { // checking that the email address is not empty
            mEmail.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) { // checking that the password is not empty
            mPassword.setError("Password is required.");
            return;
        }

        if (password.length() < 6) { // checking that the password is at least 6 characters long
            mPassword.setError("Password must be >= 6 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        authenticateUser();

    }

    public void authenticateUser() { // authenticating the user using his email and password
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast toast = Toast.makeText(FirebaseUsersLogin.this, "Account verified.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.START, 90, 0);
                    toast.show();

                    displayUserData();

                } else {
                    Toast.makeText(FirebaseUsersLogin.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

        });
    }

    private void displayUserData() { // displaying user data
        phone = findViewById(R.id.text_view_profile_phone);
        fullName = findViewById(R.id.text_view_profile_name);
        emailTextView = findViewById(R.id.text_view_profile_email);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, phone, fullName, emailTextView, FirebaseUsersLogin.this);

        fAuth = FirebaseAuth.getInstance();
        mCreateBtn = findViewById(R.id.text_view_create_account);


        // Defining the OnClickListener for the "text view button"
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FirebaseUsersRegister.class)); // redirecting the user to the "Register" activity
            }
        });
    }
}