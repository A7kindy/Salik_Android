package com.me.salik.modal;

import com.me.salik.common.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MAC on 6/15/16.
 */
public class DataManagement {

    public static DataManagement instance;

    public static synchronized DataManagement getInstance(){

        if (instance == null){
            instance = new DataManagement();
        }
        return instance;
    }

    public ArrayList<OrderInfo> orderInfos;

    public DataManagement(){
        orderInfos = new ArrayList<>();
    }

    public void setOrderInfos(JSONArray array){
        orderInfos = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++){
            OrderInfo orderInfo = new OrderInfo();
            try {
                JSONObject order = array.getJSONObject(i);
                orderInfo.setOrder_id(order.getInt(Common.ORDER_ID));
                orderInfo.setOrder_phone_number(order.getString(Common.ORDER_PHONE_NUMBER));
                orderInfo.setOrder_comment(order.getString(Common.ORDER_COMMENT));
                orderInfo.setOrder_car_id(order.getInt(Common.ORDER_CAR_ID));
                orderInfo.setOrder_location_address(order.getString(Common.ORDER_LOCATION_ADDRESS));
                orderInfo.setOrder_location_latitude(order.getDouble(Common.ORDER_LOCATION_LATITUDE));
                orderInfo.setOrder_location_longitude(order.getDouble(Common.ORDER_LOCATION_LONGITUDE));
                orderInfo.setOrder_distance(order.getDouble(Common.DISTANCE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            orderInfos.add(orderInfo);
        }
    }

    public ArrayList<OrderInfo> getOrderInfos(){
        return orderInfos;
    }

    public OrderInfo getOrderInfo(int index){
        return orderInfos.get(index);
    }

    public int getOrdersCount(){
        return orderInfos.size();
    }


}
