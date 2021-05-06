package com.sdp.movemeet.view.navigation;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sdp.movemeet.backend.BackendManager;
import com.sdp.movemeet.backend.FirebaseInteraction;
import com.sdp.movemeet.backend.firebase.firestore.FirestoreUserManager;
import com.sdp.movemeet.backend.providers.AuthenticationInstanceProvider;
import com.sdp.movemeet.backend.providers.BackendInstanceProvider;
import com.sdp.movemeet.backend.serialization.UserSerializer;
import com.sdp.movemeet.models.User;
import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.activity.ActivityListActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.profile.ProfileActivity;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.chat.ChatActivity;

import org.jetbrains.annotations.NotNull;

public class Navigation extends AppCompatActivity {

    /**
     * Starts the default ActivityDescription activity
     * @param view the View in which to start the activity
     */
    public static void startActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ActivityDescriptionActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the ActivityUpload activity
     * @param view the View in which to start the activity
     */
    public static void goToActivityUpload(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UploadActivityActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the User Profile activity
     * @param view the View in which to start the activity
     */
    public static void goToUserProfileActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the MainActivity activity
     * @param view the View in which to start the activity
     */
    public static void goToHome(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * Starts the chat activity
     * @param view the View in which to start the activity
     */
    public static void goToChat(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("ACTIVITY_CHAT_ID", "general_chat");
        context.startActivity(intent);
    }

    /**
     * Starts ActivityList activity
     * @param view the View in which to start the activity
     */
    public static void goToListOfActivities(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ActivityListActivity.class);
        context.startActivity(intent);
    }


    /**
     * Fills in the navigation bar's text fields with the user's information
     * 0 = fullName
     * 1 = email
     * 2 = phone number
     * 3 = description
     * @param fields
     * @return
     */
    public static TextView[] fillNavigationProfileFields(TextView[] fields) {
        FirebaseUser firebaseUser = AuthenticationInstanceProvider.getAuthenticationInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            UserSerializer serializer = new UserSerializer();
            FirestoreUserManager backendUserManager =
                    new FirestoreUserManager(BackendInstanceProvider.getFirestoreInstance(),
                            FirestoreUserManager.USERS_COLLECTION,
                            serializer);

            backendUserManager.getUserFromUid(userId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = serializer.deserialize(task.getResult().getData());

                        fields[0].setText(user.getFullName());
                        fields[1].setText(user.getEmail());
                        fields[2].setText(user.getPhoneNumber());
                        if (fields.length > 3 && user.getDescription() != null && !user.getDescription().isEmpty())
                            fields[3].setText(user.getDescription());
                    }
                }
            });
        }

        return fields;
    }

    /*public void handleRegisterUser() {
        // Retrieve user data (full name, email and phone number) from Firebase Firestore
        if (fAuth.getCurrentUser() != null) {
            userId = fAuth.getCurrentUser().getUid();
            TextView[] textViewArray = {fullName, email, phone};
            //FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, UploadActivityActivity.this);
            FirebaseInteraction.retrieveDataFromFirebase(fStore, userId, textViewArray, UploadActivityActivity.this);
        }
    }*/

}
