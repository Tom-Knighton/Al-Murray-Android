package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class listUsers extends AppCompatActivity {


    private Button georgebutton;
    private Button joeButton;
    private Button nickButton;
    private Button samButton;
    private Button sethButton;
    private Button taylorButton;
    private Button tomButton;
    private Button createButton;

    private String currentUID;

    private String georgeU;
    private String nickU;
    private String taylorU;
    private String sethU;
    private String samU;
    private String tomU;
    private String joeU;

    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("globalvariables").child("ids");

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {

                createButton = findViewById(R.id.createAccountButton);

                georgebutton = findViewById(R.id.georgeViewButton);
                joeButton = findViewById(R.id.joeViewButton);
                nickButton = findViewById(R.id.nickViewButton);
                samButton = findViewById(R.id.samViewButton);
                sethButton = findViewById(R.id.sethViewButton);
                taylorButton = findViewById(R.id.taylorViewButton);
                tomButton = findViewById(R.id.tomViewButton);

                if(email.equals("tomknighton@icloud.com")) {
                    createButton.setEnabled(true);
                }

                if(email.equals("cornwells2013@royalliberty.co.uk")) {
                    samButton.setEnabled(false);
                    tomButton.setEnabled(false);
                }
                if(email.equals("willisn2013@royalliberty.co.uk")) {
                    nickButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }
                if(email.equals("pught2013@royalliberty.co.uk")) {
                    taylorButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }
                if(email.equals("leigh-kaufmans2013@royalliberty.co.uk")) {
                    sethButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }

                if(email.equals("impett2013@royalliberty.co.uk")) {
                    joeButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }
                if(email.equals("dodkinsg2013@royalliberty.co.uk")) {
                    georgebutton.setEnabled(false);
                    tomButton.setEnabled(false);

                }


                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        georgeU = dataSnapshot.child("GeorgeD").getValue(String.class);
                        nickU = dataSnapshot.child("NickW").getValue(String.class);
                        taylorU = dataSnapshot.child("TaylorP").getValue(String.class);
                        sethU = dataSnapshot.child("Seth").getValue(String.class);
                        samU = dataSnapshot.child("SamC").getValue(String.class);
                        tomU = dataSnapshot.child("TomK").getValue(String.class);
                        joeU = dataSnapshot.child("JoeI").getValue(String.class);
                        String level;


                        georgebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                Intent i = new Intent(listUsers.this, editProfileView.class);
                                i.putExtra("currentEditUser", "GeorgeD");
                                i.putExtra("currentEditUID", georgeU);
                                if(getIntent().hasExtra("rankOC")) { i.putExtra("level", getIntent().getStringExtra("rankOC")); }
                                startActivity(i);
                            }
                        });
                        joeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(listUsers.this, editProfileView.class);
                                i.putExtra("currentEditUser", "JoeI");
                                i.putExtra("currentEditUID", joeU);
                                if(getIntent().hasExtra("rankOC")) { i.putExtra("level", getIntent().getStringExtra("rankOC")); }
                                startActivity(i);
                            }
                        });
                        nickButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(listUsers.this, editProfileView.class);
                                i.putExtra("currentEditUser", "NickW");
                                if(getIntent().hasExtra("rankOC")) { i.putExtra("level", getIntent().getStringExtra("rankOC")); }
                                i.putExtra("currentEditUID", nickU);
                                startActivity(i);
                            }
                        });
                        samButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(listUsers.this, editProfileView.class);
                                i.putExtra("currentEditUser", "SamC");
                                i.putExtra("currentEditUID", samU);
                                if(getIntent().hasExtra("rankOC")) { i.putExtra("level", getIntent().getStringExtra("rankOC")); }
                                startActivity(i);
                            }
                        });
                        sethButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(listUsers.this, editProfileView.class);
                                i.putExtra("currentEditUser", "Seth");
                                i.putExtra("currentEditUID", sethU);
                                if(getIntent().hasExtra("rankOC")) { i.putExtra("level", getIntent().getStringExtra("rankOC")); }
                                startActivity(i);
                            }
                        });
                        taylorButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(listUsers.this, editProfileView.class);
                                i.putExtra("currentEditUser", "TaylorP");
                                i.putExtra("currentEditUID", taylorU);
                                if(getIntent().hasExtra("rankOC")) { i.putExtra("level", getIntent().getStringExtra("rankOC")); }
                                startActivity(i);
                            }
                        });
                        tomButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(listUsers.this, editProfileView.class);
                                i.putExtra("currentEditUser", "TomK");
                                i.putExtra("currentEditUID", tomU);
                                if(getIntent().hasExtra("rankOC")) { i.putExtra("level", getIntent().getStringExtra("rankOC")); }
                                startActivity(i);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });











            }
        };

        handler.post(updater);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
    }
}

