package com.me.salik.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.gcm.GCMIntentService;
import com.me.salik.modal.Car;
import com.me.salik.modal.DataManagement;
import com.me.salik.modal.OrderInfo;
import com.me.salik.view.base.BaseActivity;

public class CustomerInfoActivity extends BaseActivity implements View.OnClickListener {

    int id = 0;
    OrderInfo orderInfo = new OrderInfo();
    private static String[] car = {Car.ECONOMY.toString(), Car.SUV.toString(), Car.VIP.toString(), Car.VVIP.toString(), Car.DELIVERY_SERVICE.toString()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id", 0);
            orderInfo = DataManagement.getInstance().getOrderInfo(id);
        }

        initData();
        initUI();
    }

    public void initData() {

    }

    public void initUI() {
        ((TextView) findViewById(R.id.phone)).setText(orderInfo.getOrder_phone_number());
        ((TextView) findViewById(R.id.customer_id)).setText(String.valueOf(orderInfo.getOrder_id()));
        ((TextView) findViewById(R.id.location)).setText(orderInfo.getOrder_location_address());
        ((TextView) findViewById(R.id.comment)).setText(orderInfo.getOrder_comment());
        ((TextView) findViewById(R.id.distance)).setText(String.valueOf((int) orderInfo.getOrder_distance()));
        ((TextView) findViewById(R.id.car)).setText(car[orderInfo.getOrder_car_id() - 1]);
        findViewById(R.id.call).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            finish();
        } else if (v.getId() == R.id.call) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + GCMIntentService.phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
    }
}
