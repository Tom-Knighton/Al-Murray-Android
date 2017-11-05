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
                calendarFragment tab1 = new calendarFragment();
                return tab1;
            case 1:
                profileViewTab tab2 = new profileViewTab();
                return tab2;
            case 2:
                infoView tab3 = new infoView();
                return tab3;

            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
