package com.almurray.android.almurrayportal;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;



public class ChatNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private FirebaseAuth mAuth;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                try {
                    notificationManager.cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                SharedPreferences.Editor prefsEditor = prefs .edit();
//                if(SendBird.getCurrentUser() == null) {
//                    SendBird.init("BEC8A4BB-2A29-41A1-B361-1FC0EAA628AD", getApplicationContext());
//                    SendBird.connect(prefs.getString("sendbirdIDC", ""), new SendBird.ConnectHandler() {
//                        @Override
//                        public void onConnected(User user, SendBirdException e) {
//                            if (FirebaseInstanceId.getInstance().getToken() == null) return;
//                            SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), new SendBird.RegisterPushTokenWithStatusHandler() {
//                                @Override
//                                public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
//                                    if(e != null){
//                                        e.printStackTrace();
//                                    } else {
//                                        Log.d("TAG", "LOGGED IN");
//
//                                    }
//                                }
//                            });
//
//
//                        }
//                    });
//                }
            }
        };

        handler.post(updater);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs .edit();
            if(SendBird.getCurrentUser() == null) {
                SendBird.connect(prefs.getString("sendbirdIDC", ""), new SendBird.ConnectHandler() {
                    @Override
                    public void onConnected(User user, SendBirdException e) {
                        if (FirebaseInstanceId.getInstance().getToken() == null) return;
                        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), new SendBird.RegisterPushTokenWithStatusHandler() {
                            @Override
                            public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
                                if(e != null){
                                    e.printStackTrace();
                                } else {
                                    Log.d("TAG", "LOGGED IN");

                                }
                            }
                        });


                    }
                });
            }

//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final String sendbirdID = dataSnapshot.child("sendbird").getValue(String.class);
//                SendBird.connect(sendbirdID, new SendBird.ConnectHandler() {
//                    @Override
//                    public void onConnected(User user, SendBirdException e) {
//                        if (FirebaseInstanceId.getInstance().getToken() == null) return;
//                        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), new SendBird.RegisterPushTokenWithStatusHandler() {
//                            @Override
//                            public void onRegistered(SendBird.PushTokenRegistrationStatus pushTokenRegistrationStatus, SendBirdException e) {
//
//                            }
//                        });
//
//
//                        Log.d("TAG", "LOGGED IN USER: "+sendbirdID);
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        String channelUrl = getIntent().getStringExtra("groupChannelUrl");
        if(channelUrl != null) {
            // If started from notification
            Log.d("TAG", "WE ARE STARTING FROM A NOTI BOI");
            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
//            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction()
//                    .replace(R.id.container_group_channel, fragment)
//                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            //if (savedInstanceState == null) {
                // If started from launcher, load list of Open Channels
            Fragment fragment = GroupChannelListFragment.newInstance();

            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();

            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .commit();
            //}
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.chatProfileAction) {
            finish();
            startActivity(new Intent(ChatNav.this, MainNav.class));
        } else if(id == R.id.chatEventsAction) {
            finish();
            startActivity(new Intent(ChatNav.this, MainNav.class));
        } else if(id == R.id.chatRequestsAction) {
            finish();
            startActivity(new Intent(ChatNav.this, MainNav.class));
        } else if(id == R.id.chatInfoAction) {
            finish();
            startActivity(new Intent(ChatNav.this, MainNav.class));
        } else if(id == R.id.ChatChatAction) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
