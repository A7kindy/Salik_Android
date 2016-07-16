package com.me.salik.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.me.salik.R;

import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.server.asyncTask.OrderChangeAsyncTask;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.activity.LogInActivity;
import com.me.salik.view.activity.TakeOrderActivity;
import com.me.salik.view.adapter.OrderListAdapter;
import com.me.salik.view.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by MAC on 6/30/16.
 */
public class OrdersFragment extends BaseFragment implements View.OnClickListener{

    View rootView;
    HomeActivity homeActivity;
    ListView listView;
    public OrderListAdapter adapter;

    public OrdersFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_orders, container,false);
        homeActivity = (HomeActivity) getActivity();
        initUI();
        return this.rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void initUI(){
        listView = (ListView)rootView.findViewById(R.id.list_view);
        adapter = new OrderListAdapter(homeActivity);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                homeActivity.index = position;
            }
        });

        (rootView.findViewById(R.id.take_order)).setOnClickListener(this);
        (rootView.findViewById(R.id.logout)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                Intent intent = new Intent(homeActivity, LogInActivity.class);
                startActivity(intent);
                homeActivity.finish();
                break;
            case R.id.take_order:
                if (checkSelectedItem()){
                    homeActivity.setParams1();
                    new OrderChangeAsyncTask(homeActivity, homeActivity.getParams1()).execute();
                } else {
                    showMsg("Please select one client.");
                }
                break;
            default:
                break;
        }
    }

    private boolean checkSelectedItem(){
        return homeActivity.index != -1;
    }


}
