package com.me.salik.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.me.salik.R;
import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.modal.DriverInfo;
import com.me.salik.view.base.BaseActivity;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity {

    private static final int SPLASH_TIME_OUT = 3000;

    GoogleCloudMessaging gcm;
    String regID;
    public static String gcmid = "";

    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (apps.preference.getGCMId().equals("")){
            getRegID();
        }

        //get hash key
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.yougoz.yougoz",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (checkPlayServices()) {
            Toast.makeText(this, "Google Play Service Supported.",  Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {

                    Intent intent;//
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RECOVER_PLAY_SERVICES:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Google Play Services must be installed.",  Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getRegID(){
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                String msg="";
                try {
                    if(gcm==null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regID=gcm.register(Common.GCM_SENDER_ID);
                    msg="Device registered, registration ID="+regID;
                    SalikLog.Error("GCM !!!!!"+ regID);

                } catch(IOException ex) {
                    msg="Error: "+ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                if(regID .equals("")){
                    gcmid = "";
                }
                gcmid = regID;
                apps.preference.setGCMId(gcmid);
            }

        }.execute();
    }

    private boolean checkPlayServices() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                showErrorDialog(status);
            } else {
                Toast.makeText(this, "Google Play Service is not supported.",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    void showErrorDialog(int code) {
        GooglePlayServicesUtil.getErrorDialog(code, this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
    }

}
