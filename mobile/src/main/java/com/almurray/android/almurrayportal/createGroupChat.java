package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class createGroupChat extends AppCompatActivity {

    CheckBox tomC;
    CheckBox nickC;

    CheckBox taylorC;
    CheckBox sethC;
    CheckBox georgeC;
    CheckBox joeC;
    CheckBox samC;
    Button nextB;
    private List<String> mSelectedIds;

    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";

    interface UsersSelectedListener {
        void onUserSelected(boolean selected, String userId);
    }


    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {

                tomC = findViewById(R.id.tomCheckBox);
                nickC = findViewById(R.id.nickCheckBox);
                taylorC = findViewById(R.id.taylorCheckBox);
                sethC = findViewById(R.id.sethCheckBox);
                georgeC = findViewById(R.id.georgeCheckBox);
                joeC = findViewById(R.id.joeCheckBox);
                samC = findViewById(R.id.samCheckBox);


                tomC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            mSelectedIds.add("tomk");
                        } else {
                            mSelectedIds.remove("tomk");
                        }
                    }
                });


                georgeC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            mSelectedIds.add("georged");
                        } else {
                            mSelectedIds.remove("georged");
                        }
                    }
                });

                nickC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            mSelectedIds.add("nickw");
                        } else {
                            mSelectedIds.remove("nickw");
                        }
                    }
                });

                taylorC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            mSelectedIds.add("taylorc");
                        } else {
                            mSelectedIds.remove("taylorc");
                        }
                    }
                });

                sethC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            mSelectedIds.add("seth");
                        } else {
                            mSelectedIds.remove("seth");
                        }
                    }
                });

                joeC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            mSelectedIds.add("joei");
                        } else {
                            mSelectedIds.remove("joei");
                        }
                    }
                });

                samC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            mSelectedIds.add("samc");
                        } else {
                            mSelectedIds.remove("samc");
                        }
                    }
                });


                nextB = findViewById(R.id.createChatButton);
                nextB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectedIds = new ArrayList<>();
                        mSelectedIds.add(SendBird.getCurrentUser().getUserId());


                        Log.d("TAG", "HELLO THIS USER 1"+mSelectedIds.get(1));
                        Log.d("TAG", "HELLO USR 0:"+mSelectedIds.get(0));
                        if(mSelectedIds.size() > 1) {
                            createGroupChannel(mSelectedIds);

                        }


                    }
                });



            }
        };

        handler.post(updater);
    }



    private void createGroupChannel(List<String> userIds) {
        GroupChannel.createChannelWithUserIds(userIds, true, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);
    }
}
