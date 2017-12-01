package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button mButton;
    EditText mEmail;
    EditText mPass;
    Button rButton;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {
                rButton = findViewById(R.id.requestAccountButton);
                rButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Login.this, requestAccount.class));
                    }
                });



            }
        };

        handler.post(updater);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mButton = (Button) findViewById(R.id.loginButton);
        mEmail = (EditText) findViewById(R.id.emailEntry);
        mPass = (EditText) findViewById(R.id.passwordEntry);
        startService(new Intent(Login.this, SoundService.class));
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(Login.this, MainNav.class);
            finish();
            startActivity(i);

        }
    }

    public void onLoginClick(View v) {

        if(mEmail.getText().toString().trim().length() == 0 || mPass.getText().toString().trim().length() == 0) {


        }

        else {
            Log.d("TAG", mEmail.getText().toString());
            Log.d("TAG", mPass.getText().toString());
            mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = prefs .edit();
                        prefsEditor.putString("currentUser", mAuth.getCurrentUser().getUid());
                        prefsEditor.commit();
                        Intent i = new Intent(Login.this, MainNav.class);
                        finish();
                        startActivity(i);

                    } else {
                        new AlertDialog.Builder(Login.this)
                                .setTitle("Al Murray Says No")
                                .setMessage("Uh oh! Your email or password is not recognised, please try again!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                }).show();

                    }


                }

            });
        }


    }
}

