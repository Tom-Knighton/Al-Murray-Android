package com.almurray.android.almurrayportal.feedUtils;

import android.app.Activity;
import android.app.DownloadManager;

import com.almurray.android.almurrayportal.GroupChatFragment;
import com.almurray.android.almurrayportal.MainNav;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.almurray.android.almurrayportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link feedPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link feedPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class feedPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public feedPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment feedPage.
     */
    // TODO: Rename and change types and number of parameters
    public static feedPage newInstance(String param1, String param2) {
        feedPage fragment = new feedPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        getActivity().setTitle("GaryGram");

    }


    private final static String SAVED_ADAPTER_ITEMS = "SAVED_ADAPTER_ITEMS";
    private final static String SAVED_ADAPTER_KEYS = "SAVED_ADAPTER_KEYS";

    private Query mQuery;
    private FeedAdapter mMyAdapter;
    private ArrayList<post> mAdapterItems;
    private ArrayList<String> mAdapterKeys;
    RecyclerView recycler;
    TextView teamText;
    DatabaseReference ref;
    FloatingActionButton fab;
    Button comments,options;

    Context current;



    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_feed_page, container, false);
        recycler = rootView.findViewById(R.id.feedPageRecycler);
        teamText = rootView.findViewById(R.id.feedPageTeam);
        fab = rootView.findViewById(R.id.feedToUpload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), uploadToFeed.class));
            }
        });


        setupFirebase();
        setupRecyclerview();


        return rootView;
    }



    private void setupFirebase() {
        ref = FirebaseDatabase.getInstance().getReference().child("feed");
        mQuery = ref;
    }

    public void setupRecyclerview() {
        mMyAdapter = new FeedAdapter(mQuery, mAdapterItems, mAdapterKeys, getContext());
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(mMyAdapter);
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
            current = context;
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
