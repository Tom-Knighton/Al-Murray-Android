package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.IDNA;
import android.media.Image;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link calendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link calendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class calendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ProgressBar loader;


    public calendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment calendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static calendarFragment newInstance(String param1, String param2) {
        calendarFragment fragment = new calendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events");


    private CardView event1;
    private CardView event2;
    private CardView event3;

    String expandedInfo1;
    String expandedInfo2;
    String expandedInfo3;

    String title1;
    String title2;
    String title3;

    FloatingActionButton info;

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {
                loader = getActivity().findViewById(R.id.chatloader);
                loader.setVisibility(View.INVISIBLE);
                event1 = getActivity().findViewById(R.id.event1Container);
                event2 = getActivity().findViewById(R.id.event2Container);
                event3 = getActivity().findViewById(R.id.event3Container);

                event1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(title1)
                                .setMessage(expandedInfo1)
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                }).show();
                    }
                });

                event2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(title2)
                                .setMessage(expandedInfo2)
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                }).show();
                    }
                });

                event3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle(title3)
                                .setMessage(expandedInfo3)
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                }).show();
                    }
                });


                info = getView().findViewById(R.id.infoCalendarButton);
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Info")
                                .setMessage("This list of upcoming events is displayed from the database, meaning it can be changed dynamically. \n\nEvents are colour coded:\n\nRed=Very Important\nOrange=Important and\nGreen=Not Important.\n\nYou can tap on each event to read more about it.")
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                }).show();
                    }
                });


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
        getActivity().setTitle("Events");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String event1Title = dataSnapshot.child("event1").child("title").getValue(String.class);
                TextView event1TitleText = (TextView)getView().findViewById(R.id.event1Title);
                title1 = event1Title;
                event1TitleText.setText(event1Title);

                String event1Date = dataSnapshot.child("event1").child("date").getValue(String.class);
                TextView event1DateText = (TextView)getView().findViewById(R.id.event1Date);
                event1DateText.setText(event1Date);

                String event1Info = dataSnapshot.child("event1").child("info").getValue(String.class);
                TextView event1InfoText = (TextView)getView().findViewById(R.id.event1Info);
                event1InfoText.setText(event1Info);

                Integer event1Importance = dataSnapshot.child("event1").child("importance").getValue(Integer.class);
                LinearLayout event1Color = (LinearLayout) getView().findViewById(R.id.event1Color);
                if(event1Importance == 1) { event1Color.setBackground(new ColorDrawable(0xFF669900));}
                if(event1Importance == 2) { event1Color.setBackground(new ColorDrawable(0xFFFF8800));}
                if(event1Importance == 3) { event1Color.setBackground(new ColorDrawable(0xFFFF4444));}

                //2
                String event2Title = dataSnapshot.child("event2").child("title").getValue(String.class);
                TextView event2TitleText = (TextView)getView().findViewById(R.id.event2Title);
                title2 = event2Title;
                event2TitleText.setText(event2Title);

                String event2Date = dataSnapshot.child("event2").child("date").getValue(String.class);
                TextView event2DateText = (TextView)getView().findViewById(R.id.event2Date);
                event2DateText.setText(event2Date);

                String event2Info = dataSnapshot.child("event2").child("info").getValue(String.class);
                TextView event2InfoText = (TextView)getView().findViewById(R.id.event2Info);
                event2InfoText.setText(event2Info);

                Integer event2Importance = dataSnapshot.child("event2").child("importance").getValue(Integer.class);
                LinearLayout event2Color = (LinearLayout) getView().findViewById(R.id.event2Color);
                if(event2Importance == 1) { event2Color.setBackground(new ColorDrawable(0xFF669900));}
                if(event2Importance == 2) { event2Color.setBackground(new ColorDrawable(0xFFFF8800));}
                if(event2Importance == 3) { event2Color.setBackground(new ColorDrawable(0xFFFF4444));}

                //3
                String event3Title = dataSnapshot.child("event3").child("title").getValue(String.class);
                TextView event3TitleText = (TextView)getView().findViewById(R.id.event3Title);
                title3 = event3Title;
                event3TitleText.setText(event3Title);

                String event3Date = dataSnapshot.child("event3").child("date").getValue(String.class);
                TextView event3DateText = (TextView)getView().findViewById(R.id.event3Date);
                event3DateText.setText(event3Date);

                String event3Info = dataSnapshot.child("event3").child("info").getValue(String.class);
                TextView event3InfoText = (TextView)getView().findViewById(R.id.event3Info);
                event3InfoText.setText(event3Info);

                Integer event3Importance = dataSnapshot.child("event3").child("importance").getValue(Integer.class);
                LinearLayout event3Color = (LinearLayout) getView().findViewById(R.id.event3Color);
                if(event3Importance == 1) { event3Color.setBackground(new ColorDrawable(0xFF669900));}
                if(event3Importance == 2) { event3Color.setBackground(new ColorDrawable(0xFFFF8800));}
                if(event3Importance == 3) { event3Color.setBackground(new ColorDrawable(0xFFFF4444));}

                //Expanded

                expandedInfo1 = dataSnapshot.child("event1").child("expandedInfo").getValue(String.class);
                expandedInfo2 = dataSnapshot.child("event2").child("expandedInfo").getValue(String.class);
                expandedInfo3 = dataSnapshot.child("event3").child("expandedInfo").getValue(String.class);



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
        return inflater.inflate(R.layout.fragment_calendar, container, false);







    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
