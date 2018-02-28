package com.almurray.android.almurrayportal;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.almurray.android.almurrayportal.anti.antiPage;
import com.almurray.android.almurrayportal.feedUtils.*;
import com.almurray.android.almurrayportal.feeds.feedMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jetradarmobile.snowfall.SnowfallView;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nl.siegmann.epublib.epub.Main;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profileViewTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profileViewTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileViewTab extends Fragment implements OSSubscriptionObserver {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressBar loader;
    private OnFragmentInteractionListener mListener;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("users");
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference globalRef = FirebaseDatabase.getInstance().getReference().child("globalvariables");


    FloatingActionButton refreshP;
    FloatingActionButton adminP;
    ImageView profilePic;
    Button logoutButton;
    Button prayerButton;
    FloatingActionButton clearButton;
    Button settingsButton;
    Button staffButton;

    LinearLayout top;


    public profileViewTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileViewTab.
     */
    // TODO: Rename and change types and number of parameters
    public static profileViewTab newInstance(String param1, String param2) {
        profileViewTab fragment = new profileViewTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    SnowfallView profileSnow;
    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {


                double random = Math.random();

                //if(random < 0.35) {
                    /**
                     *
                     * THIS IS CODE TO RANDOMLY SHOW A PAGE
                     */
//                    android.support.v4.app.Fragment fragment = null;
//                    Class fragmentClass = null;
//                    fragmentClass = feedPage.class;
//                    try {
//                        fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
//                    } catch (Exception ei) {
//                        ei.printStackTrace();
//                    }
//                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                //} else {


                    loader = getActivity().findViewById(R.id.chatloader);
                    loader.setVisibility(View.INVISIBLE);
                    final Intent intent = getActivity().getIntent();
                    staffButton = getActivity().findViewById(R.id.staffProfilePanel);
                    staffButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), staffPanel.class));
                        }
                    });
                    top = getActivity().findViewById(R.id.profileTopIdentifier);
                    profileSnow = getActivity().findViewById(R.id.profileSnow);
                    SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = prefs.edit();
                    Log.d("TAG", "**f*d*d*f*df/df/d*/*** :::::;:: "+prefs.getString("onesignal", ""));
                    if (prefs.getBoolean("snowState", true)) {
                        profileSnow.setVisibility(View.GONE);
                    } else {
                        profileSnow.setVisibility(View.GONE);
                    }
                    if (prefs.getBoolean("musicState", true)) {
                        getActivity().startService(new Intent(getActivity(), SoundService.class));
                    } else {
                        try {
                            getActivity().stopService(new Intent(getActivity(), SoundService.class));
                        } catch (Exception e) {
                            Log.d("TAG", e.getMessage());
                        }
                    }


                    settingsButton = getView().findViewById(R.id.settingsButton);
                    settingsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("currentState", "settings");
                            startActivity(new Intent(getActivity(), SettingsStore.class));
                        }
                    });

                    refreshP = getView().findViewById(R.id.refreshButton);
                    refreshP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getStats();
                        }
                    });

                    adminP = getView().findViewById(R.id.adminButton);
                    adminP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            intent.putExtra("currentState", "admin");
                            Intent i = new Intent(getActivity(), AdminView.class);
                            startActivity(i);
                        }
                    });

                    clearButton = getView().findViewById(R.id.clearButton);
                    clearButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot repSnap : dataSnapshot.getChildren()) {
                                        repSnap.child("simplePrayers").getRef().setValue(0);
                                        repSnap.child("complexPrayers").getRef().setValue(0);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });


                    profilePic = getView().findViewById(R.id.profileSmallImageView);
                    profilePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getActivity(), profileView.class);
                            startActivity(i);
                        }
                    });

                    logoutButton = getView().findViewById(R.id.logoutButton);
                    logoutButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OneSignal.setSubscription(false);
                            FirebaseAuth.getInstance().signOut();
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), Login.class));
                        }
                    });

                    prayerButton = getView().findViewById(R.id.goToPrayerRoom);
                    prayerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getActivity(), prayerRoom.class));
                        }
                    });

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String sendbirdID = dataSnapshot.child("sendbird").getValue(String.class);
                            final SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditor = prefs.edit();
                            prefsEditor.putString("staffLevel", "member");
                            prefsEditor.commit();
                            Boolean anti = dataSnapshot.child("anti").getValue(Boolean.class);
                            if (anti) {
                                android.support.v4.app.Fragment fragment = null;
                                Class fragmentClass = null;
                                fragmentClass = antiPage.class;
                                try {
                                    fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                                } catch (Exception ei) {
                                    ei.printStackTrace();
                                }
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                            }


                            String aPoints = dataSnapshot.child("AmigoPoints").getValue(String.class);
                            TextView aT = (TextView) getView().findViewById(R.id.amigoPointsLabel);
                            aT.setText(aPoints);

                            String pPoints = dataSnapshot.child("pPoints").getValue(String.class);
                            TextView pT = (TextView) getView().findViewById(R.id.positivePointsLabel);
                            pT.setText(pPoints);


                            String aRank = dataSnapshot.child("amigoRank").getValue(String.class);
                            TextView aRT = (TextView) getView().findViewById(R.id.amigoRankLabel);
                            aRT.setText("Amigo Rank: " + aRank);

                            String pRank = dataSnapshot.child("rank").getValue(String.class);
                            TextView pRT = (TextView) getView().findViewById(R.id.positiveRankLabel);
                            pRT.setText("Positive Rank: " + pRank);

                            String sName = dataSnapshot.child("sName").getValue(String.class);
                            TextView snT = (TextView) getView().findViewById(R.id.spanishNameLabel);
                            snT.setText(sName);

                            String fullName = dataSnapshot.child("fullName").getValue(String.class);
                            TextView fnT = (TextView) getView().findViewById(R.id.profileNameLabel);
                            fnT.setText(fullName);
                            prefsEditor.putString("currentFullName", fullName);
                            prefsEditor.commit();

                            String team = dataSnapshot.child("team").getValue(String.class);
                            TextView rT = (TextView) getView().findViewById(R.id.profileTeamLabel);
                            rT.setText(team);

                            String standing = dataSnapshot.child("standing").getValue(String.class);
                            TextView sT = (TextView) getView().findViewById(R.id.standingLabel);
                            sT.setText("Current Standing: " + standing);

                            String uid = dataSnapshot.child("uid").getValue(String.class);
                            prefsEditor.putString("currentUserUID", uid);
                            prefsEditor.commit();


                            String urlTo = dataSnapshot.child("urToImage").getValue(String.class);
                            Context context = getContext();
                            de.hdodenhof.circleimageview.CircleImageView pI = getView().findViewById(R.id.profileSmallImageView);
                            Picasso.with(context).load(urlTo).into(pI);


                            prefsEditor.putString("urlToImage", urlTo);
                            prefsEditor.putString("sendbirdIDC", sendbirdID);
                            prefsEditor.commit();

                            SendBird.connect(prefs.getString("sendbirdIDC", ""), new SendBird.ConnectHandler() {
                                @Override
                                public void onConnected(User user, SendBirdException e) {
                                    Log.d("TAG", "Connected");
                                    OneSignal.sendTag("sendbird", prefs.getString("sendbirdIDC", ""));
                                }
                            });


                            Boolean staffB = dataSnapshot.child("staff").getValue(Boolean.class);
                            if (staffB) {
                                prefsEditor.putString("staffLevel", "staff");
                                prefsEditor.commit();
                                staffButton.setVisibility(View.VISIBLE);
                            }

                            Boolean adminB = dataSnapshot.child("admin").getValue(Boolean.class);
                            if (adminB) {
                                prefsEditor.putString("staffLevel", "admin");
                                prefsEditor.commit();
                                adminP.setVisibility(View.VISIBLE);
                            }


                            Boolean bannedB = dataSnapshot.child("banned").getValue(Boolean.class);
                            if (bannedB) {
                                prefsEditor.putString("bannedR", dataSnapshot.child("bannedR").getValue(String.class));
                                prefsEditor.commit();
                                Intent i = new Intent(getActivity(), bannedActivity.class);
                                getActivity().finish();
                                startActivity(i);

                            }

                            dataSnapshot.child("currentVer").getRef().setValue("2.8.0.1");


                            String question1 = dataSnapshot.child("question1").getValue(String.class);
                            String question2 = dataSnapshot.child("question2").getValue(String.class);
                            String answer1 = dataSnapshot.child("answer1").getValue(String.class);
                            String answer2 = dataSnapshot.child("answer2").getValue(String.class);
                            prefsEditor.putString("question1", question1);
                            prefsEditor.putString("question2", question2);
                            prefsEditor.putString("answer1", answer1);
                            prefsEditor.putString("answer2", answer2);

                            prefsEditor.apply();
                            OneSignal.setSubscription(true);
                            if(!prefs.getString("has2.8", "").equals("true")) {
                               startActivity(new Intent(getActivity(), whatsnew.class));
                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    ref.addListenerForSingleValueEvent(eventListener);




                    ValueEventListener globListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //TODO
                            Boolean maintenance = dataSnapshot.child("maintenance").getValue(Boolean.class);
                            if (maintenance) {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), maintenanceActivity.class));
                            }

                            String latestVer = dataSnapshot.child("latestVer").getValue(String.class);
                            String latestAlpha = dataSnapshot.child("currentAlpha").getValue(String.class);
                            String latestBeta = dataSnapshot.child("currentBeta").getValue(String.class);





                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    globalRef.addListenerForSingleValueEvent(globListener);
                //}

                ((MainNav)getActivity()).setDrawer(true);
            }
        };

        handler.post(updater);


    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getActivity().setTitle("Profile");






    }

    public void getStats() {

        final OvershootInterpolator interpolator = new OvershootInterpolator();

        ViewCompat.animate(refreshP).rotation(refreshP.getRotation() + 1440f).withLayer().setDuration(4000).setInterpolator(interpolator).start();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                String  aPoints = dataSnapshot.child("AmigoPoints").getValue(String.class);
                TextView aT = (TextView)getView().findViewById(R.id.amigoPointsLabel);
                aT.setText(aPoints);

                String  pPoints = dataSnapshot.child("pPoints").getValue(String .class);
                TextView pT = (TextView)getView().findViewById(R.id.positivePointsLabel);
                pT.setText(pPoints);


                String aRank = dataSnapshot.child("amigoRank").getValue(String.class);
                TextView aRT = (TextView)getView().findViewById(R.id.amigoRankLabel);
                aRT.setText("Amigo Rank: "+aRank);

                String pRank = dataSnapshot.child("rank").getValue(String.class);
                TextView pRT = (TextView)getView().findViewById(R.id.positiveRankLabel);
                pRT.setText("Positive Rank: "+pRank);

                String sName = dataSnapshot.child("sName").getValue(String.class);
                TextView snT = (TextView)getView().findViewById(R.id.spanishNameLabel);
                snT.setText(sName);

                String fullName = dataSnapshot.child("fullName").getValue(String.class);
                TextView fnT = (TextView)getView().findViewById(R.id.profileNameLabel);
                fnT.setText(fullName);

                String team = dataSnapshot.child("team").getValue(String.class);
                TextView rT = (TextView)getView().findViewById(R.id.profileTeamLabel);
                rT.setText(team);

                String standing = dataSnapshot.child("standing").getValue(String.class);
                TextView sT = (TextView)getView().findViewById(R.id.standingLabel);
                sT.setText("Current Standing: "+standing);


                String urlTo = dataSnapshot.child("urToImage").getValue(String.class);
                Context context = getContext();
                de.hdodenhof.circleimageview.CircleImageView pI = getView().findViewById(R.id.profileSmallImageView);
                Picasso.with(context).load(urlTo).into(pI);

                SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs .edit();
                prefsEditor.putString("urlToImage", urlTo);
                prefsEditor.commit();

                Boolean adminB = dataSnapshot.child("admin").getValue(Boolean.class);
                if(adminB == true) {
                    adminP.setVisibility(View.VISIBLE);
                }

                String reqCode = dataSnapshot.child("code").getValue(String.class);
                prefsEditor.putString("reqCode", reqCode);
                prefsEditor.commit();


                Boolean bannedB = dataSnapshot.child("banned").getValue(Boolean.class);
                if (bannedB == true) {
                    Intent i = new Intent(getActivity(), bannedActivity.class);
                    getActivity().finish();
                    startActivity(i);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
        ValueEventListener globListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean maintenance = dataSnapshot.child("maintenance").getValue(Boolean.class);
                if(maintenance) {
                    //TODO
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), maintenanceActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        globalRef.addListenerForSingleValueEvent(globListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_view_tab, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onRefresh(View v) {

    }

    @Override
    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        SharedPreferences preferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("onesignal", stateChanges.getTo().getUserId());
        edit.apply();
        Log.d("TAG", "***********ID INCOMING***** : "+stateChanges.getTo().getUserId());
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
