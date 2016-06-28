package com.me.salik.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.asyncTask.GetOrdersAsyncTask;
import com.me.salik.common.Common;
import com.me.salik.modal.DataManagement;
import com.me.salik.view.activity.MainActivity;
import com.me.salik.view.adapter.CustomerPagerAdapter;
import com.me.salik.view.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MAC on 6/13/16.
 */
public class CustomerFragment extends BaseFragment {

    LayoutInflater layoutInflater;
    MainActivity delegate;

    View rootView;

    TabLayout tabLayout;
    ViewPager viewPager;
    CustomerPagerAdapter adapter;

    int position;

    public CustomerFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_customer, container,false);
        layoutInflater = inflater;

        Bundle bundle = this.getArguments();
        position = bundle.getInt("position", 0);

        new GetOrdersAsyncTask((MainActivity)getActivity(), this, ((MainActivity)getActivity()).getParams());


        initUI();
        return this.rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();

        // initialize members
    }

    public void initUI(){
        tabLayout = (TabLayout)rootView.findViewById(R.id.tab_layout);
        assert tabLayout != null;
        tabLayout.addTab(tabLayout.newTab().setText("List Customers"));
        tabLayout.getTabAt(0).setIcon(R.drawable.selector_list);
        tabLayout.addTab(tabLayout.newTab().setText("Map Customers"));
        tabLayout.getTabAt(1).setIcon(R.drawable.selector_map);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(position).select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager = (ViewPager)rootView.findViewById(R.id.pager);
        adapter = new CustomerPagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    public void getOrdersSuccess(JSONObject object){
        fetchOrderInfos(object);
    }

    public void getOrdersFail(){
        showMsg(getString(R.string.connection_error));
    }

    private void fetchOrderInfos(JSONObject object){
        JSONArray array = null;
        try {
            array = object.getJSONArray(Common.ORDERS);
            DataManagement.getInstance().setOrderInfos(array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
