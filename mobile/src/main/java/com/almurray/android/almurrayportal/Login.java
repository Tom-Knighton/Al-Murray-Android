package com.almurray.android.almurrayportal;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
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


public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button mButton;
    EditText mEmail;
    EditText mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mButton = (Button) findViewById(R.id.signinbutton);
        mEmail = (EditText) findViewById(R.id.emailText);
        mPass = (EditText) findViewById(R.id.passwordText);
    }

    public void onLoginClick(View v) {

        mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent i = new Intent(Login.this, Activity.class);
                    finish();
                    startActivity(i);

                } else {
                    new AlertDialog.Builder(Login.this)
                            .setTitle("Al Murray Says No")
                            .setMessage("Uh oh! Your email or password is not recognised, please try again!s")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            }).show();

                }


            }

        });
    }
}

