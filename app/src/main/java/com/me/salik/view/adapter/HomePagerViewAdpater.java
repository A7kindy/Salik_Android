package com.me.salik.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.me.salik.view.fragment.OrderHistoryContainerFragment;
import com.me.salik.view.fragment.OrdersFragment;
import com.me.salik.view.fragment.OrdersMapFragment;

/**
 * Created by MAC on 6/30/16.
 */
public class HomePagerViewAdpater extends FragmentPagerAdapter {
    int mNumOfTabs;

    public HomePagerViewAdpater(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;

    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OrdersFragment();
            case 1:
                return new OrdersMapFragment();
            case 2:
                return new OrderHistoryContainerFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
