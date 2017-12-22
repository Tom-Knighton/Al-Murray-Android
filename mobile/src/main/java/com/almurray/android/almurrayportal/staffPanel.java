package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class staffPanel extends AppCompatActivity {


    Button mam;
    Button mhm;
    Button ep;
    Button ry;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_panel);

        mam = findViewById(R.id.staffMAM);
        mam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staffPanel.this, seeSop.class);
                startActivity(intent);
            }
        });

        mhm = findViewById(R.id.staffMHM);
        mhm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(staffPanel.this, MainNav.class);
                intent.putExtra("toChat", "deffo");
                startActivity(intent);
            }
        });

        ep = findViewById(R.id.staffEP);
        ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String level = preferences.getString("staffLevel", "staff");
                Intent intent = new Intent(staffPanel.this, AdminView.class);
                intent.putExtra("rankOC", level);
                startActivity(intent);
            }
        });

        ry = findViewById(R.id.staffRY);
        ry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer relieved = dataSnapshot.child("relieved").getValue(Integer.class);
                        dataSnapshot.child("relieved").getRef().setValue(relieved+1);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                new AlertDialog.Builder(staffPanel.this)

                        .setTitle("Information")
                        .setMessage("You have gone to the toilet")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        }).show();
            }
        });
    }
}
