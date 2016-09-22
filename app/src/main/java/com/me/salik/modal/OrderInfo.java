package com.me.salik.modal;

import lombok.Data;

/**
 * Created by MAC on 6/30/16.
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
    String order_car_type;
    double order_distance;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_phone_number() {
        return order_phone_number;
    }

    public void setOrder_phone_number(String order_phone_number) {
        this.order_phone_number = order_phone_number;
    }

    public String getOrder_comment() {
        return order_comment;
    }

    public void setOrder_comment(String order_comment) {
        this.order_comment = order_comment;
    }

    public String getOrder_location_address() {
        return order_location_address;
    }

    public void setOrder_location_address(String order_location_address) {
        this.order_location_address = order_location_address;
    }

    public double getOrder_location_latitude() {
        return order_location_latitude;
    }

    public void setOrder_location_latitude(double order_location_latitude) {
        this.order_location_latitude = order_location_latitude;
    }

    public double getOrder_location_longitude() {
        return order_location_longitude;
    }

    public void setOrder_location_longitude(double order_location_longitude) {
        this.order_location_longitude = order_location_longitude;
    }

    public String getOrder_car_type() {
        return order_car_type;
    }

    public void setOrder_car_type(String order_car_type) {
        this.order_car_type = order_car_type;
    }

    public double getOrder_distance() {
        return order_distance;
    }

    public void setOrder_distance(double order_distance) {
        this.order_distance = order_distance;
    }
}
