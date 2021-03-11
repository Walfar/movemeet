package com.sdp.movemeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD:app/src/main/java/com/sdp/movemeet/FirebaseUsersMainActivity.java
import com.google.firebase.auth.FirebaseAuth;
=======
import com.sdp.movemeet.Backend.BackendActivityManagerDemo;
import com.sdp.movemeet.bootcamp.R;
>>>>>>> b3de06a76138e6a4334de9a565c91a5ba1152379:app/src/main/java/com/sdp/movemeet/MainActivity.java

public class FirebaseUsersMainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.sdp.movemeet.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Called when the user taps the Go button */
    @SuppressWarnings("unused")
    public void sendMessage(View view) {
        Intent intent = new Intent(this, FirebaseUsersGreetingActivity.class);
        EditText editText = findViewById(R.id.mainEditName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

<<<<<<< HEAD:app/src/main/java/com/sdp/movemeet/FirebaseUsersMainActivity.java
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut(); // this will do the logout of the user from Firebase
        startActivity(new Intent(getApplicationContext(), FirebaseUsersLogin.class)); // sending the user to the "Login" activity
        finish();
    }

=======
    public void goToFirebaseDebug(View view) {
        Intent intent = new Intent(this, BackendActivityManagerDemo.class);
        startActivity(intent);
    }
>>>>>>> b3de06a76138e6a4334de9a565c91a5ba1152379:app/src/main/java/com/sdp/movemeet/MainActivity.java
}