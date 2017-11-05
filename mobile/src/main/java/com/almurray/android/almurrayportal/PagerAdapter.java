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
        profileViewTab tab1 = new profileViewTab();
        return  tab1;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
