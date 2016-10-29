package com.me.salik.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.me.salik.R;
import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.common.Utils;
import com.me.salik.location.AppLocation;
import com.me.salik.location.LocationService;
import com.me.salik.modal.DriverInfo;
import com.me.salik.server.asyncTask.LogInAsyncTask;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    Button login;
    EditText user_name;
    EditText password;

    DriverInfo driverInfo;
    JSONObject params;
    private AppLocation mAppLocation;

    Intent  locationServiceIntent;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private LocationManager mLocationManager;

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

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check for permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = apps.preference.getDriverName();
        String password = apps.preference.getDriverPassword();
        Intent intent = getIntent();
        String previousActivity = intent.getStringExtra("activity");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("autologinCheck","granted");
            if (previousActivity != null) {
                if (username != "" && password != "") {
                    if (username != null && password != null && previousActivity.equals("splash")) {
                        setParams();
                        new LogInAsyncTask(LoginActivity.this, params).execute();
                    }
                }
            }
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
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
//        stopService(locationServiceIntent);
//        mAppLocation.stopServices();
        finish();

    }

    //Permission RequestCallback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted,
                    if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        new AppLocation(this).showSettingsAlert();
                    } else {
                        locationServiceIntent = new Intent(this, LocationService.class);
                        mAppLocation = AppLocation.getInstance(this, this, locationServiceIntent);
                        mAppLocation.startServices(this, this);
                    }


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
