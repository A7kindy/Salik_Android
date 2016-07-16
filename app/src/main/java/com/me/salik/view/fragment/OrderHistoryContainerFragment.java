package com.me.salik.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.salik.R;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.base.BaseFragment;

/**
 * Created by MAC on 7/4/16.
 */
public class OrderHistoryContainerFragment extends BaseFragment {

    LayoutInflater layoutInflater;
    HomeActivity delegate;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_order_history_container, container,false);
        layoutInflater = inflater;
        delegate = (HomeActivity)getActivity();
        Fragment fragment = new OrdersHistoryFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.nested_fragment_container, fragment).commit();
        return rootView;
    }
}
