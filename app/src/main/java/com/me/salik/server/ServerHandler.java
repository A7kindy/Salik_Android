package com.me.salik.server;

import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MAC on 6/30/16.
 */
public class ServerHandler {
    public static ServerHandler instance;

    public static synchronized ServerHandler getInstance(){
        if (instance == null){
            instance = new ServerHandler();
        }

        return instance;
    }

    int httpResult;
    JSONObject response = null;
    StringBuilder sb ;


    HttpURLConnection connection;
    DataOutputStream outputStream;

    public ServerHandler(){
        sb = new StringBuilder();
    }

    public JSONObject HttpPost(URL url, JSONObject params){

        SalikLog.Info(url.toString());

        try {
            connection = (HttpURLConnection)url.openConnection();
//            connection.setDoInput(true);
            connection.setDoOutput(true);
//            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.setRequestProperty("api-key", Common.API_KEY);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(params.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            httpResult = connection.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_OK){
                sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String line ;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                SalikLog.Info("SB:"+sb.toString());

                try {
                    response = new JSONObject(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else {
                System.out.println(connection.getResponseMessage());

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(connection!=null)
                connection.disconnect();
        }
        return response;
    }
}
