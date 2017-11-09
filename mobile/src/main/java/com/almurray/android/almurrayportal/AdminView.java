package com.almurray.android.almurrayportal;

import android.content.Intent;
import android.os.Handler;
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

public class AdminView extends AppCompatActivity {


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    private Button teamRButton;
    private Button teamFButton;
    private Button teamMButton;
    private Button teamEButton;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {
                teamRButton = findViewById(R.id.teamRButton);
                teamFButton = findViewById(R.id.teamFButton);
                teamMButton = findViewById(R.id.teamMButton);
                teamEButton = findViewById(R.id.teamEButton);
                teamRButton.setEnabled(true);
                teamFButton.setEnabled(false);
                teamMButton.setEnabled(false);
                teamEButton.setEnabled(false);


                teamRButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(AdminView.this, listUsers.class);
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
        setContentView(R.layout.activity_admin_view);


    }
}
