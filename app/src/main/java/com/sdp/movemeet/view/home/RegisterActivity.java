package com.sdp.movemeet.view.home;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.main.MainActivity;

import java.util.ArrayList;

// First activity that the users is going to see when launching the app
public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private EditText fullNameEditText, emailEditText, passwordEditText, phoneEditText;
    private Button registerBtn;
    private TextView loginBtn;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    private FirebaseFirestore fStore;
    private BackendManager<User> userManager;
    private String userIDString, emailString, passwordString, fullNameString, phoneString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        if (fAuth.getCurrentUser() != null) { // if the user is already logged in (i.e. the current user object is present), we directly send him to the "MainActivity"
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        fullNameEditText = findViewById(R.id.edit_text_full_name);
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        phoneEditText = findViewById(R.id.edit_text_phone);
        registerBtn = findViewById(R.id.button_register);
        loginBtn = findViewById(R.id.text_view_login_here);

        fStore = BackendInstanceProvider.getFirestoreInstance();
        userManager = new FirestoreUserManager(FirestoreUserManager.USERS_COLLECTION, new UserSerializer());
        progressBar = findViewById(R.id.progressBar);

    }

    public void registerUser(View view) { // when the user clicks on the "REGISTER" button, we validate his data

        emailString = emailEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();
        fullNameString = fullNameEditText.getText().toString();
        phoneString = phoneEditText.getText().toString();

        if (TextUtils.isEmpty(emailString)) { // checking that the email address is not empty
            emailEditText.setError(getString(R.string.register_email_required_message));
            return;
        }

        if (TextUtils.isEmpty(passwordString)) { // checking that the password is not empty
            passwordEditText.setError(getString(R.string.register_password_required_message));
            return;
        }

        if (passwordString.length() < 6) { // checking that the password is at least 6 characters long
            passwordEditText.setError(getString(R.string.register_short_password_message));
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        User user = new User(fullNameString, emailString, phoneString, "", new ArrayList<>(), new ArrayList<>());

        registeringUserToFirebase(user);

    }

    public void registeringUserToFirebase(User user) { // Registering the user to the Firebase database
        fAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userIDString = fAuth.getCurrentUser().getUid();
                    String path = fStore.collection(FirestoreUserManager.USERS_COLLECTION).document(userIDString).getPath();
                    user.setDocumentPath(path);
                    userManager.add(user, null).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Log.d(TAG, "onSuccess: user profile is created for " + userIDString);
                        }
                    }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.toString()));
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

        });
    }

    public void loginOnClick(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

    }

}