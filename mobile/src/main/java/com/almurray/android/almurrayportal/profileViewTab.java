package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jetradarmobile.snowfall.SnowfallView;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Console;

import javax.sql.StatementEvent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profileViewTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profileViewTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileViewTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("users");
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    FloatingActionButton refreshP;
    FloatingActionButton adminP;
    ImageView profilePic;
    Button logoutButton;
    Button prayerButton;
    FloatingActionButton clearButton;
    Button settingsButton;


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
                profileSnow = getActivity().findViewById(R.id.profileSnow);
                SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs .edit();
                if(prefs.getBoolean("snowState", false)) {
                    profileSnow.setVisibility(View.VISIBLE);
                } else {
                    profileSnow.setVisibility(View.INVISIBLE);
                }


                settingsButton = getView().findViewById(R.id.settingsButton);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                for(DataSnapshot repSnap : dataSnapshot.getChildren()) {
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

                        String question1 = dataSnapshot.child("question1").getValue(String.class);
                        String question2 = dataSnapshot.child("question2").getValue(String.class);
                        String answer1 = dataSnapshot.child("answer1").getValue(String.class);
                        String answer2 = dataSnapshot.child("answer2").getValue(String.class);
                        prefsEditor.putString("question1", question1);
                        prefsEditor.putString("question2", question2);
                        prefsEditor.putString("answer1", answer1);
                        prefsEditor.putString("answer2", answer2);

                        prefsEditor.commit();





                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                ref.addListenerForSingleValueEvent(eventListener);



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
