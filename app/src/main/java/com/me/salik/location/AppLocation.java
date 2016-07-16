package com.me.salik.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.me.salik.AppPreference;
import com.me.salik.common.Common;
import com.me.salik.common.SalikLog;

/**
 * Created by MAC on 7/4/16.
 */
public class AppLocation implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static AppLocation	mAppLocationClass;

    /**
     * This method is used to get singleton instance of MyLocation class
     * @return {@link AppLocation}
     */
    public static AppLocation getInstance(Context context, Activity activity, Intent intent) {
        if (mAppLocationClass == null) {
            mAppLocationClass = new AppLocation(context, activity, intent);
        }
        return mAppLocationClass;
    }

    private Activity		mActivity;

    private Context			mContext;
    // Stores the current instantiation of the location client in this object
    private GoogleApiClient	mGoogleApiClient;
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    private Location currentLocation;

    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the method handleRequestSuccess of
     * LocationUpdateReceiver.
     */
    boolean					mUpdatesRequested	= false;

    AppPreference           appPrefs ;


    public AppLocation(Context context) {
        mContext = context;
        initializeLocationServices(context);
    }

    private AppLocation(Context context, Activity activity, Intent intent) {
        mActivity = activity;
        mContext = context;
        // mPendingIntent = PendingIntent.getService(activity, 1, intent, 0);
        appPrefs = AppPreference.getSalikSharedPrefrence(mActivity.getApplicationContext());
        initializeLocationServices(context);
    }

    private void customUpdatePrefrences(Location location) {
        if (location != null) {
            appPrefs.setLatitude(location.getLatitude());
            appPrefs.setLongitude(location.getLongitude());
            getAddress();
        }
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * Calculates the distance between two lat-longs.
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param unit
     * @return
     */
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

    /**
     * Invoked by the "Get Location" button. Calls getLastLocation() to get the current location
     *           The view object associated with this method, in this case a Button.
     */
    public Location getLocation() {
        // If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            //final Location currentLocation = mGoogleApiClient.getLastLocation();
            return currentLocation;
        }
        return null;
    }

    /**
     * This method is usedt o initialize locaton services
     * @return void
     */
    private void initializeLocationServices(Context context) {
        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();
		/*
		 * Set the update interval
		 */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use balanced accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Set minimum displacement
        mLocationRequest.setSmallestDisplacement(100);
		/*
		 * Create a new location client, using the enclosing class to handle callbacks.
		 */
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
//		mLocationClient = new LocationClient(context, this, this);

    }

    /**
     * /* Called by Location Services when the request to connect the client finishes successfully. At this point, you
     * can request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {

        Log.e("onConnected", "Calling");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(Common.LOCATION_INTERVAL); // Update location every second

//		mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        startUpdates();
        if (getLocation() != null) {
//            if (mActivity instanceof LocationSelectActivity) {
//                ((LocationSelectActivity) mActivity).setUpMapIfNeeded();
//            }
        }
        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }

    /**
     * Called by Location Services if the connection to the location client drops because of an error.
     */
//	@Override
//	public void onDisconnected() {
//		Log.e("onDisconnected", "okkkkkkkkkkkk");
//		stopUpdates();
//	}

    /**
     * Report location updates to the UI.
     *
     * @param location
     *           The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {
        // final Intent intent = new Intent();
        // intent.setAction("updatelocation");
        // LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        SalikLog.Error("On Location Changed called");
        SalikLog.Error(String.valueOf(appPrefs.getLatitude())+":::"+String.valueOf(appPrefs.getLongitude()));

        customUpdatePrefrences(location);
        currentLocation = location;
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {
        // Check that Google Play services is available
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            // Continue
            if (mGoogleApiClient.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            // Google Play services was not available for some reason
            // Display an error dialog
                final Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity, 70);
                if (dialog != null) {
                    dialog.show();
                }
            return false;
        }
    }

    /**
     * In response to a request to start updates, send a request to Location Services
     */
    private void startPeriodicUpdates() {
        // mLocationClient.requestLocationUpdates(mLocationRequest,
        // mPendingIntent);
//		mGoogleApiClient.requestLocationUpdates(mLocationRequest, this);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public void startServices(Context context) {
        Log.e("Starting service", "-----ok");
        mContext = context;
		/*
		 * Connect the client. Don't re-start any requests here; instead, wait for onResume()
		 */
        if (!servicesConnected()) {
            mGoogleApiClient.connect();
        } else {
            if (getLocation() != null) {

            }
        }
    }

    public void startServices(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;

        Log.e("startServices", "Calling");
		/*
		 * Connect the client. Don't re-start any requests here; instead, wait for onResume()
		 */
        if (!servicesConnected()) {
            mGoogleApiClient.connect();
        } else {
            if (getLocation() != null) {
//                if (mContext instanceof LocationSelectActivity) {
//                    ((LocationSelectActivity) mContext).setUpMapIfNeeded();
//                }

                appPrefs.setLatitude(getLocation().getLatitude());
                appPrefs.setLongitude(getLocation().getLongitude());
                getAddress();

            } else if (getLocation() == null) {
                stopServices();
                startServices(context, mActivity);
            }
        }
    }

    public void getAddress(){

        LocationAddress.getAddressFromLocation(appPrefs.getLatitude(), appPrefs.getLongitude(),
                mActivity.getApplicationContext(), new GeocoderHandler());

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            appPrefs.setAddress(locationAddress);
        }
    }

    /**
     * Invoked by the "Start Updates" button Sends a request to start location updates
     */
    public void startUpdates() {
        mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }

    /**
     * In response to a request to stop updates, send a request to Location Services
     */
    private void stopPeriodicUpdates() {
//		mLocationClient.removeLocationUpdates(this);
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    public void stopServices() {
        Log.e("stopping service", "-----ok");
        // If the client is connected
        if (mGoogleApiClient.isConnected()) {
            stopPeriodicUpdates();
        }
        // if (mPendingIntent != null) {
        // mPendingIntent.cancel();
        // }
        // After disconnect() is called, the client is considered "dead".
        mGoogleApiClient.disconnect();
    }

    /**
     * Invoked by the "Stop Updates" button Sends a request to remove location updates request them.
     */
    public void stopUpdates() {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
            Log.e("in stop update", "okkkkkkkkkkkk");
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        Log.e("onDisconnected", "okkkkkkkkkkkk");
        stopUpdates();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}

