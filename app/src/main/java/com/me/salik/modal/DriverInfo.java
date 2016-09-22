package com.me.salik.modal;

import lombok.Data;

/**
 * Created by MAC on 6/30/16.
 */
@Data
public class DriverInfo {

    public static DriverInfo instance;

    public static synchronized DriverInfo getInstance(){
        if (instance == null){
            instance = new DriverInfo();
        }
        return instance;
    }

    public DriverInfo(){

    }

    String driver_name;
    String driver_password;
    String driver_location_address;
    double driver_location_latitude;
    double driver_location_longitude;
    String driver_gcm_id;

    public static void setInstance(DriverInfo instance) {
        DriverInfo.instance = instance;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_password() {
        return driver_password;
    }

    public void setDriver_password(String driver_password) {
        this.driver_password = driver_password;
    }

    public String getDriver_location_address() {
        return driver_location_address;
    }

    public void setDriver_location_address(String driver_location_address) {
        this.driver_location_address = driver_location_address;
    }

    public double getDriver_location_latitude() {
        return driver_location_latitude;
    }

    public void setDriver_location_latitude(double driver_location_latitude) {
        this.driver_location_latitude = driver_location_latitude;
    }

    public double getDriver_location_longitude() {
        return driver_location_longitude;
    }

    public void setDriver_location_longitude(double driver_location_longitude) {
        this.driver_location_longitude = driver_location_longitude;
    }

    public String getDriver_gcm_id() {
        return driver_gcm_id;
    }

    public void setDriver_gcm_id(String driver_gcm_id) {
        this.driver_gcm_id = driver_gcm_id;
    }
}
