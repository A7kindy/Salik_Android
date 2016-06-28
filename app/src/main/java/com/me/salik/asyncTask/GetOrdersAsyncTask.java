package com.me.salik.asyncTask;

import android.os.AsyncTask;

import com.me.salik.api.ServerHandler;
import com.me.salik.common.Common;
import com.me.salik.view.activity.MainActivity;
import com.me.salik.view.base.BaseActivity;
import com.me.salik.view.base.BaseFragment;
import com.me.salik.view.fragment.CustomerFragment;
import com.me.salik.view.fragment.CustomerListFragment;
import com.me.salik.view.fragment.CustomerMapFragment;
import com.me.salik.view.fragment.HomeFragment;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MAC on 6/22/16.
 */
public class GetOrdersAsyncTask extends AsyncTask<Void, Void, JSONObject> {

    BaseActivity activity;
    BaseFragment fragment;
    JSONObject params;

    public GetOrdersAsyncTask(BaseActivity activity, BaseFragment fragment, JSONObject params){
        this.activity = activity;
        this.fragment = fragment;
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
            result = ServerHandler.getInstance().HttpPost(new URL(Common.URL_GET_ORDERS), params);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        super.onPostExecute(object);
        activity.dismissProgress();
        if (object != null){
            if (activity instanceof MainActivity && fragment instanceof HomeFragment){
                ((HomeFragment)fragment).getOrdersSuccess(object);
            } else if (activity instanceof MainActivity && fragment instanceof CustomerFragment){
                ((CustomerFragment)fragment).getOrdersSuccess(object);
            }
        } else {
            if (activity instanceof MainActivity && fragment instanceof HomeFragment){
                ((HomeFragment)fragment).getOrdersFail();
            } else if (activity instanceof MainActivity && fragment instanceof CustomerFragment){
                ((CustomerFragment)fragment).getOrdersFail();
            }
        }

    }
}
