package com.almurray.android.almurrayportal.feedUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.almurray.android.almurrayportal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class feedCommentsView extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;

    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    private Query mQuery;
    private commentsAdapter mMyAdapter;
    private ArrayList<comment> mAdapterItems;
    private ArrayList<String> mAdapterKeys;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_comments_view);

        recyclerView = findViewById(R.id.commentsRecycler);
        fab = findViewById(R.id.commentsAdd);
        fab.setEnabled(false);
        setupFirebase();
        setupRecyclerview();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fab.isEnabled()) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(feedCommentsView.this);
                    final EditText newComment = new EditText(feedCommentsView.this);
                    builder.setView(newComment);
                    builder.setTitle("");
                    builder.setMessage("Enter comment");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String cdesc = newComment.getText().toString().trim();

                            if(cdesc.equals("") || cdesc.equals(" ")) {

                            } else {
                                Bundle b = getIntent().getExtras();
                                Log.d("TAG", String.valueOf(b.getInt("postNum")));
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("feed").child(String.valueOf(b.getInt("postNum")));
                                final DatabaseReference cref = FirebaseDatabase.getInstance().getReference().child("feed").child(String.valueOf(b.getInt("postNum"))).child("commentList");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Integer lastCom = dataSnapshot.child("lastComment").getValue(Integer.class);
                                        final int newCom = lastCom + 1;
                                        cref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                android.text.format.DateFormat df = new android.text.format.DateFormat();
                                                String date = String.valueOf(df.format("dd/MM/yy", new java.util.Date()));

                                                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                                dataSnapshot.child(String.valueOf(newCom)).child("comment").getRef().setValue(cdesc);
                                                dataSnapshot.child(String.valueOf(newCom)).child("commenterurl").getRef().setValue(prefs.getString("urlToImage", ""));
                                                dataSnapshot.child(String.valueOf(newCom)).child("date").getRef().setValue(date);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        dataSnapshot.child("lastComment").getRef().setValue(newCom);
                                        dataSnapshot.child("Comments").getRef().setValue(newCom);
                                        String sendbird = dataSnapshot.child("sendbird").getValue(String.class);
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        try {
                                            String jsonResponse;

                                            URL url = new URL("https://onesignal.com/api/v1/notifications");
                                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                            con.setUseCaches(false);
                                            con.setDoOutput(true);
                                            con.setDoInput(true);

                                            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                            con.setRequestProperty("Authorization", "Basic NGI4NjYzMGYtYjFhMi00ZDQ1LWIzNDYtNWI2NTRhNjRhNTY3");
                                            con.setRequestMethod("POST");


                                            SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                            String strJsonBody = "{"
                                                    + "\"app_id\": \"2144f1b0-6c2c-44e2-ab7d-80fbae543240\","
                                                    + "\"ios_badgeType\": \"Increase\","
                                                    + "\"ios_badgeCount\": \"1\","
                                                    + "\"mutable_content\": \"true\","
                                                    + "\"content_available\": \"true\","
                                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"sendbird\", \"relation\": \"=\", \"value\": \""+sendbird+"\"}],"
                                                    + "\"data\": {\"foo\": \"bar\"},"
                                                    + "\"contents\": {\"en\": \""+prefs.getString("currentFullName", "")+" commented on your post!\"}"
                                                    + "}";


                                            System.out.println("strJsonBody:\n" + strJsonBody);

                                            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                            con.setFixedLengthStreamingMode(sendBytes.length);

                                            OutputStream outputStream = con.getOutputStream();
                                            outputStream.write(sendBytes);

                                            int httpResponse = con.getResponseCode();
                                            System.out.println("httpResponse: " + httpResponse);

                                            if (httpResponse >= HttpURLConnection.HTTP_OK
                                                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                                scanner.close();
                                            } else {
                                                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                                scanner.close();
                                            }
                                            System.out.println("jsonResponse:\n" + jsonResponse);

                                        } catch (Throwable t) {
                                            t.printStackTrace();
                                        }
                                    }



                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


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

            }
        });



    }

    private void setupFirebase() {
        Bundle b = getIntent().getExtras();
        try {
            if(b.containsKey("postNum")) {
                Log.d("TAG", "HEY WE GOTTA" + String.valueOf(b.getInt("postNum")));
                Integer postNum = b.getInt("postNum");
                ref = FirebaseDatabase.getInstance().getReference().child("feed").child(String.valueOf(postNum)).child("commentList");
                mQuery = ref;
                fab.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupRecyclerview() {
        mMyAdapter = new commentsAdapter(mQuery, mAdapterItems, mAdapterKeys, feedCommentsView.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(feedCommentsView.this));
        recyclerView.setAdapter(mMyAdapter);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);


    }
}
