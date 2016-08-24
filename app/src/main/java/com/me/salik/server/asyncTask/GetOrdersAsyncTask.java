package com.me.salik.server.asyncTask;

import android.os.AsyncTask;

import com.me.salik.common.Common;
import com.me.salik.server.ServerHandler;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.base.BaseActivity;
import com.me.salik.view.base.BaseFragment;
import com.me.salik.view.fragment.OrdersFragment;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MAC on 6/30/16.
 */
public class GetOrdersAsyncTask extends AsyncTask<Void, Void, JSONObject> {
    BaseActivity activity;
    BaseFragment fragment = null;
    JSONObject params;

    public GetOrdersAsyncTask(BaseActivity activity, JSONObject params){
        this.activity = activity;
        this.params = params;
    }

    public GetOrdersAsyncTask(BaseActivity activity,BaseFragment fragment, JSONObject params){
        this.activity = activity;
        this.fragment = fragment;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (fragment == null) {
            activity.showLoadingProgressDialog();
        } else {
            fragment.showLoadingProgressDialog();
        }
    }

    @Override
    protected JSONObject doInBackground(Void... arg) {
        JSONObject result = null;
        try {
            result = ServerHandler.getInstance().HttpPost(new URL(Common.URL_GET_ORDERS), params);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        super.onPostExecute(object);
        if (fragment == null) {
            activity.dismissProgress();
        } else {
            fragment.dismissProgress();
        }
        if (object != null){
            if (activity instanceof HomeActivity && fragment == null){
                ((HomeActivity)activity).getOrdersSuccess(object);
            } else if (activity instanceof HomeActivity && fragment instanceof OrdersFragment){
                ((OrdersFragment) fragment).getOrdersSuccess(object);
            }
        } else {
            if (activity instanceof HomeActivity && fragment == null){
                ((HomeActivity)activity).getOrdersFail();
            } else if (activity instanceof HomeActivity && fragment instanceof OrdersFragment){
                ((OrdersFragment) fragment).getOrdersFail();
            }
        }

    }
}
