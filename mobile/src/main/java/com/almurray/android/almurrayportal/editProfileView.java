package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

public class editProfileView extends AppCompatActivity {


    private CircleImageView profileImage;
    private TextView amigoPoints;
    private TextView pPoints;
    private TextView ARankLabel;
    private TextView PRankLabel;
    private TextView currentStanding;
    private TextView currentTeam;
    private TextView currentName;
    private TextView currentSName;


    private Button editPButton;

    private Button dm;


    private String currentU;
    private String currentN;


    private String currentAPoints;
    private String currentPPoints;
    private String currentARank;
    private String currentPRank;
    private String userSendbird;

    private String linkToURLI;




    private DatabaseReference ref;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {

                ref = FirebaseDatabase.getInstance().getReference().child("globalvariables").child("ids");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Intent intent = getIntent();
                        if(intent.hasExtra("currentSendbird")) {
                            String send = intent.getStringExtra("currentSendbird");
                            currentU = dataSnapshot.child(send).getValue(String.class);
                        } else {
                            currentU = intent.getStringExtra("currentEditUID");
                            currentN = intent.getStringExtra("currentEditUser");

                        }


                        ARankLabel = findViewById(R.id.editARankL);
                        PRankLabel = findViewById(R.id.editPRankL);
                        amigoPoints = findViewById(R.id.editAPointsL);
                        pPoints = findViewById(R.id.editPPointsL);
                        profileImage = findViewById(R.id.editProfileImage);
                        currentTeam = findViewById(R.id.editTeamL);
                        currentStanding = findViewById(R.id.editStandingL);
                        currentName = findViewById(R.id.editNameLabel);
                        currentSName = findViewById(R.id.editSpanishName);
                        editPButton = findViewById(R.id.editProfileButton);
                        Log.d("TAG", "BEFORE INTENT");
                        if(intent.hasExtra("level")) {
                            Log.d("TAG", "HAS INTENT");
                            if(intent.getStringExtra("level").equals("staff") || intent.getStringExtra("level").equals("admin")) {
                                Log.d("TAG", "EY WE NEED VSIBILITY");
                                editPButton.setVisibility(View.VISIBLE);

                                editPButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.d("TAG", "HEYA CURRENT UID IS: "+currentU);
                                        Intent i = new Intent(editProfileView.this, editUserStats.class);
                                        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("level", intent.getStringExtra("level"));
                                        editor.putString("currentlyEditing", currentU);
                                        editor.commit();
                                        i.putExtra("currentlyEditing", currentU);
                                        startActivity(i);
                                    }
                                });
                            }
                        }

                        getStats(currentU);

                        final ArrayList<String> ids = new ArrayList<>(2);

                        dm = findViewById(R.id.profileDM);
                        dm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                String sID = preferences.getString("sendbirdIDC", "");
                                ids.add(sID);
                                ids.add(userSendbird);
                                GroupChannel.createChannelWithUserIds(ids, true, new GroupChannel.GroupChannelCreateHandler() {
                                    @Override
                                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                                        if (e != null) {
                                            // Error.
                                            return;
                                        } else {
                                            Intent intent1 = new Intent(editProfileView.this, MainNav.class);
                                            intent1.putExtra("toChat", "deffo");
                                            intent1.putExtra("openChatName", userSendbird);
                                            startActivity(intent1);
                                        }
                                    }
                                });
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

    public void getStats(String id) {
        Log.d("TAG", currentU);
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(currentU);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentARank = dataSnapshot.child("amigoRank").getValue(String.class);
                currentPRank = dataSnapshot.child("rank").getValue(String.class);
                currentAPoints = dataSnapshot.child("AmigoPoints").getValue(String.class);
                currentPPoints = dataSnapshot.child("pPoints").getValue(String.class);
                linkToURLI = dataSnapshot.child("urToImage").getValue(String.class);

                ARankLabel.setText("Amigo Rank: "+currentARank);
                PRankLabel.setText("Positivity Rank: "+currentPRank);
                currentStanding.setText("Current Standing: "+dataSnapshot.child("standing").getValue(String.class));
                currentSName.setText(dataSnapshot.child("sName").getValue(String.class));
                currentName.setText(dataSnapshot.child("fullName").getValue(String.class));
                currentTeam.setText(dataSnapshot.child("team").getValue(String.class));
                amigoPoints.setText(currentAPoints);
                pPoints.setText(currentPPoints);

                userSendbird = dataSnapshot.child("sendbird").getValue(String.class);

                Context context = getApplicationContext();
                Picasso.with(context).load(linkToURLI).into(profileImage);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_view);


    }


//    public void onEditAmigo(View v) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final EditText newAmigoPoints = new EditText(this);
//        builder.setView(newAmigoPoints);
//        builder.setTitle("Edit Amigo Points");
//        builder.setMessage("Set new Amigo Points value");
//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        dataSnapshot.child("AmigoPoints").getRef().setValue(newAmigoPoints.getText().toString());
//                        getStats(id);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        builder.show();
//    }
//
//    public void onEditPos(View v) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        final EditText newPosPoints = new EditText(this);
//        builder.setView(newPosPoints);
//        builder.setTitle("Edit Positivity Points");
//        builder.setMessage("Set new Positivity Points value");
//        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        dataSnapshot.child("pPoints").getRef().setValue(newPosPoints.getText().toString());
//                        getStats(id);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        builder.show();
//    }
}
