package com.sdp.movemeet.Navigation;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.ProfileActivity;
import com.sdp.movemeet.UploadActivityActivity;
import com.sdp.movemeet.chat.ChatActivity;

public class Navigation extends AppCompatActivity{

    public static void startActivity(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, ActivityDescriptionActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToActivityUpload(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, UploadActivityActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToUserProfileActivity(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, ProfileActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToHome(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToChat(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("ACTIVITY_CHAT_ID", "general_chat");
        mContext.startActivity(intent);
    }
}
