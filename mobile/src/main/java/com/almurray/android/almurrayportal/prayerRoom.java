package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jetradarmobile.snowfall.SnowfallView;

public class prayerRoom extends AppCompatActivity {


    Button simplePray;
    Button complexPray;
    Button prayInfo;
    SnowfallView prayerSnow;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());



    @Override
    protected void onResume() {
        super.onResume();



        Handler handler = new Handler();
        Runnable updater = new Runnable() {
            @Override
            public void run() {
                prayerSnow = findViewById(R.id.prayerSnowFall);
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs .edit();
                if(prefs.getBoolean("snowState", true)) {
                    prayerSnow.setVisibility(View.GONE);
                } else {
                    prayerSnow.setVisibility(View.GONE);
                }


                prayInfo = (Button)findViewById(R.id.prayInfo);
                prayInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(prayerRoom.this)
                                .setTitle("PSA")
                                .setMessage("This is the prayer room, available for sinners and believers alike to pray to Al Murray. At the end of each day, the total times you have clicked each prayer button will be totalled up and looked at, and points will be assigned depending on how many times you have prayed. However, OVERPRAYING CAN AND WILL LEAD TO MINUS POINTS. ")
                                .setCancelable(true)
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                }).show();
                    }
                });


                simplePray = (Button)findViewById(R.id.simplePrayButton);
                simplePray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(prayerRoom.this)
                                .setTitle("PSA")
                                .setMessage("Remember, praying too much (more than 7 times) a day can lead to minus points")
                                .setCancelable(true)
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Integer oldPrayers = dataSnapshot.child("simplePrayers").getValue(Integer.class);
                                                Integer newPrayers = oldPrayers + 1;
                                                dataSnapshot.child("simplePrayers").getRef().setValue(newPrayers);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }).show();
                    }
                });


                complexPray = (Button)findViewById(R.id.complexPrayButton);
                complexPray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(prayerRoom.this)
                                .setTitle("PSA")
                                .setMessage("One of these can be submitted per 2 days. Simply a normal prayer but with a message.")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Integer oldCPrayers = dataSnapshot.child("complexPrayers").getValue(Integer.class);
                                                Integer newCPrayers = oldCPrayers + 1;
                                                dataSnapshot.child("complexPrayers").getRef().setValue(newCPrayers);
                                                startActivity(new Intent(prayerRoom.this, complexPrayer.class));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }).show();
                    }
                });
            }
        };
        handler.post(updater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_room);
    }
}
