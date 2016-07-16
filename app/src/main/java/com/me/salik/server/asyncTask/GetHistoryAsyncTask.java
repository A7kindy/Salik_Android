package com.me.salik.server.asyncTask;

import android.os.AsyncTask;

import com.me.salik.common.Common;
import com.me.salik.server.ServerHandler;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.base.BaseActivity;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MAC on 7/2/16.
 */
public class GetHistoryAsyncTask extends AsyncTask<Void, Void, JSONObject> {

    BaseActivity activity;
    JSONObject params;

    public GetHistoryAsyncTask(BaseActivity activity, JSONObject params){
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
            result = ServerHandler.getInstance().HttpPost(new URL(Common.URL_GET_HISTORY), params);

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
            if (activity instanceof HomeActivity){
                ((HomeActivity)activity).getHistorySuccess(object);
            }
        } else {
            if (activity instanceof HomeActivity){
                ((HomeActivity)activity).getHistoryFail();
            }
        }

    }
}
