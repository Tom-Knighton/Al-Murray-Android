package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class bannedActivity extends AppCompatActivity {

    TextView bannedT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();

        Runnable updater = new Runnable() {
            @Override
            public void run() {
                bannedT = findViewById(R.id.bannedR);
                SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                bannedT.setText("You have been banned for: "+prefs.getString("bannedR", "Breaking Policy"));
            }
        };

        handler.post(updater);

    }

}

