package com.me.salik.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.me.salik.R;
import com.me.salik.common.Common;
import com.me.salik.modal.DataManagement;
import com.me.salik.server.asyncTask.ClearHistoryAsyncTask;
import com.me.salik.server.asyncTask.GetHistoryAsyncTask;
import com.me.salik.server.asyncTask.OrderChangeAsyncTask;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.adapter.HistoryAdapter;
import com.me.salik.view.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MAC on 6/30/16.
 */
public class OrdersHistoryFragment extends BaseFragment implements View.OnClickListener{

    View rootView;
    ListView listView;
    HistoryAdapter adapter;

    Button clear;

    HomeActivity homeActivity;
    JSONObject clearParams;

    public OrdersHistoryFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_orders_history, container,false);
        homeActivity = (HomeActivity)getActivity();
        initUI();
        return this.rootView;
    }


    public void initUI(){
        listView = (ListView)rootView.findViewById(R.id.list_view);
        adapter = new HistoryAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new OrderHistoryDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                fragment.setArguments(bundle);
                showFragment(fragment);
            }
        });

        clear = (Button)rootView.findViewById(R.id.clear);
        clear.setOnClickListener(this);
    }

    private void showFragment(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.nested_fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear){
            if (DataManagement.getInstance().getHistoryCount() == 0){
                showMsg("You don't have any history");
            } else{
                showConfirmMessage("Clear History", "Do you want to clear history?");
            }

        }
    }


    public void showConfirmMessage(String title, String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(homeActivity);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setClearParams();
                new ClearHistoryAsyncTask(homeActivity, OrdersHistoryFragment.this, getClearParams()).execute();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void setClearParams(){
        clearParams = new JSONObject();
        try {
            clearParams.put(Common.DRIVER_ID, apps.preference.getDriverId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getClearParams(){
        return clearParams;
    }

    public void clearHistorySuccess(JSONObject object){
        try {
            if (object.getInt(Common.STATUS) == 1){
                DataManagement.getInstance().removeAllHistory();
                adapter.notifyDataSetChanged();
            } else {
                clearHistoyUnsuccess();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void clearHistoyUnsuccess(){
        showMsg(homeActivity.getString(R.string.connection_error));
    }




}
