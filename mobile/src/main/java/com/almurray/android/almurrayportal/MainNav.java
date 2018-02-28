package com.almurray.android.almurrayportal;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.almurray.android.almurrayportal.anti.antiPage;
import com.almurray.android.almurrayportal.feedUtils.feedPage;
import com.almurray.android.almurrayportal.feeds.feedMain;
import com.almurray.android.almurrayportal.info.infoFragment;
import com.almurray.android.almurrayportal.utils.MyFirebaseInstanceIDService;
import com.almurray.android.almurrayportal.utils.MyFirebaseMessagingService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.prefs.Preferences;

import io.github.tonnyl.whatsnew.WhatsNew;
import io.github.tonnyl.whatsnew.item.WhatsNewItem;
import io.github.tonnyl.whatsnew.util.PresentationOption;
import nl.siegmann.epublib.epub.Main;

public class MainNav extends AppCompatActivity
        implements OSSubscriptionObserver, NavigationView.OnNavigationItemSelectedListener, infoFragment.OnFragmentInteractionListener, feedPage.OnFragmentInteractionListener, feedMain.OnFragmentInteractionListener, antiPage.OnFragmentInteractionListener,  chatfragment.OnFragmentInteractionListener, profileViewTab.OnFragmentInteractionListener, calendarFragment.OnFragmentInteractionListener, infoView.OnFragmentInteractionListener, chatView.OnFragmentInteractionListener, requestsTab.OnFragmentInteractionListener{



    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    ProgressBar chatloader;


    public void refreshGary() {


    }

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        SendBird.init("BEC8A4BB-2A29-41A1-B361-1FC0EAA628AD", getApplicationContext());

        startService(new Intent(MainNav.this, MyFirebaseInstanceIDService.class));
        startService(new Intent(MainNav.this, MyFirebaseMessagingService.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        if(prefs.getBoolean("musicState", true)) {
           // startActivity(new Intent(MainNav.this, SoundService.class));
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            notificationManager.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Loading...");
        chatloader = findViewById(R.id.chatloader);
        chatloader.setVisibility(View.VISIBLE);







        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setDrawer(false);


//        if(!prefs.getString("has205", "").equals("true")) {
//            Log.d("TAG", "SHOULD BE SEEING");
//            WhatsNew whatsNew = WhatsNew.newInstance(
//                    new WhatsNewItem("Notifications", "A new style of notifications, read the announcements chat for more.", R.drawable.ic_heart),
//                    new WhatsNewItem("Fixes", "Many fixes, including the annoying chat picture bug."),
//                    new WhatsNewItem("Changes", "Removed the requests tab (visually at least)")
//
//
//            );
//
//
//            whatsNew.presentAutomatically(MainNav.this);
//            edit.putString("has205", "true");
//            edit.apply();
//        }



    }

    public void setDrawer(boolean enabled) {
        if(enabled) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
        }
    }

    public void getStatus() {
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean simbanned = dataSnapshot.child("banned").getValue(Boolean.class);
                if(simbanned) {
                    finish();
                    startActivity(new Intent(MainNav.this, bannedActivity.class));
                }

                Boolean chatbanned = dataSnapshot.child("chatBan").getValue(Boolean.class);
                if(chatbanned) {
                    SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor pE = preferences.edit();
                    pE.putBoolean("chatBan", true);
                    pE.commit();
                } else {
                    SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor pE = preferences.edit();
                    pE.putBoolean("chatBan", false);
                    pE.commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ref = FirebaseDatabase.getInstance().getReference().child("globalvariables");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean maintenance = dataSnapshot.child("maintenance").getValue(Boolean.class);
                if(maintenance) {

                    finish();
                    startActivity(new Intent(MainNav.this, maintenanceActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();



        Runnable updater = new Runnable() {



            public void run() {

                OneSignal.startInit(MainNav.this).inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None).unsubscribeWhenNotificationsAreDisabled(false).init();
                OSPermissionSubscriptionState os = OneSignal.getPermissionSubscriptionState();
                Log.d("TAG", "***************"+os.getSubscriptionStatus().getUserId());
                SharedPreferences prefs2 = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs2.edit();
                edit.putString("onesignal", os.getSubscriptionStatus().getUserId());
                edit.apply();

                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    finish();
                    startActivity(new Intent(MainNav.this, Login.class));
                }

                getStatus();

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                try {
                    notificationManager.cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                if(prefs.getInt("nON", 0) > 0) {
                    Menu menu = navigationView.getMenu();
                    MenuItem nav_camara = menu.findItem(R.id.nav_camera);
                    nav_camara.setTitle(String.format("Chat (%s Unread Messages)", prefs.getInt("nON", 0)));

                } else {
                    Menu menu = navigationView.getMenu();
                    MenuItem nav_camara = menu.findItem(R.id.nav_camera);
                    nav_camara.setTitle("Chat");
                }


                final Intent intent = getIntent();
                if(intent.hasExtra("toChat")) {
                    Log.d("TAG", "Passed trial 1");
                    if(intent.getStringExtra("toChat").equals("deffo")) {
                        Log.d("TAG", "Passed trial 2");

                        try {
                            SendBird.connect(prefs.getString("sendbirdIDC", ""), new SendBird.ConnectHandler() {
                                @Override
                                public void onConnected(User user, SendBirdException e) {
                                    Log.d("TAG", "Passed trial 3");


                                    android.support.v4.app.Fragment fragment = null;
                                    Class fragmentClass = null;
                                    fragmentClass = chatfragment.class;
                                    try {
                                        fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                                    } catch (Exception ei) {
                                        ei.printStackTrace();
                                    }
                                    String url = intent.getStringExtra("toChatUrl");
                                    Bundle bundle = new Bundle();
                                    getIntent().putExtra("toChatUrl", url);
                                    bundle.putString("toChatUrl", url);
                                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragment.setArguments(bundle);
                                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                                    Log.d("TAG", "Passed trial 4");


                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if(intent.hasExtra("currentState")){
                    if(intent.getStringExtra("currentState").equals("profile")) {
                        android.support.v4.app.Fragment fragment = null;
                        Class fragmentClass = null;
                        fragmentClass = profileViewTab.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception ei) {
                            ei.printStackTrace();
                        }
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                    } else if(intent.getStringExtra("currentState").equals("feed")) {
                        android.support.v4.app.Fragment fragment = null;
                        Class fragmentClass = null;
                        fragmentClass = feedPage.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception ei) {
                            ei.printStackTrace();
                        }
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


                    } else if(intent.getStringExtra("currentState").equals("calendar")) {
                        android.support.v4.app.Fragment fragment = null;
                        Class fragmentClass = null;
                        fragmentClass = calendarFragment.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception ei) {
                            ei.printStackTrace();
                        }
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                    }
                    else if(intent.getStringExtra("currentState").equals("requests")) {
                        android.support.v4.app.Fragment fragment = null;
                        Class fragmentClass = null;
                        fragmentClass = requestsTab.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception ei) {
                            ei.printStackTrace();
                        }
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                    }
                    else if(intent.getStringExtra("currentState").equals("info")) {
                        android.support.v4.app.Fragment fragment = null;
                        Class fragmentClass = null;
                        fragmentClass = infoFragment.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception ei) {
                            ei.printStackTrace();
                        }
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                    }
                    else if(intent.getStringExtra("currentState").equals("chat")) {
                        android.support.v4.app.Fragment fragment = null;
                        Class fragmentClass = null;
                        fragmentClass = chatfragment.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception ei) {
                            ei.printStackTrace();
                        }
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                    }

                } else {
                    Log.d("TAG", "Failed trial 1");

                    try {
                        SendBird.connect(prefs.getString("sendbirdIDC", ""), new SendBird.ConnectHandler() {
                            @Override
                            public void onConnected(User user, SendBirdException e) {


                                android.support.v4.app.Fragment fragment = null;
                                Class fragmentClass = null;
                                fragmentClass = profileViewTab.class;
                                try {
                                    fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                                } catch (Exception ei) {
                                    ei.printStackTrace();
                                }
                                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                //                SendBird.init("BEC8A4BB-2A29-41A1-B361-1FC0EAA628AD", getApplicationContext());
//                if(SendBird.getConnectionState() != SendBird.ConnectionState.OPEN && SendBird.getConnectionState() != SendBird.ConnectionState.CONNECTING) {
//                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(final DataSnapshot dataSnapshot) {
//                            SendBird.connect(dataSnapshot.child("sendbird").getValue(String.class), new SendBird.ConnectHandler() {
//                                @Override
//                                public void onConnected(User user, SendBirdException e) {
//                                    Log.d("TAG", "CONNECTED: " + dataSnapshot.child("sendbird").getValue(String.class) + " FOR NOTY");
////                                    android.support.v4.app.Fragment fragment2 = null;
////                                    Class fragmentClass2 = null;
////                                    fragmentClass2 = chatfragment.class;
////                                    try {
////                                        fragment2 = (android.support.v4.app.Fragment) fragmentClass2.newInstance();
////                                    } catch (Exception i) {
////                                        i.printStackTrace();
////                                    }
////
////                                    android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
////                                    fragmentManager2.beginTransaction().replace(R.id.flContent, fragment2).commit();
//                                }
//                            });
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
             //   }

            }
        };

        handler.post(updater);
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
        getMenuInflater().inflate(R.menu.main_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        switch (item.getItemId()) {
//            case R.id.profileNoties:
//                //startActivity(new Intent(MainNav.this, noties.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_profile) {
            fragmentClass = profileViewTab.class;
            try {
                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getIntent().putExtra("currentState", "profile");
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            chatloader.setVisibility(View.INVISIBLE);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if(id == R.id.nav_garygram) {
            fragmentClass = feedPage.class;
            try {
                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getIntent().putExtra("currentState", "feed");

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            chatloader.setVisibility(View.INVISIBLE);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if(id == R.id.nav_events) {
            fragmentClass = calendarFragment.class;
            try {
                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getIntent().putExtra("currentState", "calendar");

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            chatloader.setVisibility(View.INVISIBLE);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if(id == R.id.nav_requests) {
            fragmentClass = requestsTab.class;
            try {
                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getIntent().putExtra("currentState", "requests");

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            chatloader.setVisibility(View.INVISIBLE);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if(id == R.id.nav_info) {
            fragmentClass = infoFragment.class;
            try {
                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getIntent().putExtra("currentState", "info");

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            chatloader.setVisibility(View.INVISIBLE);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if(id == R.id.nav_camera) {
            fragmentClass = chatfragment.class;
            try {
                fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            getIntent().putExtra("currentState", "chat");

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            chatloader.setVisibility(View.INVISIBLE);


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }


        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {

        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("onesignal", stateChanges.getTo().getUserId());
        edit.apply();
        Log.d("TAG", "***********ID INCOMING***** : "+stateChanges.getTo().getUserId());
    }
}
