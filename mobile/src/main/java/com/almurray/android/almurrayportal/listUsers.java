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

public class listUsers extends AppCompatActivity {


    private Button georgebutton;
    private Button joeButton;
    private Button nickButton;
    private Button samButton;
    private Button sethButton;
    private Button taylorButton;
    private Button tomButton;

    private String currentUID;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs .edit();
                currentUID = prefs.getString("currentUser", "");
                Log.d("TAG", currentUID);




                georgebutton = findViewById(R.id.georgeViewButton);
                joeButton = findViewById(R.id.joeViewButton);
                nickButton = findViewById(R.id.nickViewButton);
                samButton = findViewById(R.id.samViewButton);
                sethButton = findViewById(R.id.sethViewButton);
                taylorButton = findViewById(R.id.taylorViewButton);
                tomButton = findViewById(R.id.tomViewButton);

                if(currentUID.equals("2yZRTW0x7cQjQ5aN8isNf8bxOp92")) {
                    samButton.setEnabled(false);
                    tomButton.setEnabled(false);
                }
                if(currentUID.equals("MQVUGM9ig5SDOzIsaqKGSRHT3lJ3")) {
                    nickButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }
                if(currentUID.equals("WbWHZsR23QhjoSRqMUe8TB4Gngh2")) {
                    taylorButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }
                if(currentUID.equals("YLltc7T65CeMuOpN9P64qQixd502")) {
                    sethButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }

                if(currentUID.equals("j830FVllozcqkonqsVXfEShG8HX2")) {
                    joeButton.setEnabled(false);
                    tomButton.setEnabled(false);

                }
                if(currentUID.equals("kuYfK8Er9DT2YBkcpPamkp4eo0D3")) {
                    georgebutton.setEnabled(false);
                    tomButton.setEnabled(false);

                }





                georgebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentEditUser", "GeorgeD");
                        prefsEditor.commit();
                        Intent i = new Intent(listUsers.this, editProfileView.class);
                        startActivity(i);
                    }
                });
                joeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentEditUser", "JoeI");
                        prefsEditor.commit();
                        Intent i = new Intent(listUsers.this, editProfileView.class);
                        startActivity(i);
                    }
                });
                nickButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentEditUser", "NickW");
                        prefsEditor.commit();
                        Intent i = new Intent(listUsers.this, editProfileView.class);
                        startActivity(i);
                    }
                });
                samButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentEditUser", "SamC");
                        prefsEditor.commit();
                        Intent i = new Intent(listUsers.this, editProfileView.class);
                        startActivity(i);
                    }
                });
                sethButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentEditUser", "SethL");
                        prefsEditor.commit();
                        Intent i = new Intent(listUsers.this, editProfileView.class);
                        startActivity(i);
                    }
                });
                taylorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentEditUser", "TaylorP");
                        prefsEditor.commit();
                        Intent i = new Intent(listUsers.this, editProfileView.class);
                        startActivity(i);
                    }
                });
                tomButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentEditUser", "TomK");
                        prefsEditor.commit();
                        Intent i = new Intent(listUsers.this, editProfileView.class);
                        startActivity(i);
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

