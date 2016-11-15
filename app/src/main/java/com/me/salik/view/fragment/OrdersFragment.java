package com.me.salik.view.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.me.salik.R;

import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.location.GPSTracker;
import com.me.salik.modal.DataManagement;
import com.me.salik.modal.OrderInfo;
import com.me.salik.server.asyncTask.GetOrdersAsyncTask;
import com.me.salik.server.asyncTask.OrderChangeAsyncTask;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.activity.LoginActivity;
import com.me.salik.view.adapter.OrderListAdapter;
import com.me.salik.view.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by MAC on 6/30/16.
 */
public class OrdersFragment extends BaseFragment implements View.OnClickListener{

    View rootView;
    HomeActivity homeActivity;
    ListView listView;
    public OrderListAdapter adapter;

    Timer timer;

    public OrdersFragment(){
        autoLoadOrders();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_orders, container,false);
        homeActivity = (HomeActivity) getActivity();
        initUI();

        return this.rootView;
    }

    private void initUI(){
        listView = (ListView)rootView.findViewById(R.id.list_view);
        refresh();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                homeActivity.index = position;
            }
        });

        (rootView.findViewById(R.id.take_order)).setOnClickListener(this);
        (rootView.findViewById(R.id.logout)).setOnClickListener(this);
        (rootView.findViewById(R.id.refresh)).setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                apps.preference.setDriverName(null);
                apps.preference.setDriverPassword(null);
                Intent intent = new Intent(homeActivity, LoginActivity.class);
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
            case R.id.refresh:
                homeActivity.setParams();
                new GetOrdersAsyncTask(homeActivity, OrdersFragment.this, homeActivity.getParams()).execute();
                break;
            default:
                break;
        }
    }

    private boolean checkSelectedItem(){
        return homeActivity.index != -1;
    }

    public void autoLoadOrders() {
        final Handler handler = new Handler();
        timer = new Timer();

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            homeActivity.setParams();
                            new GetOrdersAsyncTask(homeActivity, OrdersFragment.this, homeActivity.getParams()).execute();
                            SalikLog.Error("Auto Update");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000); //execute in every 10000 ms
    }


    public void getOrdersSuccess(JSONObject object){
        fetchOrderInfos(object);

    }

    public void getOrdersFail(){
//        showMsg(getString(R.string.connection_error));
    }

    private void fetchOrderInfos(JSONObject object){
        JSONArray array = null;
        try {
            if (!object.isNull(Common.ORDERS)) {
                array = object.getJSONArray(Common.ORDERS);
                DataManagement.getInstance().setOrderInfos(array);
                refresh();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refresh(){
        //fix distances
        ArrayList<OrderInfo> orderInfos = DataManagement.getInstance().getOrderInfos();
        GPSTracker gpsTracker = new GPSTracker(getContext());
        Location currentLocation = gpsTracker.getLocation();
        for(OrderInfo o: orderInfos){
            double orderLatitude = o.getOrder_location_latitude();
            double orderLongitude = o.getOrder_location_longitude();
            double driverLatitude = currentLocation.getLatitude();
            double driverLongitude = currentLocation.getLongitude();
            double distance = distance(orderLatitude,orderLongitude,driverLatitude, driverLongitude);
            o.setOrder_distance(distance);
        }

        adapter = new OrderListAdapter(homeActivity, DataManagement.getInstance().getOrderInfos());
        listView.setAdapter(adapter);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 *1.609344;
        /*if (unit == 'd') {
            dist = dist * 1609.344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }*/
        return (dist);
    }
}
