package com.me.salik.view.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.me.salik.R;
import com.me.salik.asyncTask.LogInAsyncTask;
import com.me.salik.common.Common;
import com.me.salik.common.Utils;
import com.me.salik.modal.DriverInfo;
import com.me.salik.service.GPSTracker;
import com.me.salik.service.LocationAddress;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    Button login;
    EditText user_name;
    EditText password;

    GPSTracker gpsTracker;

    DriverInfo driverInfo;
    JSONObject params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        gpsTracker = new GPSTracker(this);
        driverInfo = DriverInfo.getInstance();

        params = new JSONObject();

        initUI(findViewById(R.id.parent));
        getLocation();
    }

    public void initUI(View view){
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this);

        user_name = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.password);

        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                initUI(innerView);
            }
        }

    }

    public void getLocation(){
        if (gpsTracker.canGetLocation()){

            apps.preference.setLatitude(gpsTracker.getLatitude());
            apps.preference.setLongitude(gpsTracker.getLongitude());

            getAddress();
        } else{
            gpsTracker.showSettingsAlert();
        }

//        apps.preference.setLatitude(25.26987);
//        apps.preference.setLongitude(51.541984);
    }

    public void getAddress(){

        LocationAddress.getAddressFromLocation(apps.preference.getLatitude(), apps.preference.getLongitude(),
                getApplicationContext(), new GeocoderHandler());

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Toast.makeText(LoginActivity.this, locationAddress, Toast.LENGTH_SHORT).show();
            apps.preference.setAddress(locationAddress);
//            apps.preference.setAddress("Doha");
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login){
            login();
        }
    }

    public void login(){
        if (Utils.isEmpty(user_name)){
            showMsg("Please enter user name");
        } else if (Utils.isEmpty(password)){
            showMsg("Please enter password");
        } else {

            apps.preference.setDriverName(user_name.getText().toString());
            apps.preference.setDriverPassword(password.getText().toString());

            setParams();

            new LogInAsyncTask(this, params).execute();
        }

    }

    public void setParams(){
        try {
        params.put(Common.DRIVER_NAME, apps.preference.getDriverName());
        params.put(Common.DRIVER_PASSWORD, apps.preference.getDriverPassword());
        params.put(Common.DRIVER_LOCATION_ADDRESS, apps.preference.getAddress());
        params.put(Common.DRIVER_LOCATION_LATITUDE, apps.preference.getLatitude());
        params.put(Common.DRIVER_LOCATION_LONGITUDE, apps.preference.getLongitude());
        params.put(Common.DRIVER_GCM_ID, apps.preference.getGCMId());
//                params.put(Common.DRIVER_LOCATION_ADDRESS, "Doha");
//                params.put(Common.DRIVER_LOCATION_LATITUDE, 25.16987);
//                params.put(Common.DRIVER_LOCATION_LONGITUDE, 51.541984);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loginSuccess(JSONObject object){
        fetchInfo(object);
    }

    public void loginFail(){
        showMsg(getString(R.string.connection_error));
    }

     private void fetchInfo(JSONObject object){

         try {
             int status = object.getInt(Common.STATUS);
             if (status == 1){
                 goHome();
             } else if (status == 2){
                 showMsg("You don't registered!");
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }

    private void goHome(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

