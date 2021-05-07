package com.sdp.movemeet.view.main;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.sdp.movemeet.R;

public class MainUnregisterActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_unregister);

        createDrawer();
    }

    public void createDrawer() {
        //drawerLayout = findViewById(R.id.drawer_layout_unregister);
        textView = findViewById(R.id.textViewUnregister);
        //toolbar = findViewById(R.id.toolbarUnregister);
    }
}