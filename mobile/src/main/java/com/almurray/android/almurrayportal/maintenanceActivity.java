package com.almurray.android.almurrayportal;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class maintenanceActivity extends AppCompatActivity {

    TextView info;
    DatabaseReference globalRef = FirebaseDatabase.getInstance().getReference().child("globalvariables");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();





        Runnable updater = new Runnable() {

            public void run() {

                info = findViewById(R.id.maintenanceinfo);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        info.setText(dataSnapshot.child("maintenanceText").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                globalRef.addListenerForSingleValueEvent(eventListener);


            }
        };

        handler.post(updater);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(maintenanceActivity.this, MainNav.class);
        intent.putExtra("toChat", true);
        intent.putExtra("groupChannelUrl", "sendbird_group_channel_52153338_beed64ef0e9bc8b2ae656620663fb64b5e9b42a7");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
