package com.me.salik.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.me.salik.view.fragment.CustomerListFragment;
import com.me.salik.view.fragment.CustomerMapFragment;

/**
 * Created by MAC on 6/13/16.
 */
public class CustomerPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public CustomerPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                CustomerListFragment tab1 = new CustomerListFragment();
                return tab1;
            case 1:
                CustomerMapFragment tab2 = new CustomerMapFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//
//    }
}
