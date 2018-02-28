package com.almurray.android.almurrayportal.feedUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.almurray.android.almurrayportal.Login;
import com.almurray.android.almurrayportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class uploadToFeed extends AppCompatActivity {


    ImageView uploadImage;
    TextView uploadHeader,uploadTeam,description;
    Button setDesc, upload;
    Boolean hasDesc, hasImage;

    Integer newPost;

    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference secondRef;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasDesc = false;
        hasImage = false;
        setContentView(R.layout.activity_upload_to_feed);
        uploadImage = findViewById(R.id.uploadFeedImage);
        uploadTeam = findViewById(R.id.uploadFeedTeam);
        uploadHeader = findViewById(R.id.uploadFeedHeader);
        description = findViewById(R.id.uploadFeedDescription);
        setDesc = findViewById(R.id.uploadFeedDescriptionButton);
        upload = findViewById(R.id.uploadPostButton);

        upload.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upload.isEnabled()) {
                    final String uuid = UUID.randomUUID().toString();
                    reference = ref.getReference().child("feed/"+uuid+".jpg");
                    final UploadTask uploadTask = reference.putBytes(imageb);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Log.d("TAG", "HEYA LOOKIE HERE AT: "+String.valueOf(downloadUrl));
                            Picasso.with(uploadToFeed.this).load(downloadUrl).into(uploadImage);
                            secondRef = FirebaseDatabase.getInstance().getReference().child("globalvariables");
                            secondRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Integer lastPost = dataSnapshot.child("lastPost").getValue(Integer.class);
                                    newPost = lastPost - 1;
                                    dataSnapshot.child("lastPost").getRef().setValue(newPost);
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("feed").child(String.valueOf(newPost));
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            final SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                            dataSnapshot.child("Comments").getRef().setValue(1);
                                            dataSnapshot.child("Likes").getRef().setValue(0);
                                            dataSnapshot.child("desc").getRef().setValue(description.getText().toString());
                                            Log.d("TAG", String.valueOf(downloadUrl));
                                            dataSnapshot.child("postURL").getRef().setValue(String.valueOf(downloadUrl));
                                            dataSnapshot.child("posterURL").getRef().setValue(prefs.getString("urlToImage", ""));
                                            dataSnapshot.child("posterName").getRef().setValue(prefs.getString("currentFullName", ""));
                                            dataSnapshot.child("lastComment").getRef().setValue(1);
                                            dataSnapshot.child("postNum").getRef().setValue(newPost);
                                            dataSnapshot.child("lastLike").getRef().setValue(0);
                                            dataSnapshot.child("sendbird").getRef().setValue(prefs.getString("sendbirdIDC", ""));

                                            Log.d("TAG", String.valueOf(newPost));
                                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("feed").child(String.valueOf(newPost)).child("commentList");
                                            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                                                    String date = String.valueOf(df.format("dd/MM/yy", new java.util.Date()));
                                                    dataSnapshot.child("1").child("comment").getRef().setValue(description.getText().toString());
                                                    dataSnapshot.child("1").child("commenterurl").getRef().setValue(prefs.getString("urlToImage", ""));
                                                    dataSnapshot.child("1").child("date").getRef().setValue(date);
                                                    finish();

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    });
                } else {

                }
            }
        });
        ImagePicker.setMinQuality(600,600);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.pickImageGalleryOnly(uploadToFeed.this, 1313);
            }
        });

        setDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(uploadToFeed.this);
                final EditText newDesc = new EditText(uploadToFeed.this);
                builder.setView(newDesc);
                builder.setTitle("");
                builder.setMessage("Set new description.");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String cdesc = newDesc.getText().toString();
                        cdesc = cdesc.trim();

                        if(cdesc.equals("") || cdesc.equals(" ")) {
                            hasDesc = false;
                            updateButton();
                        } else {
                            description.setText(cdesc);
                            hasDesc = true;
                            updateButton();
                        }
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
        });
    }


    // Creating URI.


    private void updateButton() {
        if(hasImage && hasDesc) {
            upload.setEnabled(true);
        } else {
            upload.setEnabled(false);
        }
    }

    Bitmap bmp;
    ByteArrayOutputStream baos;
    byte[] imageb;
    FirebaseStorage ref;
    StorageReference reference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode) {
            case 1313:
                if(data != null) {
                    storageReference = FirebaseStorage.getInstance().getReference();
                    Log.d("TAG", "WE FINISHED SELECTING A GODDAMN IMAGE");

                    try {
                        bmp = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
                        baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        imageb = baos.toByteArray();

                        ref = FirebaseStorage.getInstance();
                        String uuid = UUID.randomUUID().toString();
                        reference = ref.getReference().child("tempFeed/"+uuid+".jpg");
                        final UploadTask uploadTask = reference.putBytes(imageb);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.d("TAG", "HEYA LOOKIE HERE AT: "+String.valueOf(downloadUrl));
                                Picasso.with(uploadToFeed.this).load(downloadUrl).into(uploadImage);
                                hasImage = true;
                                updateButton();
                            }
                        });
                    } catch (Exception e) {
                        hasImage = false;
                        e.printStackTrace();
                        new android.support.v7.app.AlertDialog.Builder(uploadToFeed.this)
                                .setTitle("Al Murray Says No")
                                .setMessage("That Image failed to upload, sorry :(")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                }).show();
                    }
                }

        }


    }

}

