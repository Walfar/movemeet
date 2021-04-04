package com.sdp.movemeet.Navigation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.sdp.movemeet.Activity.ActivityDescriptionActivity;
import com.sdp.movemeet.Backend.BackendActivityManagerDemo;
import com.sdp.movemeet.LoginActivity;
import com.sdp.movemeet.MainActivity;
import com.sdp.movemeet.ProfileActivity;
import com.sdp.movemeet.R;
import com.sdp.movemeet.UploadActivityActivity;
import com.sdp.movemeet.map.MapsActivity;

public class Navigation extends AppCompatActivity{

    public static void startActivity(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, ActivityDescriptionActivity.class);
        mContext.startActivity(intent);
    }

    public static void goToFirebaseDebug(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, BackendActivityManagerDemo.class);
        mContext.startActivity(intent);
    }

    public static void goToMaps(View view) {
        Context mContext = view.getContext();
        Intent intent = new Intent(mContext, MapsActivity.class);
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
}
