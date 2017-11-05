package com.almurray.android.almurrayportal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Activity extends AppCompatActivity implements profileViewTab.OnFragmentInteractionListener, calendarFragment.OnFragmentInteractionListener, infoView.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));
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

        viewPager.setCurrentItem(1);
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
}
