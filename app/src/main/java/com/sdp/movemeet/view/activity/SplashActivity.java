package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.home.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private LottieAnimationView foot;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*foot = findViewById(R.id.foot);
        foot.setSpeed(1);
        foot.playAnimation();*/

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //startActivity(new Intent(this, HomeScreenActivity.class));
    }
}