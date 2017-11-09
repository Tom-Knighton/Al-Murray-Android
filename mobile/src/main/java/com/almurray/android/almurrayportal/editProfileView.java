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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfileView extends AppCompatActivity {


    private CircleImageView profileImage;
    private TextView amigoPoints;
    private TextView pPoints;
    private Button editPPoints;
    private Button editAPoints;
    private TextView ARankLabel;
    private TextView PRankLabel;


    private String currentAPoints;
    private String currentPPoints;
    private String currentARank;
    private String currentPRank;
    private String linkToURLI;
    private String profile;



    private DatabaseReference ref;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {
                profileImage = findViewById(R.id.editProfileImage);
                amigoPoints = findViewById(R.id.editAmigoLabel);
                pPoints = findViewById(R.id.editPositiveLabel);
                editPPoints = findViewById(R.id.posEdit);
                editAPoints = findViewById(R.id.amigoEdit);
                ARankLabel = findViewById(R.id.editARankView);
                PRankLabel = findViewById(R.id.editPrankView);
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs .edit();
                profile = prefs.getString("currentEditUser", "");
                Log.d("TAG", profile);

                if(profile == "GeorgeD") {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child("kuYfK8Er9DT2YBkcpPamkp4eo0D3");
                    getStats();
                }
                if(profile == "JoeI") {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child("j830FVllozcqkonqsVXfEShG8HX2");
                    getStats();
                }
                if(profile == "NickW") {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child("MQVUGM9ig5SDOzIsaqKGSRHT3lJ3");
                    getStats();
                }
                if(profile == "SamC") {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child("2yZRTW0x7cQjQ5aN8isNf8bxOp92");
                    getStats();
                }
                if(profile == "SethL") {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child("uRpVX2ppdsQ4VKxPyDFGsx7q51i1");
                    getStats();
                }
                if(profile == "TaylorP") {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child("WbWHZsR23QhjoSRqMUe8TB4Gngh2");
                    getStats();
                }
                if(profile == "TomK") {
                    ref = FirebaseDatabase.getInstance().getReference().child("users").child("aSatYR3zJpW1wTXfrrCGwhN6Cuc2");
                    getStats();

                }


            }
        };

        handler.post(updater);


    }

    public void getStats() {
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
                amigoPoints.setText("Amigo Points: "+currentAPoints);
                pPoints.setText("Positivity Points: "+currentPPoints);

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
                        dataSnapshot.child("AmigoPoints").getRef().setValue(newAmigoPoints.getText().toString());
                        getStats();
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

    public void onEditPos(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText newPosPoints = new EditText(this);
        builder.setView(newPosPoints);
        builder.setTitle("Edit Positivity Points");
        builder.setMessage("Set new Positivity Points value");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.child("pPoints").getRef().setValue(newPosPoints.getText().toString());
                        getStats();
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
}
