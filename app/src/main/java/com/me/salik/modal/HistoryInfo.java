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
}
