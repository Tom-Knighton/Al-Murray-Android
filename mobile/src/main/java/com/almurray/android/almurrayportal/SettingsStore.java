package com.almurray.android.almurrayportal;

import android.app.Activity;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsStore extends AppCompatActivity {

    Button buy20AP;
    Button buy30P;
    Button buy50AP;
    Button buy50PP;
    Button buyAR;
    Button buyPR;
    Button buyNewPic;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {

                buy20AP = findViewById(R.id.buy20AP);
                buy20AP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder buy20A = new AlertDialog.Builder(getApplicationContext());
                        buy20A.setCancelable(true);
                        buy20A.setTitle("Information");
                        buy20A.setMessage("If you click confirm you agree to pay the money and that there can be no refunds.");
                        buy20A.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        buy20A.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        buy20A.show();
                    }
                });

                buy30P = findViewById(R.id.buy30PP);
                buy20AP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                }).show();
                    }
                });
                buy50AP = findViewById(R.id.buy50AP);
                buy50AP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                }).show();
                    }
                });
                buy50PP = findViewById(R.id.buy50PP);
                buy50PP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                }).show();
                    }
                });
                buyAR = findViewById(R.id.buyARank);
                buyAR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds. You also agree that if you are on a rank that can not be ranked up through money, you will save this opportunity until you reach such a rank.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                }).show();
                    }
                });
                buyPR = findViewById(R.id.buyPRank);
                buyPR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds. You also agree that if you are on a rank that can not be ranked up through money, you will save this opportunity until you reach such a rank.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



                                    }
                                }).show();
                    }
                });
                buyNewPic = findViewById(R.id.buyProfilePicture);
                buyNewPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds. You also agree that if your image is not deemed acceptable, you will have to choose a new one.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {



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
        setContentView(R.layout.activity_settings_store);
    }
}
