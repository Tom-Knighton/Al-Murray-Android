package com.almurray.android.almurrayportal.feedUtils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.almurray.android.almurrayportal.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

public class seePhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_photo);
        photoView = findViewById(R.id.zommablePhoto);

        Bundle extras = getIntent().getExtras();
        try {
            if(extras.containsKey("photoToSee")) {
                Picasso.with(seePhoto.this).load(extras.getString("photoToSee")).into(photoView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PhotoView photoView;


}
