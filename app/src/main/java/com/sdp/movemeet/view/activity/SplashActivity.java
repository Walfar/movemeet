package com.sdp.movemeet.view.activity;

import android.content.Intent;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.sdp.movemeet.R;
import com.sdp.movemeet.view.home.HomeScreenActivity;
import com.sdp.movemeet.view.home.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread td = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };td.start();

    }
}