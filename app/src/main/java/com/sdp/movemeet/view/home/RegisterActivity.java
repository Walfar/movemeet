package com.sdp.movemeet.view.home;

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
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreActivityManager;
import com.sdp.movemeet.view.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

// First activity that the users is going to see when launching the app
public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText fullNameEditText, emailEditText, passwordEditText, phoneEditText;
    Button registerBtn;
    TextView loginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    FirestoreActivityManager FirestoreManager;
    String userIDString, emailString, passwordString, fullNameString, phoneString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.edit_text_full_name);
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        phoneEditText = findViewById(R.id.edit_text_phone);
        registerBtn = findViewById(R.id.button_register);
        loginBtn = findViewById(R.id.text_view_login_here);

        fAuth = FirebaseAuth.getInstance(); // getting the current instance of the database (to perform actions on the database)
        //fStore = FirebaseFirestore.getInstance(); // instantiating the Firebase Firestore variable
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) { // if the user is already logged in (i.e. the current user object is present), we directly send him to the "MainActivity"
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }

    public void registerUser(View view) { // when the user clicks on the "REGISTER" button, we validate his data

        emailString = emailEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();
        fullNameString = fullNameEditText.getText().toString();
        phoneString = phoneEditText.getText().toString();

        if (TextUtils.isEmpty(emailString)) { // checking that the email address is not empty
            emailEditText.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(passwordString)) { // checking that the password is not empty
            passwordEditText.setError("Password is required.");
            return;
        }

        if (passwordString.length() < 6) { // checking that the password is at least 6 characters long
            passwordEditText.setError("Password must be >= 6 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        registeringUserToFirebase();

    }

    public void registeringUserToFirebase() { // Registering the user to the Firebase database
        fAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // if the account has been correctly been created, we store his data and launch the "MainActivity"
                    userIDString = fAuth.getCurrentUser().getUid(); // retrieving the user id of the currently logged in (registered) user
                    //DocumentReference documentReference = fStore.collection("users").document(userIDString); // if we first don't have this "users" collection in our database, it will automatically create it in Cloud Firestore
                    DocumentReference documentReference = FirestoreManager.get("users").getResult().getDocumentReference(userIDString);
                    Map<String, Object> user = registeringData();
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user profile is created for " + userIDString);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    // An error could for instance happen in case a user tries to register with an
                    // email already registered in the database ("Error! The email address is
                    // already in use by another account.")
                    Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public Map<String, Object> registeringData() {
        // Map with "String" as keys (that will act as attributes in our document) and "objects" as the data
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullNameString);
        user.put("email", emailString);
        user.put("phone", phoneString);

        return user;
    }

    public void loginOnClick(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }

}