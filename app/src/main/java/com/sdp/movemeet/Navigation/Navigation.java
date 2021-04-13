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

import org.jetbrains.annotations.NotNull;

public class Navigation extends AppCompatActivity{

    public static void startActivity(@NotNull View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, ActivityDescriptionActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToActivityUpload(@NotNull View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, UploadActivityActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToUserProfileActivity(@NotNull View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, ProfileActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToHome(@NotNull View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToChat(@NotNull View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, ChatActivity.class);
        mContext.startActivity(intent);
    }

}
