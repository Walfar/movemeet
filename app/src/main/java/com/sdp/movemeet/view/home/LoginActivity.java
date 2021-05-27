package com.sdp.movemeet.view.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.R;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.view.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginBtn;
    private TextView createBtn;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        progressBar = findViewById(R.id.activity_login_progress_bar);
        fAuth = AuthenticationInstanceProvider.getAuthenticationInstance();
        loginBtn = findViewById(R.id.button_login);
        createBtn = findViewById(R.id.text_view_create_account);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // When the user clicks on the "LOGIN" button, we first validate his data
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) { // checking that the email address is not empty
                    emailEditText.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) { // checking that the password is not empty
                    passwordEditText.setError("Password is required.");
                    return;
                }

                if (password.length() < 6) { // checking that the password is at least 6 characters long
                    passwordEditText.setError("Password must be >= 6 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Then we authenticate the user using his email and password
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);// we redirect the user to the "MainActivity"
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                });

            }

        });

    }

    public void openRegisterActivity(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class)); // redirecting the user to the "Register" activity
    }
}