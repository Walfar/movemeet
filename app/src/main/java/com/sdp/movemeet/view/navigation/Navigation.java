package com.sdp.movemeet.view.navigation;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.movemeet.view.activity.ActivityDescriptionActivity;
import com.sdp.movemeet.view.activity.ActivityListActivity;
import com.sdp.movemeet.view.main.MainActivity;
import com.sdp.movemeet.view.profile.ProfileActivity;
import com.sdp.movemeet.view.activity.UploadActivityActivity;
import com.sdp.movemeet.view.chat.ChatActivity;

public class Navigation extends AppCompatActivity {

    public static void startActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ActivityDescriptionActivity.class);
        context.startActivity(intent);
    }

    public static void goToActivityUpload(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, UploadActivityActivity.class);
        context.startActivity(intent);
    }

    public static void goToUserProfileActivity(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    public static void goToHome(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void goToChat(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("ACTIVITY_CHAT_ID", "general_chat_new_format"); // general_chat"
        context.startActivity(intent);
    }

    public static void goToListOfActivities(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ActivityListActivity.class);
        context.startActivity(intent);
    }

}
