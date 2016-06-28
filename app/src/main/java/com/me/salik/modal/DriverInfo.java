package com.me.salik.modal;

import lombok.Data;

/**
 * Created by MAC on 6/14/16.
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

}
