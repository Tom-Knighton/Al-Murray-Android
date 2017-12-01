package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link requestsTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link requestsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class requestsTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button amigoPoints;
    Button amigoLoan;
    Button posPoints;
    Button manReview;
    Button confessions;
    Button feedback;




    public requestsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment requestsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static requestsTab newInstance(String param1, String param2) {
        requestsTab fragment = new requestsTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView codeText;


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private String message;
    private String answer;


    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();


        Runnable updater = new Runnable() {

            public void run() {
                final SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                final SharedPreferences.Editor prefsEditor = prefs .edit();


                amigoPoints = (Button)getView().findViewById(R.id.amigoRequest);
                amigoPoints.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Random r = new Random();
                        int rN = r.nextInt((2-1)+1)+1;
                        if(rN == 1) { message = prefs.getString("question1", ""); answer = prefs.getString("answer1", ""); }
                        if(rN == 2) { message = prefs.getString("question2", ""); answer = prefs.getString("answer2", ""); }
                        final EditText secAnswer = new EditText(getActivity());
                        new AlertDialog.Builder(getContext())
                                .setTitle("Security Question")
                                .setView(secAnswer)
                                .setMessage(message)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(secAnswer.getText().toString() == answer.toString()) {
                                            Toast.makeText(getContext(), "hi", Toast.LENGTH_LONG);
                                            Log.d("TAG", "BLUUUUUUUUURG");
                                        } else {
                                            Log.d("TAG", "WRONG");
                                            Log.d("TAG", "ENTERED: "+secAnswer.getText().toString());
                                            Log.d("TAG", "PUT: "+answer.toString());
                                        }
                                    }
                                })
                                .show();




//                        new AlertDialog.Builder(getContext())
//                                .setTitle("Information")
//                                .setMessage("Please be aware your amigo-ness may be under investigation if you submit more than one amigo points request per 30 days, exceeding this limit may reset your amigo points to 1. This limit does not include Amigo Loans, however you may not request amigo points if you are currently taking out an Amigo Loan. Your request for amigo points will be manually reviewed and if deemed worthy you should recieve your points within 30 working days.")
//                                .setCancelable(true)
//                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        dialogInterface.dismiss();
//
//                                    }
//                                })
//                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Random r = new Random();
//                                        int rN = r.nextInt((2-1)+1)+1;
//                                        if(rN == 1) { message = prefs.getString("question1", ""); answer = prefs.getString("answer1", ""); }
//                                        else if(rN == 2) {message = prefs.getString("question2", ""); answer = prefs.getString("answer2", ""); }
//                                        final EditText secAnswer = new EditText(getActivity());
//                                        new AlertDialog.Builder(getContext())
//                                                .setView(secAnswer)
//                                                .setTitle("Security Question")
//                                                .setMessage(message)
//                                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        if(secAnswer.getText().toString() == answer.toString()) {
//                                                            Intent to1 = new Intent(getContext(), amigoPointForm.class);
//                                                            startActivity(to1);
//                                                            Log.d("TAG", answer);
//                                                            Log.d("TAG", "right");
//
//                                                        }
//                                                        else{
//                                                            Toast.makeText(getContext(), "Security question answered incorrectly", Toast.LENGTH_SHORT);
//                                                            dialogInterface.dismiss();
//                                                            Log.d("TAG", answer);
//                                                            Log.d("TAG", "wrong");
//
//                                                        }
//                                                        Log.d("TAG", "COFNIRMED");
//                                                    }
//                                                })
//                                                .setCancelable(true)
//                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        dialogInterface.dismiss();
//                                                    }
//                                                })
//                                                .show();
//
//
//
//
//                                    }
//                                }).show();
//
                         }
                });

                amigoLoan = (Button)getView().findViewById(R.id.loanRequest);
                amigoLoan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Information")
                                .setMessage("Please be aware you amigo-ness may be under investigation if you submit more than one amigo loan per 30 days, exceeding this limit may temporarily suspend your amigoness. If your loan is accepted, you must wait 60 days before requesting another loan. Amigo loans must be payed back with the current interest rate within 365 days. Your request for an amigo loan will be manually reviewed and if deemed worthy you should recieve your loan within 30 working days.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getContext(), amigoLoanForm.class);
                                        startActivity(i);


                                    }
                                }).show();
                    }
                });

                posPoints = (Button)getView().findViewById(R.id.posRequest);
                posPoints.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Information")
                                .setMessage("Please be aware your positivity may be under investigation if you submit more than one positivityness request per 30 days, exceeding this limit may reset your positvity points to 1. If you have recieved an amigo loan recently your application for positvity will be denied.  REQUESTING POSITVITY POINTS IS ONLY AVALIABLE TO THOSE WHO ARE POP 2 OR LOWER. Your request for positivity points will be manually reviewed and if deemed worthy you should recieve your points within 30 working days.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getContext(), positivePointForm.class);
                                        startActivity(i);


                                    }
                                }).show();
                    }
                });

                manReview = (Button)getView().findViewById(R.id.manualRequest);
                manReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Information")
                                .setMessage("If you click 'proceed', your account will be placed under temporary special measures and a SPOPIT and a Sénor Amigo will manually review your account and points. Requesting this more than once every 90 days may possibly result in a complete reset of Amigoness and Positivty, as per Al Murray.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getContext(), manualReviewForm.class);
                                        startActivity(i);


                                    }
                                }).show();
                    }
                });

                confessions = (Button)getView().findViewById(R.id.tip);
                confessions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Information")
                                .setMessage("If you have any information on wanted individuals, negative individuals, or want to report a lack of amigoiness, click 'proceed'. Alternatively, if you would like to commend someone, this is also the place to do it.")
                                .setCancelable(false)
                                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();

                                    }
                                })
                                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getContext(), tipForm.class);
                                        startActivity(i);


                                    }


                                }).show();
                    }
                });

                feedback = (Button)getView().findViewById(R.id.feedback);
                feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Information")
                                .setMessage("Hey there! Submit feedback now on what you want added or changed to this app!")
                                .setCancelable(false)
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getContext(), feedBackForm.class);
                                        startActivity(i);


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

        getActivity().setTitle("Requests");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests_tab, container, false);
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
