package com.almurray.android.almurrayportal.feeds;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almurray.android.almurrayportal.R;
import com.google.android.gms.gcm.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link feedMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link feedMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class feedMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private TextView desc;

    public feedMain() {
        // Required empty public constructor
    }

    FirebaseDatabase database;
    DatabaseReference myRef ;
    List<post> list;
    RecyclerView recycle;


    @Override
    public void onResume() {
        super.onResume();

        Handler handler = new Handler();
        Runnable updater = new Runnable() {
            @Override
            public void run() {


//                final RecyclerAdaptter recyclerAdapter = new RecyclerAdaptter(list,getActivity());
//                recycle.setAdapter(recyclerAdapter);
//                final RecyclerView.LayoutManager recyce = new GridLayoutManager(getActivity(),1);
//                /// RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
//                // recycle.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//                recycle.setLayoutManager(recyce);
//                recycle.setItemAnimator( new DefaultItemAnimator());






            }
        };
        handler.post(updater);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment feedMain.
     */
    // TODO: Rename and change types and number of parameters
    public static feedMain newInstance(String param1, String param2) {
        feedMain fragment = new feedMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerAdaptter adaptter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }



    public void reloadData() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("feed");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<post>();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    post value = dataSnapshot.getValue(post.class);
                    post Post = new post();

                    String posterName = value.getPosterName();
                    String posterURL = value.getPosterURL();
                    String Desc = value.getDesc();
                    Integer Likes = value.getLikes();
                    String postURL = value.getPostURL();
                    Integer Comments = value.getComments();

                    Post.setPosterName(posterName);
                    Post.setDesc(Desc);
                    Post.setComments(Comments);
                    Post.setPostURL(postURL);
                    Post.setPosterURL(posterURL);
                    Post.setLikes(Likes);

                    list.add(Post);
                    adaptter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_feed_main, container, false);

        recycle = (RecyclerView) rootview.findViewById(R.id.feedRecycler);


        list = new ArrayList<post>();
        adaptter = new RecyclerAdaptter(list, getContext());
        recycle.setAdapter(adaptter);
        reloadData();
        return rootview;
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
