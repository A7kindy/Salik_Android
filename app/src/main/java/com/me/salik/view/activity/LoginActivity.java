package com.me.salik.view.activity;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.me.salik.R;
import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.common.Utils;
import com.me.salik.location.AppLocation;
import com.me.salik.location.GPSTracker;
import com.me.salik.location.LocationAddress;
import com.me.salik.location.LocationService;
import com.me.salik.modal.DriverInfo;
import com.me.salik.server.asyncTask.LogInAsyncTask;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends BaseActivity implements View.OnClickListener{

    Button login;
    EditText user_name;
    EditText password;

    DriverInfo driverInfo;
    JSONObject params;
    private AppLocation mAppLocation;

    Intent  locationServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        driverInfo = DriverInfo.getInstance();

        params = new JSONObject();

        initUI(findViewById(R.id.parent));
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

    @Override
    protected void onStart() {
        super.onStart();

        final LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AppLocation(this).showSettingsAlert();
        } else {
            Log.e("onResume", "on Resume MAIN is Fine");
            locationServiceIntent = new Intent(this, LocationService.class);
            mAppLocation = AppLocation.getInstance(this, this, locationServiceIntent);
            mAppLocation.startServices(this, this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
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
//                JSONObject object1 = object.getJSONObject(Common.DRIVER_CAR_ID);
                apps.preference.setDriverId(object.getInt(Common.DRIVER_ID));
                apps.preference.setDriverCarId(object.getInt(Common.DRIVER_CAR_ID));
                apps.preference.setDriverCarType(object.getString(Common.DRIVER_CAR_TYPE));
                SalikLog.Error("DriverID--->"+String.valueOf(apps.preference.getDriverId()));
                goHome();
            } else if (status == 2){
                showMsg("You don't registered!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void goHome(){
        Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
        startActivity(intent);
//        stopService(locationServiceIntent);
//        mAppLocation.stopServices();
        finish();

    }
}
