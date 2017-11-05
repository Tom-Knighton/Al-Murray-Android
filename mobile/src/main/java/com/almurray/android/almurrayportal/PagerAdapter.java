package com.almurray.android.almurrayportal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by tom on 04/11/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs) {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }




    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                profileViewTab tab1 = new profileViewTab();
                return tab1;
            case 1:
                calendarFragment tab2 = new calendarFragment();
                return tab2;

            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
