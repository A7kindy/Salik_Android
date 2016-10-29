package com.me.salik.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;


import com.me.salik.R;
import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.modal.DataManagement;
import com.me.salik.server.asyncTask.GetHistoryAsyncTask;
import com.me.salik.server.asyncTask.GetOrdersAsyncTask;
import com.me.salik.view.adapter.HomePagerViewAdpater;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Stack;

public class HomeActivity extends BaseActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    TabLayout tabLayout;
    ViewPager viewPager;
    HomePagerViewAdpater adapter;

    JSONObject params, params1, historyParams;

    int position = 0;
    public int index = -1;
    public int orderState = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupTab();

        //check permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else{
            initUI(0);
        }

        //check for incompleteOrder
        int currentOrderId = apps.preference.getCurrentOrderId();
        if(currentOrderId != -99){
            Intent intent = new Intent(this,TakeOrderActivity.class);
            intent.putExtra(Common.CURRENT_ORDER_ID,currentOrderId);
            intent.putExtra("index", index);
            startActivity(intent);
        }

    }

    private void initUI(int position){
        this.position = position;
        viewPager = (ViewPager)findViewById(R.id.pager);
        adapter = new HomePagerViewAdpater(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setupTab(){
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Orders"));
        tabLayout.getTabAt(0).setIcon(R.drawable.selector_list);
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.getTabAt(1).setIcon(R.drawable.selector_map);
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.getTabAt(2).setIcon(R.drawable.selector_history);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(position).select();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0){
                    setParams();
                    new GetOrdersAsyncTask(HomeActivity.this, getParams()).execute();
                } else if (tab.getPosition() == 1){

                } else if (tab.getPosition() == 2){
                    setHistoryParams();
                    new GetHistoryAsyncTask(HomeActivity.this, getHistoryParams()).execute();

                }
                index = -1;
                if (!historyStack.isEmpty()){
                    historyStack.pop().onBack();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2 && !historyStack.isEmpty()){
                    historyStack.pop().onBack();
                }
            }
        });
    }

    public void setParams(){
        params = new JSONObject();
        try {
            params.put(Common.DRIVER_NAME, apps.preference.getDriverName());
            params.put(Common.DRIVER_LOCATION_ADDRESS, apps.preference.getAddress());
            params.put(Common.DRIVER_LOCATION_LATITUDE, apps.preference.getLatitude());
            params.put(Common.DRIVER_LOCATION_LONGITUDE, apps.preference.getLongitude());
            params.put(Common.DRIVER_CAR_TYPE, apps.preference.getDriverCarType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getParams(){
        return params;
    }


    public void setParams1(){
        params1 = new JSONObject();
        try {
            params1.put(Common.DRIVER_ID, apps.preference.getDriverId());
            params1.put(Common.ORDER_ID, DataManagement.getInstance().getOrderInfo(index).getOrder_id());
            params1.put(Common.ORDER_STATE, orderState);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getParams1(){
        return params1;
    }

    public void setHistoryParams(){
        historyParams = new JSONObject();
        try{
            historyParams.put(Common.DRIVER_ID, apps.preference.getDriverId());
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public JSONObject getHistoryParams(){
        return historyParams;
    }

    public void getOrdersSuccess(JSONObject object){
        fetchOrderInfos(object);
        initUI(0);
    }

    public void getOrdersFail(){
//        showMsg(getString(R.string.connection_error));
        initUI(0);
    }

    private void fetchOrderInfos(JSONObject object){
        JSONArray array = null;
        try {
            if (!object.isNull(Common.ORDERS)) {
                array = object.getJSONArray(Common.ORDERS);
                DataManagement.getInstance().setOrderInfos(array);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setParams();
        new GetOrdersAsyncTask(HomeActivity.this, getParams()).execute();
    }

    public void goTakeOrderActivity(int index){
        Intent intent = new Intent(HomeActivity.this, TakeOrderActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    public void orderStateChangeSuccess(JSONObject object) {
        try {
            SalikLog.Error("OrderState---->"+String.valueOf(object.getInt(Common.STATUS)));
            if (object.getInt(Common.CURRENT_ORDER_STATE) != 1){
                showMsg("Order is taken error!");
            } else{
                goTakeOrderActivity(index);
                index = -1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void orderStateChangeFail(){
//        showMsg(getString(R.string.connection_error));
    }

    public void getHistorySuccess(JSONObject object){
        fetchHistoryInfo(object);
        initUI(2);
    }

    public void getHistoryFail(){
//        showMsg(getString(R.string.connection_error));

    }

    public void fetchHistoryInfo(JSONObject object){
        JSONArray array = null;
        DataManagement.getInstance().removeAllHistory();
        try {
            if (object.getInt(Common.STATUS)==1) {
                array = object.getJSONArray(Common.ORDER_HISTORY);
                DataManagement.getInstance().setHistoryInfos(array);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnBackKeyPressedListener {
        void onBack();
    }

    public Stack<OnBackKeyPressedListener> historyStack = new Stack<>();

    public void pushHistoryStack(OnBackKeyPressedListener listener){
        historyStack.push(listener);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 2 && !historyStack.isEmpty()){
            historyStack.pop().onBack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //Runtime Permission Callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    initUI(0);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            //other case
        }
    }

}
