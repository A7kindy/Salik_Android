package com.me.salik.server.asyncTask;

import android.os.AsyncTask;

import com.me.salik.common.Common;
import com.me.salik.server.ServerHandler;
import com.me.salik.view.base.BaseActivity;
import com.me.salik.view.base.BaseFragment;
import com.me.salik.view.fragment.OrdersHistoryFragment;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MAC on 7/4/16.
 */
public class ClearHistoryAsyncTask extends AsyncTask<Void, Void, JSONObject> {

    BaseActivity activity;
    BaseFragment fragment;
    JSONObject params;

    public ClearHistoryAsyncTask(BaseActivity activity, BaseFragment fragment, JSONObject params){
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
    protected JSONObject doInBackground(Void... args) {
        JSONObject result = null;
        try {
            result = ServerHandler.getInstance().HttpPost(new URL(Common.URL_CLEAR_HISTORY), params);

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
            if (fragment instanceof OrdersHistoryFragment){
                ((OrdersHistoryFragment)fragment).clearHistorySuccess(jsonObject);
            }
        } else {
            if (fragment instanceof OrdersHistoryFragment){
                ((OrdersHistoryFragment)fragment).clearHistoyUnsuccess();
            }
        }
    }
}
