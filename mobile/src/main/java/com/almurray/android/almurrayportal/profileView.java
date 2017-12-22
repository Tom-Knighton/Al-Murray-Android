package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

public class profileView extends AppCompatActivity {

    private String url;
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);


        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        url = prefs.getString("urlToImage", "");


        photoView = findViewById(R.id.zoomable);
        Picasso.with(getApplicationContext()).load(url).into(photoView);
    }
}
