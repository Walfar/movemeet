package com.sdp.movemeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// First activity that the users is going to see when launching the app
public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID, email, password, fullName, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_users_register);

        mFullName = findViewById(R.id.edit_text_full_name);
        mEmail = findViewById(R.id.edit_text_email);
        mPassword = findViewById(R.id.edit_text_password);
        mPhone = findViewById(R.id.edit_text_phone);
        mRegisterBtn = findViewById(R.id.button_register);
        mLoginBtn = findViewById(R.id.text_view_login_here);

        fAuth = FirebaseAuth.getInstance(); // getting the current instance of the database (to perform actions on the database)
        fStore = FirebaseFirestore.getInstance(); // instantiating the Firebase Firestore variable
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) { // if the user is already logged in (i.e. the current user object is present), we directly send him to the "MainActivity"
            startActivity(new Intent(getApplicationContext(), FirebaseUsersMainActivity.class));
            finish();
        }

    }

    public void registerUser(View view) { // when the user clicks on the "REGISTER" button, we validate his data

        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        fullName = mFullName.getText().toString();
        phone = mPhone.getText().toString();

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

        registeringUserToFirebase();

    }

    public void registeringUserToFirebase() { // Registering the user to the Firebase database
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // if the account has been correctly been created, we store his data and launch the "MainActivity"
                    userID = fAuth.getCurrentUser().getUid(); // retrieving the user id of the currently logged in (registered) user
                    DocumentReference documentReference = fStore.collection("users").document(userID); // if we first don't have this "users" collection in our database, it will automatically create it in Cloud Firestore
                    Map<String, Object> user = registeringData();
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user profile is created for " + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), FirebaseUsersMainActivity.class));

                } else {
                    Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show(); // an error could for instance happen in case a user tries to register with an email already registered in the database ("Error! The email address is already in use by another account.")
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public Map<String, Object> registeringData() {
        Map<String, Object> user = new HashMap<>(); // map with "String" as keys (that will act as attributes in our document) and "objects" as the data
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("phone", phone);

        return user;
    }

    public void loginOnClick(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // redirecting the user to the "Login" activity

    }

}