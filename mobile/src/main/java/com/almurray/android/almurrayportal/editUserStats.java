package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.wearable.DataApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class editUserStats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_stats);




    }

    private CircleImageView profilepic;
    private Button changePic;

    private TextView aPoints;
    private Button changeAPoints;

    private TextView pPoints;
    private Button changePPoints;

    private TextView aRank;
    private TextView pRank;
    private TextView team;

    private Button ban;
    private Button unban;
    private Button chatBan;
    private Button chatUnban;
    private Button promote;
    private Button demote;


    //SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
    //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(prefs.getString("currentlyEditing", ""));
    DatabaseReference ref;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {

                Log.d("TAG", "HEY WE GOTTA A OVER HEAR: "+getIntent().getStringExtra("currentlyEditing"));

                profilepic = findViewById(R.id.editStatsViewPic);
                changePic = findViewById(R.id.editStatsEditPic);
                aPoints = findViewById(R.id.editStatsViewAP);
                changeAPoints = findViewById(R.id.editStatsEditAP);
                pPoints = findViewById(R.id.editStatsViewPP);
                changePPoints = findViewById(R.id.editStatsEditPP);
                aRank = findViewById(R.id.editStatsARank);
                pRank = findViewById(R.id.editStatsPrank);
                team = findViewById(R.id.editStatsTeam);
                ban = findViewById(R.id.editStatsBan);
                unban = findViewById(R.id.editStatsUnban);
                chatBan = findViewById(R.id.editStatsChatBan);
                chatUnban = findViewById(R.id.editStatsChatUnban);
                promote = findViewById(R.id.editStatsPromotion);
                demote = findViewById(R.id.editStatsDemotion);

                ref = FirebaseDatabase.getInstance().getReference().child("users").child(getIntent().getStringExtra("currentlyEditing"));


                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Picasso.with(getApplicationContext()).load(dataSnapshot.child("urToImage").getValue(String.class)).into(profilepic);

                        aPoints.setText(dataSnapshot.child("AmigoPoints").getValue(String.class));
                        pPoints.setText(dataSnapshot.child("pPoints").getValue(String.class));

                        aRank.setText(dataSnapshot.child("amigoRank").getValue(String.class));
                        pRank.setText(dataSnapshot.child("rank").getValue(String.class));
                        team.setText(dataSnapshot.child("team").getValue(String.class));

                        changePic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(editUserStats.this)
                                        .setTitle("Al Murray Says No")
                                        .setMessage("Sorry, you have to be an Admin or higher to edit profile pictures. You are: -Staff-")
                                        .setCancelable(true)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                            }
                                        }).show();
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

    public void onEditAmigo(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText newAmigoPoints = new EditText(this);
        builder.setView(newAmigoPoints);
        builder.setTitle("Edit Amigo Points");
        builder.setMessage("Set new Amigo Points value");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        dataSnapshot.child("AmigoPoints").getRef().setValue(newAmigoPoints.getText().toString());
                        getStats("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void onEditPositive(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText newAmigoPoints = new EditText(this);
        builder.setView(newAmigoPoints);
        builder.setTitle("Edit Positive Points");
        builder.setMessage("Set new Positive Points value");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.child("pPoints").getRef().setValue(newAmigoPoints.getText().toString());
                        getStats("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void onBan(View v) {
        new AlertDialog.Builder(editUserStats.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to ban?")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.child("banned").getRef().setValue(true);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

        .show();
    }

    public void onUnBan(View v) {
        new AlertDialog.Builder(editUserStats.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to unban?")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("banned").getRef().setValue(false);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }

    public void onChatBan(View v) {
        new AlertDialog.Builder(editUserStats.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to ban from chat?")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("chatBan").getRef().setValue(true);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }

    public void onChatUnban(View v) {
        new AlertDialog.Builder(editUserStats.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to uban from chat?")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("chatBan").getRef().setValue(false);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }

    public void onPromotion(View v) {
        new AlertDialog.Builder(editUserStats.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to recommend for promotion?")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("promotion").getRef().setValue(true);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }

    public void onDemotion(View v) {
        new AlertDialog.Builder(editUserStats.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to recommend for demotion?")
                .setCancelable(true)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dataSnapshot.child("demotion").getRef().setValue(true);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }

    private void getStats(String id) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("urToImage").getValue(String.class)).into(profilepic);

                aPoints.setText(dataSnapshot.child("AmigoPoints").getValue(String.class));
                pPoints.setText(dataSnapshot.child("pPoints").getValue(String.class));

                aRank.setText(dataSnapshot.child("amigoRank").getValue(String.class));
                pRank.setText(dataSnapshot.child("rank").getValue(String.class));
                team.setText(dataSnapshot.child("team").getValue(String.class));




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
