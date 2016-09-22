package com.me.salik.modal;

import lombok.Data;

/**
 * Created by MAC on 7/2/16.
 */
@Data
public class HistoryInfo {
    public HistoryInfo(){

    }

    int order_id;
    String order_phone_number;
    String order_location_address;
    String order_completed_time;

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

    public String getOrder_location_address() {
        return order_location_address;
    }

    public void setOrder_location_address(String order_location_address) {
        this.order_location_address = order_location_address;
    }

    public String getOrder_completed_time() {
        return order_completed_time;
    }

    public void setOrder_completed_time(String order_completed_time) {
        this.order_completed_time = order_completed_time;
    }
}
