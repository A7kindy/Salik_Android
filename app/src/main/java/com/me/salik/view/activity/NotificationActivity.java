package com.me.salik.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.gcm.GCMIntentService;
import com.me.salik.modal.Car;
import com.me.salik.view.base.BaseActivity;

public class NotificationActivity extends BaseActivity implements View.OnClickListener{

    private static String []car = {Car.ECONOMY.toString(), Car.SUV.toString(), Car.VIP.toString(), Car.VVIP.toString()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initUI();
    }

    public void initUI(){
        ((TextView) findViewById(R.id.phone)).setText(GCMIntentService.phone);
        ((TextView) findViewById(R.id.location)).setText(GCMIntentService.location);
        ((TextView) findViewById(R.id.comment)).setText(GCMIntentService.msg);
        ((TextView) findViewById(R.id.car)).setText(car[GCMIntentService.car_id]);


        findViewById(R.id.accept).setOnClickListener(this);
        findViewById(R.id.reject).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.accept){
            finish();
        } else if (v.getId() == R.id.reject){
            finish();
        }
    }
}
