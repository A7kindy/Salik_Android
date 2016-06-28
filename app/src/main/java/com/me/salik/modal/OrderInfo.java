package com.me.salik.modal;

import lombok.Data;

/**
 * Created by MAC on 6/13/16.
 */
@Data
public class OrderInfo {

    public OrderInfo(){

    }

    int order_id;
    String order_phone_number;
    String order_comment;
    String order_location_address;
    double order_location_latitude;
    double order_location_longitude;
    int order_car_id;
    double order_distance;

}
