package com.me.salik.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.me.salik.common.Common;

/**
 * Created by MAC on 6/23/16.
 */
public class AppPreference {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static AppPreference	salikSharedPrefrence;

    public static synchronized AppPreference getSalikSharedPrefrence(Context context){
        if (salikSharedPrefrence == null){
            salikSharedPrefrence = new AppPreference(context);
        }

        return salikSharedPrefrence;
    }

    public AppPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(Common.APP_PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void clearEditor() {
        editor.clear();
        editor.commit();
    }

    public void commitEditor() {
        editor.commit();
    }

    public void setDriverName(String driverNamee){
        editor.putString(Common.DRIVER_NAME, driverNamee);
        editor.commit();
    }

    public String getDriverName(){
        return sharedPreferences.getString(Common.DRIVER_NAME, "");
    }

    public void setDriverPassword(String password){
        editor.putString(Common.DRIVER_PASSWORD, password);
        editor.commit();
    }

    public String getDriverPassword(){
        return sharedPreferences.getString(Common.DRIVER_PASSWORD, "");
    }

    public void setAddress(String address){
        editor.putString(Common.DRIVER_LOCATION_ADDRESS, "");
        editor.commit();
    }

    public String getAddress(){
        return sharedPreferences.getString(Common.DRIVER_LOCATION_ADDRESS, "");
    }

    public void setLatitude(Double latitude){
        editor.putString(Common.DRIVER_LOCATION_LATITUDE, String.valueOf(latitude));
        editor.commit();
    }

    public Double getLatitude(){
        return Double.valueOf(sharedPreferences.getString(Common.DRIVER_LOCATION_LATITUDE, "0.0"));
    }

    public void setLongitude(Double longitude){
        editor.putString(Common.DRIVER_LOCATION_LONGITUDE, String.valueOf(longitude));
        editor.commit();
    }

    public Double getLongitude(){
        return Double.valueOf(sharedPreferences.getString(Common.DRIVER_LOCATION_LONGITUDE, "0.0"));
    }

    public void setGCMId(String gcmId){
        editor.putString(Common.DRIVER_GCM_ID, gcmId);
        editor.commit();
    }

    public String getGCMId(){
        return sharedPreferences.getString(Common.DRIVER_GCM_ID, "");
    }

}
