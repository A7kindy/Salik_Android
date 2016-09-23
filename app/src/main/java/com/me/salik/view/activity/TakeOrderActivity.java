package com.me.salik.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;
import com.me.salik.modal.DataManagement;
import com.me.salik.server.asyncTask.OrderChangeAsyncTask;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class TakeOrderActivity extends BaseActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 0;
    TextView number;
    TextView location;
    TextView comment;

    JSONObject params;

    int index = 0;
    int orderState = 1;
    int order_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            index = bundle.getInt("index", 0);
            order_id = DataManagement.getInstance().getOrderInfo(index).getOrder_id();
        }
        initUI();
    }

    private void initUI(){
        number = (TextView)findViewById(R.id.number);
        number.setText(DataManagement.getInstance().getOrderInfo(index).getOrder_phone_number());
        location = (TextView)findViewById(R.id.location);
        location.setText(DataManagement.getInstance().getOrderInfo(index).getOrder_location_address());
        comment = (TextView)findViewById(R.id.comment);
        comment.setText(DataManagement.getInstance().getOrderInfo(index).getOrder_comment());

        (findViewById(R.id.open_map)).setOnClickListener(this);
        (findViewById(R.id.call)).setOnClickListener(this);
        (findViewById(R.id.complete)).setOnClickListener(this);
        (findViewById(R.id.cancel)).setOnClickListener(this);

        params = new JSONObject();
    }

    private void setParams(int state){
        try {
            params.put(Common.DRIVER_ID, apps.preference.getDriverId());
            params.put(Common.ORDER_ID, order_id);
            params.put(Common.ORDER_STATE, state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_map:
                showMapActivity();
                break;
            case R.id.call:
                showCallActivity();
                break;
            case R.id.complete:
                orderState = 3;
                setParams(orderState);
                showConfirmMessage(getString(R.string.order_complete), getString(R.string.are_you_sure));
                break;
            case R.id.cancel:
                orderState = 1;
                setParams(orderState);
                showConfirmMessage(getString(R.string.order_cancel), getString(R.string.are_you_sure));
                break;
            default:
                break;
        }
    }

    public void showConfirmMessage(String title, String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new OrderChangeAsyncTask(TakeOrderActivity.this, params).execute();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void showCallActivity(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CALL_PHONE);
        }
        else{
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + DataManagement.getInstance().getOrderInfo(index).getOrder_phone_number()));
            startActivity(intent);
        }
    }

    public void showMapActivity(){
//        Intent intent = new Intent(TakeOrderActivity.this, OpenMapActivity.class);
//        intent.putExtra("index", index);
//        startActivity(intent);

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+DataManagement.getInstance().getOrderInfo(index).getOrder_location_latitude()+","+ DataManagement.getInstance().getOrderInfo(index).getOrder_location_longitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void orderStateChangeSuccess(JSONObject object) {
        try {
            SalikLog.Error("OrderState---->"+String.valueOf(object.getInt(Common.STATUS)));
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void orderStateChangeFail(){
        showMsg(getString(R.string.connection_error));
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //if permission is granted

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
