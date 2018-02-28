package com.almurray.android.almurrayportal.chatUsers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.almurray.android.almurrayportal.CreateGroupChannelActivity;
import com.almurray.android.almurrayportal.R;
import com.almurray.android.almurrayportal.SelectDistinctFragment;
import com.almurray.android.almurrayportal.SelectUserFragment;
import com.almurray.android.almurrayportal.feedUtils.FeedAdapter;
import com.almurray.android.almurrayportal.feedUtils.post;
import com.almurray.android.almurrayportal.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBirdException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class chatUserList extends AppCompatActivity implements SelectUserFragment.UsersSelectedListener, SelectDistinctFragment.DistinctSelectedListener {

    private Query mQuery;
    RecyclerView recyclerView;
    private userAdapter adapter;
    private static ArrayList<userChat> mAdapterItems;
    private ArrayList<String> mAdapterKeys;

    DatabaseReference ref;


    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";
    static final int STATE_SELECT_USERS = 0;
    static final int STATE_SELECT_DISTINCT = 1;
    private static Button nextButton;
    private static List<String> mSelectedIds;
    private boolean mIsDistinct = true;

    private int mCurrentState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_list);
        mSelectedIds = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.chatUsersRecycler);

        nextButton = (Button) findViewById(R.id.newCreateChatButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_SELECT_USERS) {
//                if (mCurrentState == STATE_SELECT_DISTINCT) {
                    mIsDistinct = PreferenceUtils.getGroupChannelDistinct(chatUserList.this);
                    createGroupChannel(mSelectedIds, mIsDistinct);
                }
            }
        });
        setupFirebase();
        setRecyclerView();

    }

    void setState(int state) {
        if (state == STATE_SELECT_USERS) {
            mCurrentState = STATE_SELECT_USERS;
            nextButton.setVisibility(View.VISIBLE);
//            mCreateButton.setVisibility(View.GONE);
//            mNextButton.setVisibility(View.VISIBLE);
        } else if (state == STATE_SELECT_DISTINCT){
            mCurrentState = STATE_SELECT_DISTINCT;
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private void setupFirebase() {
        ref = FirebaseDatabase.getInstance().getReference().child("chatUsers");
        mQuery = ref;
    }

    private void setRecyclerView() {
        adapter = new userAdapter(mQuery, mAdapterItems, mAdapterKeys, chatUserList.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(chatUserList.this));
        recyclerView.setAdapter(adapter);
    }

    public void createGroupChannel(final List<String> userIds, boolean distinct) {
        GroupChannel.createChannelWithUserIds(userIds, distinct, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }
                SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                setResult(RESULT_OK, intent);
                finish();
                for(int i = 0; i < userIds.size(); i++) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    if (!userIds.get(i).equals(prefs.getString("sendbirdIDC", ""))) {
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



                            String strJsonBody = "{"
                                    + "\"app_id\": \"2144f1b0-6c2c-44e2-ab7d-80fbae543240\","
                                    + "\"headings\": {\"en\": \"Hey!\"},"
                                    + "\"ios_badgeType\": \"Increase\","
                                    + "\"ios_badgeCount\": \"1\","
                                    + "\"mutable_content\": \"true\","
                                    + "\"content_available\": \"true\","
                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"sendbird\", \"relation\": \"=\", \"value\": \""+userIds.get(i)+"\"}],"
                                    + "\"data\": {\"foo\": \"bar\"},"
                                    + "\"contents\": {\"en\": \"You were added to a group chat by: "+prefs.getString("currentFullName", "")+"\"}"
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
                }

                }



        });
    }

    public static void onSelected(boolean selected, String userID) {
        if (selected) {
            mSelectedIds.add(userID);
        } else {
            mSelectedIds.remove(userID);
        }

        if (mSelectedIds.size() > 0) {
            nextButton.setEnabled(true);
//            mNextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
//            mNextButton.setEnabled(false);
        }


    }

    @Override
    public void onUserSelected(boolean selected, String userId) {

        if (selected) {
            mSelectedIds.add(userId);
        } else {
            mSelectedIds.remove(userId);
        }

        if (mSelectedIds.size() > 0) {
            nextButton.setEnabled(true);
//            mNextButton.setEnabled(true);
        } else {
            nextButton.setEnabled(false);
//            mNextButton.setEnabled(false);
        }
    }

    @Override
    public void onDistinctSelected(boolean distinct) {
        mIsDistinct = distinct;
    }




}


