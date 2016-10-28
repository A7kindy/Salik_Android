package com.me.salik.view.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.me.salik.R;
import com.me.salik.location.GPSTracker;
import com.me.salik.modal.DataManagement;
import com.me.salik.server.asyncTask.OrderChangeAsyncTask;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.activity.TakeOrderActivity;
import com.me.salik.view.base.BaseFragment;

import java.util.HashMap;

/**
 * Created by MAC on 6/30/16.
 */
public class OrdersMapFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener{

    View rootView;
    HomeActivity homeActivity;

    private GoogleMap mMap;
    private MapView mapView;
    Marker currLocationMarker;
    LatLng latLng;

    private GPSTracker mGpsTracker;

    public HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    Marker marker;

    public OrdersMapFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_orders_map, container,false);
        homeActivity = (HomeActivity)getActivity();
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        initUI();
        return this.rootView;
    }
    private void initUI(){
        (rootView.findViewById(R.id.take_order)).setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setOnMarkerClickListener(this);

        mMap.setMyLocationEnabled(true);
        mGpsTracker = new GPSTracker(getContext(),mMap);
        latLng = new LatLng(mGpsTracker.getLatitude(), mGpsTracker.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        setOrderMaker();

    }

    private void addMyMaker(LatLng latLng) {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        markerOptions.title("My Location");
        currLocationMarker = mMap.addMarker(markerOptions);
    }


    private void setOrderMaker() {

        for (int i = 0; i < DataManagement.getInstance().getOrdersCount(); i++) {
            latLng = new LatLng(DataManagement.getInstance().getOrderInfo(i).getOrder_location_latitude(),
                    DataManagement.getInstance().getOrderInfo(i).getOrder_location_longitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(String.valueOf(DataManagement.getInstance().getOrderInfo(i).getOrder_id()));
            markerOptions.snippet(DataManagement.getInstance().getOrderInfo(i).getOrder_location_address());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            marker = mMap.addMarker(markerOptions);
            mHashMap.put(marker, i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.take_order){
            if (checkSelectedMaker()){
                homeActivity.setParams1();
                new OrderChangeAsyncTask(homeActivity, homeActivity.getParams1()).execute();
            } else {
                showMsg("Please Select one marker");
            }
        }
    }

    private boolean checkSelectedMaker(){
        return homeActivity.index != -1;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("My Location")){
            homeActivity.index = -1;
        } else {
            homeActivity.index = mHashMap.get(marker);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        }
        return false;
    }
}
