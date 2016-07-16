package com.me.salik.common;

/**
 * Created by MAC on 6/30/16.
 */
public class Common {

    public static final String TAG = "Salik";
    public static final String APP_PREFERENCE = "salikPrefernec";

    public static final boolean DEVELOPER_MODE = false;

    //Device Info
    public static int deviceWidth = 0;
    public static int deviceHeight = 0;

    //Server Info
    public final static String API_KEY = "12345";
//    public final static String SERVER_URL = "http://172.16.1.192:8080/Salik/index.php";
//    public final static String SERVER_URL = "http://138.128.178.90/~oczxbfkm/Salik/index.php";
public final static String SERVER_URL = "http://salikappqatar.com/Salik/index.php";

    public final static String URL_LOGIN = SERVER_URL + "/api/login";
    public final static String URL_GET_ORDERS = SERVER_URL + "/api/getOrders";
    public final static String URL_CHANGE_ORDER_STATE = SERVER_URL + "/api/orderStateChange";
    public final static String URL_GET_HISTORY = SERVER_URL + "/api/getHistory";
    public final static String URL_CLEAR_HISTORY = SERVER_URL + "/api/clearHistory";

    //Response
    public final static String STATUS = "status";
    public final static String MSG = "msg";

    //Order Info    public final static String DRIVER_ID = "driver_id";

    public final static String ORDERS = "orders";
    public final static String ORDER_ID = "order_id";
    public final static String ORDER_PHONE_NUMBER = "order_phone_number";
    public final static String ORDER_COMMENT = "order_comment";
    public final static String ORDER_LOCATION_ADDRESS = "order_location_address";
    public final static String ORDER_LOCATION_LATITUDE = "order_location_latitude";
    public final static String ORDER_LOCATION_LONGITUDE = "order_location_longitude";
    public final static String ORDER_CAR_TYPE = "order_car_type";
    public final static String DISTANCE = "distance";
    public final static String ORDER_STATE = "order_state";
    public final static String ORDER_COMPLETED_TIME = "order_completed_time";
    public final static String ORDER_HISTORY = "order_history";

    //Driver Info
    public final static String DRIVER_ID = "driver_id";
    public final static String DRIVER_NAME = "driver_name";
    public final static String DRIVER_PASSWORD = "driver_password";
    public final static String DRIVER_LOCATION_ADDRESS = "driver_location_address";
    public final static String DRIVER_LOCATION_LATITUDE = "driver_location_latitude";
    public final static String DRIVER_LOCATION_LONGITUDE = "driver_location_longitude";
    public final static String DRIVER_CAR_ID = "driver_car_id";
    public final static String DRIVER_CAR_TYPE = "driver_car_type";

    public static final int		LOCATION_INTERVAL								= 10000;

}
