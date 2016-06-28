package com.me.salik.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.asyncTask.GetOrdersAsyncTask;
import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.modal.DataManagement;
import com.me.salik.view.activity.MainActivity;
import com.me.salik.view.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    MainActivity delegate;

    View rootView;

    Button next;
    TextView customer_number;

    JSONObject params;


    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        delegate = (MainActivity) getActivity();
        initData();
        initUI();
        new GetOrdersAsyncTask(delegate, this, params).execute();
        return rootView;
    }

    public void initData(){
        params = delegate.getParams();
    }

    public void initUI(){
        customer_number = (TextView)rootView.findViewById(R.id.customer_number);
        customer_number.setText(String.valueOf(DataManagement.getInstance().getOrdersCount()));
        next = (Button)rootView.findViewById(R.id.next);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next){
            delegate.selectMenu(1);
        }
    }

    public void getOrdersSuccess(JSONObject object){
        fetchOrderInfos(object);
        customer_number.setText(String.valueOf(DataManagement.getInstance().getOrdersCount()));

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