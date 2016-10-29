package com.me.salik.server.asyncTask;

import android.os.AsyncTask;

import com.me.salik.common.Common;
import com.me.salik.server.ServerHandler;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.activity.TakeOrderActivity;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MAC on 7/1/16.
 */
public class OrderChangeAsyncTask extends AsyncTask<Void, Void, JSONObject> {

    BaseActivity activity;

    JSONObject params;

    public OrderChangeAsyncTask(BaseActivity activity, JSONObject params){
        this.activity = activity;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showLoadingProgressDialog();
    }

    @Override
    protected JSONObject doInBackground(Void... arg) {
        JSONObject result = null;
        try {
            result = ServerHandler.getInstance().HttpPost(new URL(Common.URL_CHANGE_ORDER_STATE), params);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        activity.dismissProgress();
        if (jsonObject != null){
            //Store current Order in Preferences
            try {
                activity.apps.preference.setCurrentOrderId(params.getInt(Common.ORDER_ID));
            }catch(Exception e){

            }

            if (activity instanceof TakeOrderActivity) {
                ((TakeOrderActivity) activity).orderStateChangeSuccess(jsonObject);
            } else if (activity instanceof HomeActivity){
                ((HomeActivity) activity).orderStateChangeSuccess(jsonObject);
            }
        } else {
            if (activity instanceof TakeOrderActivity) {
                ((TakeOrderActivity) activity).orderStateChangeFail();
            } else if (activity instanceof HomeActivity){
                ((HomeActivity) activity).orderStateChangeFail();
            }
        }
    }
}
