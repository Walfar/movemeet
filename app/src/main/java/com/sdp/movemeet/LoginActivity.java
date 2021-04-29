package com.sdp.movemeet;

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

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    TextView createBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edit_text_email);
        password = findViewById(R.id.edit_text_password);
        progressBar = findViewById(R.id.activity_login_progress_bar);
        fAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.button_login);
        createBtn = findViewById(R.id.text_view_create_account);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // When the user clicks on the "LOGIN" button, we first validate his data
                String email = LoginActivity.this.email.getText().toString().trim();
                String password = LoginActivity.this.password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) { // checking that the email address is not empty
                    LoginActivity.this.email.setError("Email is required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) { // checking that the password is not empty
                    LoginActivity.this.password.setError("Password is required.");
                    return;
                }

                if (password.length() < 6) { // checking that the password is at least 6 characters long
                    LoginActivity.this.password.setError("Password must be >= 6 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Then we authenticate the user using his email and password
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class)); // we redirect the user to the "MainActivity"

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