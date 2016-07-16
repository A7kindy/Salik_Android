package com.me.salik.location;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by MAC on 7/4/16.
 */
public class CheckConnection {

    private static CheckConnection	mCheckConnection;

    /**
     * @param context
     *           of the class calling this method
     * @return instance of this class This method is the global point of access for getting the only one instance of this
     *         class
     */
    public static CheckConnection getInstance() {
        if (mCheckConnection == null) {
            mCheckConnection = new CheckConnection();
        }
        return mCheckConnection;
    }

    public boolean isConnection(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
	/*
	 * public void showConnectionError(Activity mActivity){ Toast toast=new Toast(mActivity);
	 * toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); toast.makeText(mActivity,
	 * "Connection error",Toast.LENGTH_SHORT).show(); } public void showNotUploadError(Activity mActivity){ Toast
	 * toast=new Toast(mActivity); toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0); toast.makeText(mActivity,
	 * "Challenge upload pending",Toast.LENGTH_SHORT).show(); }
	 */

}
