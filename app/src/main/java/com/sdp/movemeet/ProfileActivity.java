package com.sdp.movemeet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdp.movemeet.Backend.FirebaseInteraction;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_FULL_NAME = "fullName";
    public static final String EXTRA_MESSAGE_EMAIL = "email";
    public static final String EXTRA_MESSAGE_PHONE = "phone";
    public static final String EXTRA_MESSAGE_DESCRIPTION = "description";

    ImageView profileImage;
    TextView fullName, email, phone, description;
    ProgressBar progressBar;

    String userId;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.image_view_profile_image);
        fullName = findViewById(R.id.text_view_activity_profile_name);
        email = findViewById(R.id.text_view_activity_profile_email);
        phone = findViewById(R.id.text_view_activity_profile_phone);
        description = findViewById(R.id.text_view_activity_profile_description);
        progressBar = findViewById(R.id.progress_bar_profile);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            displayRegisteredUserData();

            storageReference = FirebaseStorage.getInstance().getReference();
            loadRegisteredUserProfilePicture();
        }

    }


    public void displayRegisteredUserData() {
        fStore = FirebaseFirestore.getInstance();
        if (fAuth.getCurrentUser() != null) {
            TextView[] textViewArray = {fullName, email, phone, description};
            textViewArray = FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, ProfileActivity.this);
            fullName = textViewArray[0];
            email = textViewArray[1];
            phone = textViewArray[2];
            description = textViewArray[3];
        }

    }


    private void loadRegisteredUserProfilePicture() {
        StorageReference profileRef = storageReference.child("users/" + userId + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
    }


    public void goToEditProfileActivity(View view) {
        Intent i = new Intent(view.getContext(), EditProfileActivity.class);
        i.putExtra(EXTRA_MESSAGE_FULL_NAME, fullName.getText().toString()); // key, value
        i.putExtra(EXTRA_MESSAGE_EMAIL, email.getText().toString());
        i.putExtra(EXTRA_MESSAGE_PHONE, phone.getText().toString());
        i.putExtra(EXTRA_MESSAGE_DESCRIPTION, description.getText().toString());
        startActivity(i);
    }

}