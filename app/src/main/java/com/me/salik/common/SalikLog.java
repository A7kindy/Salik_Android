package com.me.salik.common;

import android.util.Log;

/**
 * Created by MAC on 6/13/16.
 */
public class SalikLog {
    private static boolean Debug = true;

    public static void Info(String msg){
        if (Debug){
            Log.i(Common.TAG, msg);
        }
    }
    public static void Error(String msg){
        if(Debug == true){
            Log.e(Common.TAG, msg);
        }
    }

}
