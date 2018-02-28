package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class whatsnew extends AppCompatActivity {

    Button cont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsnew);

        cont = findViewById(R.id.whatsNewButton);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("has2.8", "true");
                editor.apply();

                startActivity(new Intent(whatsnew.this, MainNav.class));
            }
        });
    }
}
