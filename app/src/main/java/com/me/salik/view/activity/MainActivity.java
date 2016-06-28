package com.me.salik.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.common.Common;
import com.me.salik.view.base.BaseActivity;
import com.me.salik.view.base.BaseFragment;
import com.me.salik.view.fragment.CustomerFragment;
import com.me.salik.view.fragment.CustomerListFragment;
import com.me.salik.view.fragment.CustomerMapFragment;
import com.me.salik.view.fragment.HistoryFragment;
import com.me.salik.view.fragment.HomeFragment;
import com.me.salik.view.fragment.LocationFragment;
import com.me.salik.view.fragment.SlideMenuFragment;
import com.me.salik.view.slidemenu.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements SlideMenuFragment.SideMenuItemClickListener, View.OnClickListener {


    BaseFragment fragment = null;

    SlidingMenu leftMenu;
    SlideMenuFragment slideMenuFragment;

    ImageButton menuButton;
    TextView navTitle;

    JSONObject params;

    public static FragmentManager fragmentManager;

    public static final int MENUITEM_HOME = 0, MENUITEM_LIST= 1,
            MENUITEM_MAP = 2,MENUITEM_HISTORY = 3, MENUITEM_LOG_OUT=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        initData();
        initUI();
        initLeftMenu();
        selectMenu(0);
    }

    public void initData(){
        setParams();
    }

    public void initUI(){
        menuButton = (ImageButton)findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this);

        navTitle = (TextView)findViewById(R.id.navTitle);

        fragmentManager = getSupportFragmentManager();
    }

    public void selectMenu(int position){

        switch (position){
            case MENUITEM_HOME:
                fragment = new HomeFragment();
                changeFragment(fragment);
                break;
            case MENUITEM_LIST:
                fragment = new CustomerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position", 0);
                fragment.setArguments(bundle);
                changeFragment(fragment);
                break;
            case MENUITEM_MAP:
                fragment = new CustomerFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", 1);
                fragment.setArguments(bundle1);
//                fragment = new LocationFragment();
                changeFragment(fragment);
                break;
            case MENUITEM_HISTORY:
                fragment = new HistoryFragment();
                changeFragment(fragment);
                break;
        }

    }

    public void changeFragment(BaseFragment fragment){
        if (fragment != null){
            showFragment(R.id.fragment_container, fragment, false, true, CUSTOM_ANIMATIONS.FADE_IN);
        }
    }

    public void setSlideMenuFragment(SlideMenuFragment fragment){
        slideMenuFragment = fragment;
    }

    @Override
    public void onMenuItemSelected (int position, String title){
        selectMenu(position);
        setTitle(title);
        toggleSlideMenu();
    }

    public void toggleSlideMenu() {
        leftMenu.toggle();
    }

    private void initLeftMenu(){
        leftMenu = new SlidingMenu(this);
        leftMenu.setMode(SlidingMenu.LEFT);
        leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        leftMenu.setBehindOffsetRes(R.dimen.behind_offset);
        leftMenu.setFadeDegree(0.5f);
        leftMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        leftMenu.setMenu(R.layout.slide_menu_container);
        leftMenu.setSlidingEnabled(true);
    }

    public void setTitle(String title){
        navTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menuButton:
                toggleSlideMenu();
                break;
        }
    }

    public void setParams(){
        params = new JSONObject();
        try {
            params.put(Common.DRIVER_NAME, apps.preference.getDriverName());
            params.put(Common.DRIVER_LOCATION_ADDRESS, apps.preference.getAddress());
            params.put(Common.DRIVER_LOCATION_LATITUDE, apps.preference.getLatitude());
            params.put(Common.DRIVER_LOCATION_LONGITUDE, apps.preference.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getParams(){
        return params;
    }

}
