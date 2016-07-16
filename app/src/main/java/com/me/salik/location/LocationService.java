package com.me.salik.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;

import com.me.salik.AppPreference;

/**
 * Created by MAC on 7/4/16.
 */
public class LocationService extends Service {
    private Location location;

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        final double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'd') {
            dist = dist * 1609.344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final AppPreference appPrefs = AppPreference.getSalikSharedPrefrence(getApplicationContext());
        if (intent != null) {
            location = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        }
        if (location != null) {
            appPrefs.setLatitude(location.getLatitude());
            appPrefs.setLongitude(location.getLongitude());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}

