package com.me.salik.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.me.salik.R;
import com.me.salik.modal.DataManagement;
import com.me.salik.view.activity.CustomerInfoActivity;
import com.me.salik.view.activity.MainActivity;
import com.me.salik.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MAC on 6/13/16.
 */
public class CustomerMapFragment extends BaseFragment implements OnMapReadyCallback,
        com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,  GoogleMap.OnInfoWindowClickListener{

    View rootView;

    private GoogleMap mMap;
    private MapView mapView;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Marker currLocationMarker;
    LatLng latLng;

    List<LatLng> routePoints = new ArrayList<>();
    public HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    Marker marker;

    public CustomerMapFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_customer_map, container, false);
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        initUI();
        return this.rootView;
    }

    public void initUI() {

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

    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(getActivity(), "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setOnInfoWindowClickListener(this);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        latLng = new LatLng(apps.preference.getLatitude(), apps.preference.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(8).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        addMyMaker(latLng);
        setOrderMaker();

    }



    @Override
    public void onLocationChanged(Location location) {

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        addMyMaker(latLng);
        routePoints.add(latLng);
        Polyline route = mMap.addPolyline(new PolylineOptions()
                .width(1)
                .color(R.color.mainColor)
                .geodesic(true)
                .zIndex(3));
        route.setPoints(routePoints);

//        Toast.makeText(getActivity(),"Location Changed", Toast.LENGTH_SHORT).show();

        //zoom to current position:
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(8).build();
//
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        Toast.makeText(getActivity(), "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
//            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            addMyMaker(latLng);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
//        Toast.makeText(getActivity(),"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(getActivity(),"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getActivity(), CustomerInfoActivity.class);
        intent.putExtra("id", mHashMap.get(marker));
        startActivity(intent);
    }
}
