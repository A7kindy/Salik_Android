package com.me.salik.asyncTask;

import android.os.AsyncTask;

import com.me.salik.api.ServerHandler;
import com.me.salik.common.Common;
import com.me.salik.view.activity.LoginActivity;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MAC on 6/22/16.
 */
public class LogInAsyncTask extends AsyncTask<Void, Void, JSONObject> {

    BaseActivity activity;
    JSONObject params;

    public LogInAsyncTask(BaseActivity activity, JSONObject params){
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
            result = ServerHandler.getInstance().HttpPost(new URL(Common.URL_LOGIN), params);

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
            ((LoginActivity)activity).loginSuccess(jsonObject);
        } else {
            ((LoginActivity)activity).loginFail();
        }
    }
}
