package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.almurray.android.almurrayportal.utils.PreferenceUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link chatfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link chatfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chatfragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ProgressBar loader;

    public chatfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chatfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static chatfragment newInstance(String param1, String param2) {
        chatfragment fragment = new chatfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor pE = preferences.edit();
        pE.putInt("nON", 0);
        pE.commit();
        ShortcutBadger.removeCount(getContext());



        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem nav_camara = menu.findItem(R.id.nav_camera);
        nav_camara.setTitle("Chat");

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        loader = getActivity().findViewById(R.id.chatloader);
        loader.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        final String channelUrl = getActivity().getIntent().getStringExtra("groupChannelUrl");
        if(channelUrl != null) {
            // If started from notification
            SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

            Log.d("TAG","Connected");
            Log.d("TAG", channelUrl);
            Log.d("TAG", "WE ARE STARTING FROM A NOTI BOI");
            Fragment fragment = GroupChannelListFragment.newInstance();
//            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction()
//                    .replace(R.id.container_group_channel, fragment)
//                    .commit();
            SharedPreferences.Editor pE = prefs.edit();

            pE.putString("inChannelURL", channelUrl);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();


        } else {
            //if (savedInstanceState == null) {
            // If started from launcher, load list of Open Channels

            Intent intent = getActivity().getIntent();
            if(intent.hasExtra("inChatURL")) {
                Log.d("TAG", "WE ARE STARTING FROM AN NOTI BOI");
                Fragment fragment = GroupChannelListFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("toChatUrl", intent.getStringExtra("inChatURL"));

                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();

                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .commit();
            } else {
                Log.d("TAG", "WE ARE STARTING FROM AN INSTANCEY BOI");
                Fragment fragment = GroupChannelListFragment.newInstance();
                Bundle args = new Bundle();

                android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();

                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .commit();
            }

            //}
        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatfragment, container, false);
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
