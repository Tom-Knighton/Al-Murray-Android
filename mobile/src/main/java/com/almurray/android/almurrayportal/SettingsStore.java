package com.almurray.android.almurrayportal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsStore extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    Button buy20AP;
    Button buy30P;
    Button buy50AP;
    Button buy50PP;
    Button buyAR;
    Button buyPR;
    Button buyNewPic;


    SwitchCompat snowSwitch;
    SwitchCompat musicSwitch;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {
                final SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                final SharedPreferences.Editor prefsEditor = prefs .edit();

                musicSwitch = findViewById(R.id.musicSwitch);
                snowSwitch = findViewById(R.id.snowSwitch);
                snowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {

                            prefsEditor.putBoolean("snowState", true);
                            prefsEditor.commit();
                        }

                        else {

                            prefsEditor.putBoolean("snowState", false);
                            prefsEditor.commit();
                        }
                    }
                });

                if(prefs.getBoolean("snowState", true)) {
                    snowSwitch.setChecked(true);
                } else {
                    snowSwitch.setChecked(false);
                }

                musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {
                            prefsEditor.putBoolean("musicState", true);
                            prefsEditor.commit();
                        } else {
                            prefsEditor.putBoolean("musicState", false);
                            prefsEditor.commit();
                        }
                    }
                });

                if(prefs.getBoolean("musicState", true)) {
                    snowSwitch.setChecked(true);
                } else {
                    snowSwitch.setChecked(false);
                }

                buy20AP = findViewById(R.id.buy20AP);
                buy20AP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(SettingsStore.this)
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds")
                                .setCancelable(true)
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            bp.purchase(SettingsStore.this, "buy_20_amigo_points");

                                    }
                                })
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                }).show();
                    }
                });

                buy30P = findViewById(R.id.buy30PP);
                buy30P.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(SettingsStore.this)
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds")
                                .setCancelable(true)
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bp.purchase(SettingsStore.this, "buy_30_pp");

                                    }
                                })
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
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
                        new AlertDialog.Builder(SettingsStore.this)
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds")
                                .setCancelable(true)
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bp.purchase(SettingsStore.this, "buy_50_amigo_points");

                                    }
                                })
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
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
                        new AlertDialog.Builder(SettingsStore.this)
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds")
                                .setCancelable(true)
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bp.purchase(SettingsStore.this, "buy_50_pp");

                                    }
                                })
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
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
                        new AlertDialog.Builder(SettingsStore.this)
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds. You also agree that if the next rank can not be bought, this will be saved until you reach a suitable rank.")
                                .setCancelable(true)
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bp.purchase(SettingsStore.this, "buy_a_rank");

                                    }
                                })
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
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
                        new AlertDialog.Builder(SettingsStore.this)
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds. You also agree that if the next rank can not be bought, this will be saved until you reach a suitable rank.")
                                .setCancelable(true)
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bp.purchase(SettingsStore.this, "buy_p_rank");

                                    }
                                })
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
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
                        new AlertDialog.Builder(SettingsStore.this)
                                .setTitle("Information")
                                .setMessage("If you click confirm you agree to pay the money and that there can be no refunds. You also agree that if your image is not acceptable you will have to choose a different image.")
                                .setCancelable(true)
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bp.purchase(SettingsStore.this, "buy_pic_change");

                                    }
                                })
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
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

    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_store);

        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgi4aB8eseqmSEldobawzxGjoazpmxL+pg4OQldjOxZvuWgvz7VnNuA1vEfc+21bI8aVm2JaOoYJvrJWxu6HLJ6D1AITtSteEHuYZB9uiiQw90U7/9WK0ALVXrY4Emn2o41O1NqHOHFmmuyMzju5wufPVNvfw33YTht9RZFwkvk4zKq8e+fJNC5nJmLk2t0S9E7kKCh4RjjOZORosZBE9oRXlPAsnEAVjilduqbGzUB0RWajwsMMQ0U5R8GB9bsbAf02+zHa0/RIlgsiAx9+9ZbwSBwyLQyv1JU62uw1PDD1cJ4M7fOwyj/2Uuqb5kFfTkJle51z1MmGnWewbDJIIBQIDAQAB", this);
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(SettingsStore.this, "Purchased successfully", Toast.LENGTH_SHORT);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(SettingsStore.this, "Error. You were not charged.", Toast.LENGTH_SHORT);

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
