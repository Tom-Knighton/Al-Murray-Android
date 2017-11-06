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
                requestsTab tab2 = new requestsTab();
                return tab2;
            case 2:
                profileViewTab tab3 = new profileViewTab();
                return tab3;
            case 3:
                infoView tab4 = new infoView();
                return tab4;

            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
