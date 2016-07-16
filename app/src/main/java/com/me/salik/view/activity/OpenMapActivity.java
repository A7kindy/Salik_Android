package com.me.salik.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.me.salik.R;
import com.me.salik.modal.DataManagement;
import com.me.salik.view.base.BaseActivity;

public class OpenMapActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    Marker currLocationMarker;
    Marker marker;
    LatLng latLng;

    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_map);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            index = bundle.getInt("index", 0);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initUI();
    }

    private void initUI(){
        (findViewById(R.id.back)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back){
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(true);

        latLng = new LatLng(apps.preference.getLatitude(), apps.preference.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        addMyMaker(latLng);
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
        currLocationMarker = mMap.addMarker(markerOptions);
    }

    private void setOrderMaker() {

            latLng = new LatLng(DataManagement.getInstance().getOrderInfo(index).getOrder_location_latitude(),
                    DataManagement.getInstance().getOrderInfo(index).getOrder_location_longitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(String.valueOf(DataManagement.getInstance().getOrderInfo(index).getOrder_id()));
            markerOptions.snippet(DataManagement.getInstance().getOrderInfo(index).getOrder_location_address());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            marker = mMap.addMarker(markerOptions);
    }

}
