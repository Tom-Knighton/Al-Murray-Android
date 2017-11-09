package com.almurray.android.almurrayportal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Activity extends AppCompatActivity implements profileViewTab.OnFragmentInteractionListener, calendarFragment.OnFragmentInteractionListener, infoView.OnFragmentInteractionListener, requestsTab.OnFragmentInteractionListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));
        tabLayout.addTab(tabLayout.newTab().setText("Requests"));
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);




        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter =  new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                String hi = String.valueOf(tab.getPosition());
                Log.d("TAG", hi);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setCurrentItem(2);






    }







    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onLogoutClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(Activity.this, Login.class);
        finish();
        startActivity(i);
    }

    private WebView mWebview ;


    public void onAPointRequest(View v) {
        new AlertDialog.Builder(Activity.this)
                .setTitle("Information")
                .setMessage("Please be aware your amigo-ness may be under investigation if you submit more than one amigo points request per 30 days, exceeding this limit may reset your amigo points to 1. This limit does not include Amigo Loans, however you may not request amigo points if you are currently taking out an Amigo Loan. Your request for amigo points will be manually reviewed and if deemed worthy you should recieve your points within 30 working days.")
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
                        Intent i = new Intent(Activity.this, amigoPointForm.class);
                        startActivity(i);


                    }
                }).show();
    }

    public void onPPointRequest(View v) {
        new AlertDialog.Builder(Activity.this)
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
                        Intent i = new Intent(Activity.this, positivePointForm.class);
                        startActivity(i);


                    }
                }).show();
    }

    public void onLoanRequest(View v) {
        new AlertDialog.Builder(Activity.this)
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
                        Intent i = new Intent(Activity.this, amigoLoanForm.class);
                        startActivity(i);


                    }
                }).show();
    }

    public void onManualRequest(View v) {
        new AlertDialog.Builder(Activity.this)
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
                        Intent i = new Intent(Activity.this, manualReviewForm.class);
                        startActivity(i);


                    }
                }).show();
    }

    public void onTip(View v) {
        new AlertDialog.Builder(Activity.this)
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
                        Intent i = new Intent(Activity.this, tipForm.class);
                        startActivity(i);


                    }


                }).show();
    }

    public void onFeedback(View v) {
        new AlertDialog.Builder(Activity.this)
                .setTitle("Information")
                .setMessage("Hey there! Submit feedback now on what you want added or changed to this app!")
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Activity.this, feedBackForm.class);
                        startActivity(i);


                    }


                }).show();
    }




}
