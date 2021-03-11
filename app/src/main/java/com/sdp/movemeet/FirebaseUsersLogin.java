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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FirebaseUsersLogin extends AppCompatActivity {

    EditText mEmail, mPassword;
    TextView mCreateBtn, mDetailsGlanceBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    TextView fullName, emailTextView, phone;
    FirebaseFirestore fStore;
    String userId;

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


        // Defining the OnClickListener for the "text_view_create_account button"
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FirebaseUsersRegister.class)); // redirecting the user to the "Register" activity
            }
        });

        // Defining the OnClickListener for the "text_view_details_glance button"
        mDetailsGlanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the user clicks on the "text_view_details_glance button" button, we first validate his data
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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

                // Then we authenticate the user using his email and password
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast toast = Toast.makeText(FirebaseUsersLogin.this, "Account verified.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.START, 90, 0);
                            toast.show();

                            // Displaying user data
                            //----
                            phone = findViewById(R.id.text_view_profile_phone);
                            fullName = findViewById(R.id.text_view_profile_name);
                            emailTextView = findViewById(R.id.text_view_profile_email);

                            fAuth = FirebaseAuth.getInstance();
                            fStore = FirebaseFirestore.getInstance();

                            userId = fAuth.getCurrentUser().getUid();

                            // Retrieving the data from the Firestore database
                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            documentReference.addSnapshotListener(FirebaseUsersLogin.this, new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    phone.setText(documentSnapshot.getString("phone"));
                                    fullName.setText(documentSnapshot.getString("fullName"));
                                    emailTextView.setText(documentSnapshot.getString("email"));
                                }

                            });
                            //----

                        } else {
                            Toast.makeText(FirebaseUsersLogin.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                });

            }
        });

    }
}