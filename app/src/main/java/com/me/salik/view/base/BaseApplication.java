package com.me.salik.view.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.me.salik.common.Common;
import com.me.salik.preference.AppPreference;

/**
 * Created by MAC on 6/13/16.
 */
public class BaseApplication extends Application {

    public AppPreference preference;
    public BaseApplication(){

    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("unused")

    @Override
    public void onCreate(){
        if (Common.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();


        init();
    }

    public void init(){
        getDeviceInfo(((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());
        preference = AppPreference.getSalikSharedPrefrence(getApplicationContext());
    }

    public void getDeviceInfo(Display display){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        Common.deviceHeight = displayMetrics.heightPixels;
        Common.deviceWidth = displayMetrics.widthPixels;
    }

    public boolean isConnectedInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        }
        return false;
    }


}
