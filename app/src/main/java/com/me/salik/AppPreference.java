package com.me.salik;

import android.content.Context;
import android.content.SharedPreferences;

import com.me.salik.common.Common;

/**
 * Created by MAC on 6/30/16.
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

    public void setDriverId(int driverId){
        editor.putInt(Common.DRIVER_ID, driverId);
        editor.commit();
    }

    public int getDriverId(){
        return sharedPreferences.getInt(Common.DRIVER_ID, 0);
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
        editor.putString(Common.DRIVER_LOCATION_ADDRESS, address);
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

    public void setDriverCarId(int driverCarId){
        editor.putInt(Common.DRIVER_CAR_ID, driverCarId);
        editor.commit();
    }

    public int getDriverCarId(){
        return sharedPreferences.getInt(Common.DRIVER_CAR_ID, 0);
    }

    public void setDriverCarType(String driverCarType){
        editor.putString(Common.DRIVER_CAR_TYPE, driverCarType);
        editor.commit();
    }

    public String getDriverCarType(){
        return sharedPreferences.getString(Common.DRIVER_CAR_TYPE, "");
    }

}
